package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val mapper: ObjectMapper by inject()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val requestBody = request.body as Any
        val message = mapper.convertValue(requestBody, SlackMessage::class.java)
        return when (message.type) {
            "url_verification" -> {
                log.info("Received challenge")
                val challengeMessage = mapper.convertValue(request.body, SlackChallengeMessage::class.java)
                APIGatewayProxyResponseEvent().apply {
                    statusCode = 200
                    body = challengeMessage.challenge
                }
            }
            "event_callback" -> {
                val callbackMessage = mapper.convertValue(request.body, SlackCallbackMessage::class.java)
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

