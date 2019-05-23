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
import com.merricklabs.partymode.storage.PartymodeStorage
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
        val message = mapper.convertValue(input["body"], SlackMessage::class.java)
        log.info("Received payload: ${input["body"]}")
        return when (message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = mapper.convertValue(input["body"], SlackChallengeMessage::class.java)
                ApiGatewayResponse(200, challengeMessage.challenge)
            }
            "event_callback" -> {
                val callbackMessage = mapper.convertValue(input["body"], SlackCallbackMessage::class.java)
                // Only respond to at-mentions and ignore other bot messages
                if (isAtMention(callbackMessage)) {
                    log.info("Is an at-mention of our bot.")
                    handleMention(callbackMessage)
                }
                ApiGatewayResponse(200, "ok")
            }
            else -> ApiGatewayResponse(200, "ok")
        }
    }

    private fun isAtMention(message: SlackCallbackMessage) = message.event.type == "app_mention"
            && message.event.text.contains("<@${config.slack.botUserId}>")
            && message.event.bot_id == null

    private fun handleMention(message: SlackCallbackMessage) {
        log.info("Handling at-mention")
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)

        when (message.event.text) {
            in Regex(".*pm help$") -> sendReply(message, HELP_TEXT)
            in Regex(".*pm [1-5]$") -> {
                val regex = "[1-5]$".toRegex()
                val numHours = regex.find(message.event.text)!!.value.toInt()
                storage.saveTimeToDb(numHours)
                val suffix = if (numHours > 1) "hours" else "hour"
                sendReply(message, "partymode enabled for $numHours $suffix")
            }
            else -> sendReply(message, HELP_TEXT)
        }
    }

    private fun sendReply(message: SlackCallbackMessage, text: String) {
        val okHttpClient = OkHttpClient()
        val json = MediaType.get("application/json; charset=utf-8")
        val responseMessage = SlackBotMessage(message.event.channel, "<@${message.event.user}>: $text")
        val body = RequestBody.create(json, mapper.writeValueAsString(responseMessage))
        log.info("Sending message back to Slack: ${mapper.writeValueAsString(responseMessage)}")
        val request = Request.Builder()
                .header("Authorization", "Bearer ${config.slack.botToken}")
                .url(SLACK_URL)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        log.info("Got response from Slack API: $response with body: ${response.body()!!.string()}")
    }

    companion object {
        const val SLACK_URL = "https://slack.com/api/chat.postMessage"
        const val HELP_TEXT = "Usage:\npm `[1-5]`: Enable partymode for n hours"
    }
}

