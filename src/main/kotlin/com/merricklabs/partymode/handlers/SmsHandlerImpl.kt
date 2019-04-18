package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioUtil
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Body
import com.twilio.twiml.messaging.Message
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

private val log = KotlinLogging.logger {}

@Suppress("NAME_SHADOWING")
class SmsHandlerImpl : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val storage: PartymodeStorage by inject()
    private val config: PartymodeConfig by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context?): ApiGatewayResponse {
        val twilioInput = TwilioUtil.parseInput(input["body"] as String)

        val sender = twilioInput.getValue("From")[0]
        if (!validateSender(sender)) {
            log.info("Validation failed for sender $sender. Must match my number: ${config.phone.myNumber}")

            // Simply don't respond to these. Otherwise someone could rack up our Twilio bill by hitting this endpoint a bunch of times
            return ApiGatewayResponse.build {
                statusCode = 400
            }
        }

        try {
            val numHours = twilioInput.getValue("Body")[0].toInt()
            if (numHours <= 0) {
                return createMessageResponse(twilioInput, "Please specify a number greater than 0.")
            } else if (numHours > MAX_HOURS) {
                return createMessageResponse(twilioInput, "Please specify a number less than $MAX_HOURS.")
            }

            storage.saveTimeToDb(numHours)
            return createMessageResponse(twilioInput, "Enabling partymode for $numHours hours")
        } catch (e: Exception) {
            return createMessageResponse(twilioInput, "Please specify the number of hours to enable autobuzz for.")
        }
    }

    private fun createMessageResponse(twilioInput: Map<String, List<String>>, message: String): ApiGatewayResponse {
        val message = Message.Builder()
                .body(Body.Builder(message).build())
                .to(twilioInput.getValue("From")[0])
                .build()
        val response = MessagingResponse.Builder().message(message).build()
        return ApiGatewayResponse.build {
            rawBody = response.toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    private fun validateSender(sender: String): Boolean {
        return sender == System.getenv("MY_NUMBER")
    }

    companion object {
        const val MAX_HOURS = 5
    }
}
