package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.config.PartymodeConfig
import io.github.davidmerrick.partymode.external.twilio.TwilioParams
import javax.inject.Singleton

@Singleton
class CallFilter(private val config: PartymodeConfig.PhoneConfig) {

    /**
     * Returns true if the call should be accepted, false otherwise
     */
    fun apply(params: TwilioParams): Boolean {
        return params.from()?.let {
            it.contains(config.callboxNumber) || it.contains(config.myNumber)
        } ?: false
    }
}