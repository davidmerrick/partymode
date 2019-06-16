package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.echobot.slack.SlackCallbackMessage
import com.merricklabs.echobot.slack.SlackChallengeMessage
import com.merricklabs.echobot.slack.SlackMessage
import com.merricklabs.partymode.bots.PartyBot
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {
    private val bot: PartyBot by inject()

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val message = input.body as SlackMessage
        return when (message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = input.body as SlackChallengeMessage
                APIGatewayProxyResponseEvent().apply {
                    statusCode = 200
                    body = challengeMessage.challenge
                }
            }
            "event_callback" -> {
                val callbackMessage = input.body as SlackCallbackMessage
                bot.handle(callbackMessage)
                APIGatewayProxyResponseEvent().apply {
                    statusCode = 200
                    body = "ok"
                }
            }
            else -> APIGatewayProxyResponseEvent().apply {
                statusCode = 200
                body = "ok"
            }
        }
    }
}

