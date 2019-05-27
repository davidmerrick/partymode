package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.echobot.slack.SlackCallbackMessage
import com.merricklabs.echobot.slack.SlackChallengeMessage
import com.merricklabs.echobot.slack.SlackMessage
import com.merricklabs.partymode.bots.PartyBot
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.util.PartymodeObjectMapper
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val mapper: PartymodeObjectMapper by inject()
    private val bot: PartyBot by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        // This ugliness is necessary because
        // if endpoint is event handler, body is given as an object, otherwise as a String
        val body = when (input["body"]) {
            is String -> input["body"] as String
            else -> input["body"] as Any
        }
        val message = deserialize(body, SlackMessage::class.java)
        log.info("Received payload: $body")
        return when (message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = deserialize(body, SlackChallengeMessage::class.java)
                ApiGatewayResponse(200, challengeMessage.challenge)
            }
            "event_callback" -> {
                val callbackMessage = deserialize(body, SlackCallbackMessage::class.java)
                bot.handle(callbackMessage)
                ApiGatewayResponse(200, "ok")
            }
            else -> ApiGatewayResponse(200, "ok")
        }
    }

    private fun <T> deserialize(body: Any, clazz: Class<T>): T = if (body is String) {
        mapper.readValue(body, clazz)
    } else {
        mapper.convertValue(body, clazz)
    }
}

