package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.echobot.slack.SlackCallbackMessage
import com.merricklabs.echobot.slack.SlackChallengeMessage
import com.merricklabs.echobot.slack.SlackMessage
import com.merricklabs.partymode.bots.PartyBot
import com.merricklabs.partymode.models.ApiGatewayResponse
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val helpers: HandlerHelpers by inject()
    private val bot: PartyBot by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val body = input["body"]
        val message = helpers.deserializeInput(body, SlackMessage::class.java)
        log.info("Received payload: $body")
        return when (message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = helpers.deserializeInput(body, SlackChallengeMessage::class.java)
                ApiGatewayResponse(200, challengeMessage.challenge)
            }
            "event_callback" -> {
                val callbackMessage = helpers.deserializeInput(body, SlackCallbackMessage::class.java)
                bot.handle(callbackMessage)
                ApiGatewayResponse(200, "ok")
            }
            else -> ApiGatewayResponse(200, "ok")
        }
    }
}

