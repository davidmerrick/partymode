package com.merricklabs.partymode.twilio

import com.merricklabs.partymode.config.PartymodeConfig
import com.twilio.security.RequestValidator
import org.koin.core.KoinComponent
import org.koin.core.inject

class TwilioHelpers : KoinComponent {
    private val config: PartymodeConfig by inject()

    fun validateRequest(twilioParams: TwilioParams, requestUrl: String, twilioSignatureHeader: String): Boolean {
        val validator = RequestValidator(config.twilio.authToken)

        return validator.validate(requestUrl, twilioParams.validationParams(), twilioSignatureHeader)
    }
}