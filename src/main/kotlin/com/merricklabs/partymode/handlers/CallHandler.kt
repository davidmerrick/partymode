package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeModule
import com.merricklabs.partymode.models.ApiGatewayResponse
import org.koin.standalone.StandAloneContext

class CallHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    var callHandlerImpl: CallHandlerImpl? = null

    init {
        StandAloneContext.startKoin(listOf(PartymodeModule))
    }

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        return callHandlerImpl!!.handleRequest(input, context)
    }
}