package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeModule
import com.merricklabs.partymode.models.ApiGatewayResponse
import org.koin.standalone.StandAloneContext

class SlackMessageHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    private val handlerImpl: SlackMessageHandlerLogic

    init {
        StandAloneContext.startKoin(listOf(PartymodeModule))
        handlerImpl = SlackMessageHandlerLogic()
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        return handlerImpl.handleRequest(input, context)
    }
}