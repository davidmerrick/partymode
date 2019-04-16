package com.merricklabs.autobuzzer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.autobuzzer.twilio.TwilioUtil
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Message
import org.apache.logging.log4j.LogManager

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val twilioInput = TwilioUtil.parseInput(input["body"] as String)

        // Echo back the body
        val message = Message.Builder(twilioInput.getValue("Body")[0])
                .to(twilioInput.getValue("From")[0])
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
