package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.koin.core.KoinComponent
import org.koin.core.inject

class HandlerHelpers : KoinComponent {
    private val mapper: ObjectMapper by inject()

    // This ugliness is necessary because
    // if endpoint is event handler, body is given as an object, otherwise as a String
    fun <T> deserializeInput(body: Any?, clazz: Class<T>): T = if (body is String) {
        mapper.readValue(body, clazz)
    } else {
        mapper.convertValue(body, clazz)
    }

    fun okResponse() = APIGatewayProxyResponseEvent().apply {
        statusCode = 200
        body = "ok"
    }

    fun okResponse(responseBody: String) = APIGatewayProxyResponseEvent().apply {
        statusCode = 200
        body = responseBody
    }
}