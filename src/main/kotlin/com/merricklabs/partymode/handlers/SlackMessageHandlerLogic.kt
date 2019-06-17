package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.partymode.bots.PartyBot
import com.merricklabs.partymode.slack.SlackCallbackMessage
import com.merricklabs.partymode.slack.SlackChallengeMessage
import com.merricklabs.partymode.slack.SlackMessage
import com.merricklabs.partymode.slack.SlackMessageType.EVENT_CALLBACK
import com.merricklabs.partymode.slack.SlackMessageType.URL_VERIFICATION
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic : RequestHandler<Map<String, Any>, APIGatewayProxyResponseEvent>, KoinComponent {
    private val bot: PartyBot by inject()
    private val helpers: HandlerHelpers by inject()

    override fun handleRequest(request: Map<String, Any>, context: Context): APIGatewayProxyResponseEvent {
        val requestBody = request["body"]
        val message = helpers.deserializeInput(requestBody, SlackMessage::class.java)
        return when (message.type) {
            URL_VERIFICATION -> {
                log.info("Received challenge")
                val challengeMessage = helpers.deserializeInput(requestBody, SlackChallengeMessage::class.java)
                helpers.okResponse(challengeMessage.challenge)
            }
            EVENT_CALLBACK -> {
                val callbackMessage = helpers.deserializeInput(requestBody, SlackCallbackMessage::class.java)
                bot.handle(callbackMessage)
                helpers.okResponse()
            }
            else -> helpers.okResponse()
        }
    }
}

