package io.github.davidmerrick.partymode.handlers

import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class SlackMessageHandlerLogic {
//    private val bot: PartyBot by inject()
//    private val slackClient: SlackClient by inject()
//    private val helpers: HandlerHelpers by inject()
//
//    override fun handleRequest(request: Map<String, Any>, context: Context): APIGatewayProxyResponseEvent {
//        if(!validateRequest(request)){
//            return APIGatewayProxyResponseEvent().apply {
//                statusCode = HttpStatus.SC_BAD_REQUEST
//            }
//        }
//
//        val requestBody = request["body"]
//        val message = helpers.deserializeInput(requestBody, SlackMessage::class.java)
//        return when (message.type) {
//            URL_VERIFICATION -> {
//                log.info("Received challenge")
//                val challengeMessage = helpers.deserializeInput(requestBody, SlackChallengeMessage::class.java)
//                helpers.okResponse(challengeMessage.challenge)
//            }
//            EVENT_CALLBACK -> {
//                val callbackMessage = helpers.deserializeInput(requestBody, SlackCallbackMessage::class.java)
//                if (bot.shouldHandle(callbackMessage)) {
//                    val response = bot.handle(callbackMessage)
//                    slackClient.sendReply(response)
//                }
//                helpers.okResponse()
//            }
//            else -> helpers.okResponse()
//        }
//    }
//
//    private fun validateRequest(request: Map<String, Any>): Boolean {
//        val headers = request["headers"] as Map<String, String>
//        if(!headers.containsKey(SLACK_SIGNATURE)){
//            log.warn("Headers do not contain Slack signature")
//            return false
//        }
//
//        return true
//    }
}

