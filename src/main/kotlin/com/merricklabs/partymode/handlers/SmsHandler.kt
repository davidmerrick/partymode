package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.PartymodeModule
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioUtil
import com.twilio.twiml.MessagingResponse
import com.twilio.twiml.messaging.Body
import com.twilio.twiml.messaging.Message
import mu.KotlinLogging
import org.koin.standalone.StandAloneContext.startKoin

private val log = KotlinLogging.logger {}

class SmsHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {

    private var storage: PartymodeStorage? = null
    private var config: PartymodeConfig? = null

    init {
        startKoin(listOf(PartymodeModule))
        storage = PartymodeStorage()
        config = PartymodeConfig()
    }

    override fun handleRequest(input: Map<String, Any>, context: Context?): ApiGatewayResponse {
        val twilioInput = TwilioUtil.parseInput(input["body"] as String)

        val sender = twilioInput.getValue("From")[0]
        if (!validateSender(sender)) {
            log.info("Validation failed for sender $sender. Must match my number: ${config!!.phone.myNumber}")

            // Simply don't respond to these. Otherwise someone could rack up our Twilio bill by hitting this endpoint a bunch of times
            return ApiGatewayResponse.build {
                statusCode = 400
            }
        }

        try {
            val numHours = twilioInput.getValue("Body")[0].toInt()
            storage!!.saveTimeToDb(numHours)
            val message = Message.Builder()
                    .body(Body.Builder("Enabling partymode for $numHours hours").build())
                    .to(twilioInput.getValue("From")[0])
                    .build()
            val response = MessagingResponse.Builder().message(message).build()

            return ApiGatewayResponse.build {
                rawBody = response.toXml()
                headers = mapOf("Content-Type" to "application/xml")
            }
        } catch (e: Exception) {
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
}
