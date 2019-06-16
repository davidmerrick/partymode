package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.partymode.PartymodeModule
import org.koin.core.context.startKoin

class SlackMessageHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val handlerImpl: SlackMessageHandlerLogic

    init {
        startKoin {
            modules(PartymodeModule)
        }

        handlerImpl = SlackMessageHandlerLogic()
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return handlerImpl.handleRequest(input, context)
    }
}