package io.github.davidmerrick.partymode.external.google

import com.google.auth.oauth2.TokenVerifier
import io.github.davidmerrick.partymode.config.GoogleAssistantConfig
import mu.KotlinLogging
import javax.inject.Singleton

private const val GOOGLE_ISSUER = "https://accounts.google.com"

private val log = KotlinLogging.logger {}

@Singleton
class GoogleAssistantTokenValidator(config: GoogleAssistantConfig) {
    private val verifier = TokenVerifier.newBuilder()
        .setAudience(config.appName)
        .setIssuer(GOOGLE_ISSUER)
        .build()

    fun validate(token: String): Boolean {
        log.info("Validating Google Assistant token")

        return try {
            verifier.verify(token)
            true
        } catch (e: TokenVerifier.VerificationException) {
            log.warn("Exception validating Google token", e)
            false
        }
    }
}
