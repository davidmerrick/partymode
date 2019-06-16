package com.merricklabs.partymode.twilio

import com.merricklabs.partymode.config.PartymodeConfig
import com.twilio.security.RequestValidator
import org.koin.core.KoinComponent
import org.koin.core.inject

class TwilioHelpers : KoinComponent {
    private val config: PartymodeConfig by inject()

    fun validateRequest(twilioParams: TwilioParams, twilioSignatureHeader: String): Boolean {
        val validator = RequestValidator(config.twilio.authToken)

        // The Twilio request URL
        val url = "https://mycompany.com/myapp.php?foo=1&bar=2"

        return validator.validate(url, twilioParams.validationParams(), twilioSignatureHeader)
    }
}