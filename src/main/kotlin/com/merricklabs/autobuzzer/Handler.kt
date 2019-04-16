package com.merricklabs.autobuzzer

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Say
import org.apache.logging.log4j.LogManager


class Handler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        val say = Say.Builder(
                "Hello from your pals at Twilio! Have fun.")
                .build()
        val voiceResponse = VoiceResponse.Builder()
                .say(say)
                .build()
        return ApiGatewayResponse.build {
            rawBody = voiceResponse.toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    companion object {
        private val LOG = LogManager.getLogger(Handler::class.java)
    }
}
