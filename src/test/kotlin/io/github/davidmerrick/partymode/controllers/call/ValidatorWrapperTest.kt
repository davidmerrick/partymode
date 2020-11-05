package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.config.TwilioConfig
import io.github.davidmerrick.partymode.external.twilio.PartymodeCallRequestValidator
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

@MicronautTest(application = TestApplication::class)
class ValidatorWrapperTest {
    @get:MockBean(TwilioConfig::class)
    val twilioConfig = mockk<TwilioConfig>()

    init {
        every { twilioConfig.authToken } returns "12345"
    }

    @Test
    fun `Confirm validation works`() {
        val requestValidator = PartymodeCallRequestValidator(twilioConfig)

        // Note that this assumes the Twilio authtoken is set to "12345"
        val url = "https://mycompany.com/myapp.php?foo=1&bar=2"
        val params = mapOf(
            "CallSid" to "CA1234567890ABCDE",
            "Caller" to "+12349013030",
            "Digits" to "1234",
            "From" to "+12349013030",
            "To" to "+18005551212"
        )
        val twilioSignature = "0/KCTR6DLpKmkAf8muzZqo1nDgQ="

        requestValidator.validate(url, params, twilioSignature) shouldBe true
    }
}
