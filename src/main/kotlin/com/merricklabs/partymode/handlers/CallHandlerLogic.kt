package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.slack.SlackNotifier
import com.merricklabs.partymode.sns.SnsNotifier
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
    private val snsNotifier: SnsNotifier by inject()

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

    private fun pushNotifications(){
        val message = "Buzzed someone in"
        // Push to SNS topic
        if(config.sns.topicArn != null){
            snsNotifier.notify(message)
        }

        // Push to Slack
        try {
            slackNotifier.notify(message)
        } catch (e: Exception){
            log.error("Error posting Slack notification", e)
        }
    }
}

