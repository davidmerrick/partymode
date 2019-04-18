package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeModule
import com.merricklabs.partymode.models.ApiGatewayResponse
import org.koin.standalone.StandAloneContext.startKoin

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    var smsHandlerImpl: SmsHandlerImpl? = null

    init {
        startKoin(listOf(PartymodeModule))
        smsHandlerImpl = SmsHandlerImpl()
    }

    override fun handleRequest(input: Map<String, Any>, context: Context?): ApiGatewayResponse {
        return smsHandlerImpl!!.handleRequest(input, context)
    }
}