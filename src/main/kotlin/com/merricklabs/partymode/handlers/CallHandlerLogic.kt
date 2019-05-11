package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.slack.SlackNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

private val log = KotlinLogging.logger {}

class CallHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val storage: PartymodeStorage by inject()
    private val config: PartymodeConfig by inject()
    private val slackNotifier: SlackNotifier by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        return ApiGatewayResponse.build {
            rawBody = getResponse().toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    private fun getResponse(): VoiceResponse {
        val partyLease = storage.getLatestItem()
        log.info("Got lease: $partyLease")
        if (partyLease.isActive()) {
            log.info("Buzzing someone in.")
            try {
                slackNotifier.notify("Buzzed someone in")
            } catch (e: Exception){
                log.error("Error posting Slack notification", e)
            }
            return VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build()) // Todo: these DTMF tones are pretty short. Might want to use an mp3
                    .build()
        }

        val number = Number.Builder(config.phone.myNumber).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(number).build())
                .build()
    }
}

