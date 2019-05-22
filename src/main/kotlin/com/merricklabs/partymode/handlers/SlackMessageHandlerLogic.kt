package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.merricklabs.echobot.slack.SlackBotMessage
import com.merricklabs.echobot.slack.SlackCallbackMessage
import com.merricklabs.echobot.slack.SlackChallengeMessage
import com.merricklabs.echobot.slack.SlackMessage
import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.slack.SlackNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioUtil
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val mapper = ObjectMapper()
    private val storage: PartymodeStorage by inject()
    private val config: PartymodeConfig by inject()

    init {
        mapper.registerModule(KotlinModule())
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val body = input!!["body"] as String
        log.info("Received payload: $body")
        val message = mapper.readValue(body, SlackMessage::class.java)
        return when(message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = mapper.readValue(body, SlackChallengeMessage::class.java)
                ApiGatewayResponse(200, challengeMessage.challenge)
            }
            "event_callback" -> {
                val callbackMessage = mapper.readValue(body, SlackCallbackMessage::class.java)
                if(callbackMessage.event.isAtMention() && callbackMessage.event.text.contains("<@${config.slack.botUserName}>")){
                    log.info("Is an at-mention of our bot.")
                    sendReply(callbackMessage)
                }
                ApiGatewayResponse(200)
            }
            else -> ApiGatewayResponse(200)
        }
    }

    private fun sendReply(message: SlackCallbackMessage){
        val message = SlackBotMessage(message.event.channel, message.event.text.replace("<@${config.slack.botUserName}>", "<@${message.event.user}>").trim())
        val okHttpClient = OkHttpClient()
        val json = MediaType.get("application/json; charset=utf-8")
        val body = RequestBody.create(json, mapper.writeValueAsString(message))
        log.info("Sending message back to Slack: ${mapper.writeValueAsString(message)}")
        val request = Request.Builder()
                .header("Authorization", "Bearer ${config.slack.botToken}")
                .url(SLACK_URL)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        log.info("Got response from Slack API: $response")
    }

    companion object {
        const val SLACK_URL = "https://slack.com/api/chat.postMessage"
    }
}

