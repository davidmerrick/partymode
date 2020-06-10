package io.github.davidmerrick.partymode.external.twilio

import com.twilio.security.RequestValidator
import io.github.davidmerrick.partymode.config.PartymodeConfig.TwilioConfig
import mu.KotlinLogging
import javax.inject.Singleton

/**
 * Wrapper class for Twilio validator so it can be mocked out during tests
 */
private val log = KotlinLogging.logger {}

@Singleton
class TwilioValidatorWrapper(config: TwilioConfig) {
    private val validator = RequestValidator(config.authToken)

    // Todo: Delete this
    init {
        log.info("Created validator with authToken: ${config.authToken}")
    }

    fun validate(body: String, requestUrl: String, twilioSignature: String): Boolean {
        val twilioParams = TwilioParams(body)
        return validator.validate(requestUrl, twilioParams.paramMap, twilioSignature)
    }
}