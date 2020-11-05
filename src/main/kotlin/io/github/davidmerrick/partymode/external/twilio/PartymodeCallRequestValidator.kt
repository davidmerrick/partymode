package io.github.davidmerrick.partymode.external.twilio

import com.twilio.security.RequestValidator
import io.github.davidmerrick.partymode.config.TwilioConfig
import javax.inject.Singleton

/**
 * Wrapper class for Twilio validator so it can be mocked out during tests
 */
@Singleton
class PartymodeCallRequestValidator(config: TwilioConfig) : RequestValidator(config.authToken)
