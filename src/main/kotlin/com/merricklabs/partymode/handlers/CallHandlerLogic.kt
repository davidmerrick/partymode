package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.merricklabs.partymode.models.ApiGatewayResponse
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.util.PartymodeUtil
import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.time.Instant

private val log = KotlinLogging.logger {}

class CallHandlerLogic : RequestHandler<Map<String, Any>, ApiGatewayResponse>, KoinComponent {
    private val storage: PartymodeStorage by inject()

    override fun handleRequest(input: Map<String, Any>, context: Context): ApiGatewayResponse {
        return ApiGatewayResponse.build {
            rawBody = getResponse().toXml()
            headers = mapOf("Content-Type" to "application/xml")
        }
    }

    private fun getResponse(): VoiceResponse {
        val item = storage.getLatestItem()
        log.info("Got item: $item")
        val shouldBuzz = PartymodeUtil.shouldBuzz(Instant.parse(item.startTime), item.timeout)
        if (shouldBuzz) {
            log.info("Buzzing someone in.")
            return VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build()) // Todo: these DTMF tones are pretty short. Might want to use an mp3
                    .build()
        }

        val myNumber = Number.Builder(System.getenv("MY_NUMBER")).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(myNumber).build())
                .build()
    }
}

