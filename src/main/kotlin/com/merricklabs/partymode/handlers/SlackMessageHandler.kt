package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.partymode.PartymodeModule
import org.koin.core.context.startKoin

class SlackMessageHandler : RequestHandler<Map<String, Any>, APIGatewayProxyResponseEvent> {
    private val handlerImpl: SlackMessageHandlerLogic

    init {
        startKoin {
            modules(PartymodeModule)
        }

        handlerImpl = SlackMessageHandlerLogic()
    }

    override fun handleRequest(request: Map<String, Any>, context: Context): APIGatewayProxyResponseEvent {
        return handlerImpl.handleRequest(request, context)
    }
}