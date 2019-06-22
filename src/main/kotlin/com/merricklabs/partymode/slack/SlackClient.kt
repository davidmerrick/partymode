package com.merricklabs.partymode.slack

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.partymode.bots.PartyBot
import com.merricklabs.partymode.config.PartymodeConfig
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class SlackClient : KoinComponent {

    private val mapper by inject<ObjectMapper>()
    private val config by inject<PartymodeConfig>()

    fun sendReply(message: SlackBotMessage) {
        val okHttpClient = OkHttpClient()
        val json = MediaType.get("application/json; charset=utf-8")
        val body = RequestBody.create(json, mapper.writeValueAsString(message))
        log.info("Sending message back to Slack: ${mapper.writeValueAsString(message)}")
        val request = Request.Builder()
                .header("Authorization", "Bearer ${config.slack.botToken}")
                .url(PartyBot.SLACK_URL)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        log.info("Got response from Slack API: $response with body: ${response.body()!!.string()}")
    }
}