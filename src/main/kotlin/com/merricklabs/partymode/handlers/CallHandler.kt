package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.merricklabs.partymode.PartymodeModule
import org.koin.core.context.startKoin

class CallHandler : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private val callHandlerImpl: CallHandlerLogic

    init {
        startKoin {
            modules(PartymodeModule)
        }
        callHandlerImpl = CallHandlerLogic()
    }

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return callHandlerImpl.handleRequest(input, context)
    }
}