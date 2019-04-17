package com.merricklabs.partymode

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import org.apache.logging.log4j.LogManager


class CallHandler : RequestHandler<Map<String, Any>, ApiGatewayResponse> {
    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        return ApiGatewayResponse.build {
            rawBody = getResponse().toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    private fun getResponse(): VoiceResponse {
        val shouldBuzz = System.getenv("SHOULD_BUZZ") != null && System.getenv("SHOULD_BUZZ").toBoolean()
        if (shouldBuzz) {
            LOG.info("Buzzing someone in.")
            return VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build()) // Todo: these DTMF tones are pretty short. Might want to use an mp3
                    .build()
        }

        val myNumber = Number.Builder(System.getenv("MY_NUMBER")).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(myNumber).build())
                .build()
    }

    companion object {
        private val LOG = LogManager.getLogger(CallHandler::class.java)
    }
}
