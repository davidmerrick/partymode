package com.merricklabs.autobuzzer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.autobuzzer.twilio.TwilioUtil
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Body
import com.twilio.twiml.messaging.Message
import org.apache.logging.log4j.LogManager

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val twilioInput = TwilioUtil.parseInput(input["body"] as String)

        val sender = twilioInput.getValue("From")[0]
        if(!validateSender(sender)){
            LOG.error("Validation failed for sender $sender. Must match my number: ${System.getenv("MY_NUMBER")}")
            return ApiGatewayResponse.build {
                statusCode = 400
            }
        }

        try {
            val numHours = twilioInput.getValue("Body")[0].toInt()
            val message = Message.Builder()
                    .body(Body.Builder("Enabling autobuzz for $numHours hours").build())
                    .to(twilioInput.getValue("From")[0])
                    .build()
            val response = MessagingResponse.Builder().message(message).build()

            return ApiGatewayResponse.build {
                rawBody = response.toXml()
                headers = mapOf("Content-Type" to "application/xml")
            }
        } catch(e: Exception){
            val message = Message.Builder()
                    .body(Body.Builder("Please specify the number of hours to enable autobuzz for.").build())
                    .to(twilioInput.getValue("From")[0])
                    .build()
            val response = MessagingResponse.Builder().message(message).build()

            return ApiGatewayResponse.build {
                rawBody = response.toXml()
                headers = mapOf("Content-Type" to "application/xml")
            }
        }
    }

    private fun validateSender(sender: String): Boolean {
        return sender == System.getenv("MY_NUMBER")
    }

    companion object {
        private val LOG = LogManager.getLogger(SmsHandler::class.java)
    }
}
