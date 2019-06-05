package com.merricklabs.partymode.bots

import com.merricklabs.echobot.slack.SlackBotMessage
import com.merricklabs.echobot.slack.SlackCallbackMessage
import com.merricklabs.partymode.slack.SlackNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.util.PartymodeObjectMapper
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.koin.standalone.inject

private val log = KotlinLogging.logger {}

class PartyBot : SlackBot() {
    private val storage: PartymodeStorage by inject()
    private val mapper: PartymodeObjectMapper by inject()
    private val slackNotifier: SlackNotifier by inject()

    override fun handle(message: SlackCallbackMessage) {
        if (!shouldHandle(message)) {
            return
        }

        log.info("Handling message")
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