package io.github.davidmerrick.partymode.controllers.call

import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import com.twilio.twiml.voice.Reject
import io.github.davidmerrick.partymode.config.PartymodeConfig.PhoneConfig
import io.github.davidmerrick.partymode.external.twilio.TwilioParams
import io.github.davidmerrick.partymode.storage.PartymodeStorage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class CallHandlerLogic(
        private val storage: PartymodeStorage,
        private val filter: CallFilter,
        private val config: PhoneConfig
) {
    fun handleRequest(body: String): String {
        val twilioParams = TwilioParams(body)
        if (filter.apply(twilioParams)) {
            log.info("Received a valid call.")
            return buildResponse()
        }

        log.info("Invalid call. Rejecting.")
        return buildRejectResponse()
    }

    private fun buildRejectResponse(): String {
        val body = VoiceResponse.Builder()
                .reject(Reject.Builder().build())
                .build()
        return body.toXml()
    }

    private fun buildResponse(): String {
        if (storage.isPartymodeEnabled()) {
            log.info("Buzzing someone in.")
            val body = VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build())
                    .build()
            return body.toXml()
        }

        val number = Number.Builder(config.myNumber).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(number).build())
                .build()
                .toXml()
    }
}