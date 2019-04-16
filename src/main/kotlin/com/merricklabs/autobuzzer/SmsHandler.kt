package com.merricklabs.autobuzzer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Message
import org.apache.logging.log4j.LogManager

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val message = Message.Builder("foo")
                .to(System.getenv("MY_NUMBER"))
                .build()
        val response = MessagingResponse.Builder().message(message).build()

        return ApiGatewayResponse.build {
            rawBody = response.toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger(SmsHandler::class.java)
    }
}
