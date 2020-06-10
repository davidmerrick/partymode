package io.github.davidmerrick.partymode.controllers.call

import com.twilio.twiml.VoiceResponse
import com.twilio.twiml.voice.Dial
import com.twilio.twiml.voice.Number
import com.twilio.twiml.voice.Play
import com.twilio.twiml.voice.Reject
import io.github.davidmerrick.partymode.config.PartymodeConfig.PhoneConfig
import io.github.davidmerrick.partymode.external.twilio.TwilioCallPayload
import io.github.davidmerrick.partymode.pubsub.NotificationProducer
import io.github.davidmerrick.partymode.storage.PartymodeStorage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class CallHandlerLogic(
        private val storage: PartymodeStorage,
        private val filter: CallFilter,
        private val phoneConfig: PhoneConfig,
        private val producer: NotificationProducer
) {
    /**
     * Returns a TwiML XML voice response
     */
    fun handleRequest(callPayload: TwilioCallPayload): String {
        if (filter.apply(callPayload)) {
            log.info("Received a valid call.")
            return acceptCall()
        }

        log.info("Invalid call. Rejecting.")
        return rejectCall()
    }

    private fun rejectCall(): String {
        val body = VoiceResponse.Builder()
                .reject(Reject.Builder().build())
                .build()
        return body.toXml()
    }

    private fun acceptCall(): String {
        if (storage.isPartymodeEnabled()) {
            log.info("Buzzing someone in.")
            val body = VoiceResponse.Builder()
                    .play(Play.Builder().digits("ww999").build())
                    .build()
            producer.produce("Buzzed someone in")
            return body.toXml()
        }

        val number = Number.Builder(phoneConfig.myNumber).build()
        return VoiceResponse.Builder()
                .dial(Dial.Builder().number(number).build())
                .build()
                .toXml()
    }
}