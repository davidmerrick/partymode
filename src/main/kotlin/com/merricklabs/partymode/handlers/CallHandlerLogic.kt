package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.sns.SnsNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioHeaders.X_TWILIO_SIGNATURE
import com.merricklabs.partymode.twilio.TwilioHelpers
import com.merricklabs.partymode.twilio.TwilioParams
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import com.twilio.twiml.voice.Reject
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CallHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val storage: PartymodeStorage by inject()
    private val config: PartymodeConfig by inject()
    private val snsNotifier: SnsNotifier by inject()
    private val twilioHelpers: TwilioHelpers by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val body = input["body"] as String
        log.info("Received input: $body")

        val twilioParams = TwilioParams(body)

        // Validate the request
        if (!validateRequest(twilioParams, input["headers"] as Map<String, String>)) {
            return buildRejectResponse()
        }

        twilioParams.from()?.let {
            if (it.contains(config.phone.callboxNumber) || it.contains(config.phone.myNumber)) {
                log.info("Received a valid call from callbox or my number.")
                return buildResponse()
            }
        }

        log.info("Invalid call. Rejecting.")
        return buildRejectResponse()
    }

    private fun validateRequest(twilioParams: TwilioParams, headers: Map<String, String>): Boolean {
        if (!twilioParams.isValidPayload()) {
            log.warn("Payload failed validation.")
            return false
        }

        if (!headers.containsKey(X_TWILIO_SIGNATURE)) {
            log.warn("Request headers does not contain Twilio signature.")
            return false
        }

        val requestUrl = "https://${headers["Host"]}${headers["resourcePath"]}"
        if (!twilioHelpers.validateRequest(twilioParams, requestUrl, headers[X_TWILIO_SIGNATURE]!!)) {
            log.warn("Request did not match signature.")
            return false
        }

        return true
    }

    private fun buildRejectResponse(): ApiGatewayResponse {
        val body = VoiceResponse.Builder()
                .reject(Reject.Builder().build())
                .build()
        return xmlResponse(body)
    }

    private fun buildResponse(): ApiGatewayResponse {
        val partyLease = storage.getLatestItem()
        log.info("Got lease: $partyLease")
        if (partyLease.isActive()) {
            log.info("Buzzing someone in.")
            pushNotifications()
            val body = VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build())
                    .build()
            return xmlResponse(body)
        }

        val number = Number.Builder(config.phone.myNumber).build()
        val body = VoiceResponse.Builder()
                .dial(Dial.Builder().number(number).build())
                .build()
        return xmlResponse(body)
    }

    private fun pushNotifications() {
        val message = "Buzzed someone in"
        if (config.sns.topicArn != null) {
            snsNotifier.notify(message)
        }
    }

    companion object {
        fun xmlResponse(body: VoiceResponse): ApiGatewayResponse {
            return ApiGatewayResponse.build {
                rawBody = body.toXml()
                headers = mapOf("Content-Type" to "application/xml")
            }
        }

    }
}
