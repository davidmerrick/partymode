package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.sns.SnsNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
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

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val body = input["body"] as String
        log.info("Received input: $body")

        val twilioParams = TwilioParams(body)

        twilioParams.from()?.let {
            if (it.contains(config.phone.callboxNumber) || it.contains(config.phone.myNumber)) {
                log.info("Received a valid call from callbox or my number.")
                return ApiGatewayResponse.build {
                    rawBody = buildResponse().toXml()
                    headers = mapOf("Content-Type" to "application/xml")
                }
            }
        }

        log.info("Invalid call. Rejecting.")
        return ApiGatewayResponse.build {
            rawBody = buildRejectResponse().toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    private fun buildRejectResponse(): VoiceResponse {
        return VoiceResponse.Builder()
                .reject(Reject.Builder().build())
                .build()
    }

    private fun buildResponse(): VoiceResponse {
        val partyLease = storage.getLatestItem()
        log.info("Got lease: $partyLease")
        if (partyLease.isActive()) {
            log.info("Buzzing someone in.")
            pushNotifications()
            return VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build())
                    .build()
        }

        val number = Number.Builder(config.phone.myNumber).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(number).build())
                .build()
    }

    private fun pushNotifications() {
        val message = "Buzzed someone in"
        if (config.sns.topicArn != null) {
            snsNotifier.notify(message)
        }
    }

    private companion object {
        const val FROM_FIELD = "From"
    }
}

