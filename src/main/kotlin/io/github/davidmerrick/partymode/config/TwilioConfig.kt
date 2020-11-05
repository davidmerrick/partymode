package io.github.davidmerrick.partymode.config

import io.github.davidmerrick.partymode.external.gcp.SecretsFetcher
import javax.inject.Singleton

private const val AUTH_TOKEN = "TWILIO_AUTH_TOKEN"

@Singleton
class TwilioConfig(private val secretsFetcher: SecretsFetcher) {
    val authToken by lazy { secretsFetcher.getSecret(AUTH_TOKEN) }
}
