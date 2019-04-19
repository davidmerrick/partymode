package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeModule
import com.merricklabs.partymode.models.ApiGatewayResponse
import org.koin.standalone.StandAloneContext.startKoin

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    private val smsHandlerImpl: SmsHandlerLogic

    init {
        startKoin(listOf(PartymodeModule))
        smsHandlerImpl = SmsHandlerLogic()
    }

    override fun handleRequest(input: Map<String, Any>, context: Context?): ApiGatewayResponse {
        return smsHandlerImpl.handleRequest(input, context)
    }
}