package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.sns.SnsNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioHeaders.TWILIO_SIGNATURE
import com.merricklabs.partymode.twilio.TwilioHelpers
import com.merricklabs.partymode.twilio.TwilioParams
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import com.twilio.twiml.voice.Reject
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CallHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {
    private val storage: PartymodeStorage by inject()
    private val config: PartymodeConfig by inject()
    private val snsNotifier: SnsNotifier by inject()
    private val twilioHelpers: TwilioHelpers by inject()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        if (!validateRequest(request)) {
            return APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_BAD_REQUEST
            }
        }

        val body = request.body
        log.info("Received body: $body")

        val twilioParams = TwilioParams(body)
        twilioParams.from()?.let {
            if (it.contains(config.phone.callboxNumber) || it.contains(config.phone.myNumber)) {
                log.info("Received a valid call from callbox or my number.")
                return buildResponse()
            }
        }

        log.info("Invalid call. Rejecting.")
        return buildRejectResponse()
    }

    private fun validateRequest(request: APIGatewayProxyRequestEvent): Boolean {
        val headers = request.headers
        if (!headers.containsKey(TWILIO_SIGNATURE)) {
            log.warn("Request headers does not contain Twilio signature.")
            return false
        }

        val twilioParams = TwilioParams(request.body)
        val requestUrl = request.getRequestUrl()
        if (!twilioHelpers.validateRequest(twilioParams, requestUrl, headers[TWILIO_SIGNATURE]!!)) {
            log.warn("Request did not match signature. " +
                    "Request url: $requestUrl, Signature: ${headers[TWILIO_SIGNATURE]}")
            return false
        }

        return true
    }

    private fun buildRejectResponse(): APIGatewayProxyResponseEvent {
        val body = VoiceResponse.Builder()
                .reject(Reject.Builder().build())
                .build()
        return xmlResponse(body)
    }

    private fun buildResponse(): APIGatewayProxyResponseEvent {
        if (storage.isPartymodeEnabled()) {
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
        fun xmlResponse(voiceResponse: VoiceResponse): APIGatewayProxyResponseEvent {
            return APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_OK
                body = voiceResponse.toXml()
                headers = mapOf("Content-Type" to "application/xml")
            }
        }
    }
}

fun APIGatewayProxyRequestEvent.getRequestUrl(): String {
    return "https://${this.headers["Host"]}${this.requestContext.path}"
}
