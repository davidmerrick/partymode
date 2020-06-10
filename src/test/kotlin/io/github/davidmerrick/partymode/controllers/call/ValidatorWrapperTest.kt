package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.external.twilio.PartymodeCallRequestValidator
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject


@MicronautTest(application = TestApplication::class)
class ValidatorWrapperTest {

    @Inject
    lateinit var requestValidator: PartymodeCallRequestValidator

    @Test
    fun `Confirm validation works`() {
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