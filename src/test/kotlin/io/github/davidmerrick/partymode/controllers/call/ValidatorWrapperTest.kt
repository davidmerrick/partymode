package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.external.twilio.TwilioValidatorWrapper
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject


@MicronautTest(application = TestApplication::class)
class ValidatorWrapperTest {

    @Inject
    lateinit var twilioValidatorWrapper: TwilioValidatorWrapper

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
        ).asSequence()
                .map { it.key + "=" + it.value }
                .joinToString("&")
        val signature = "0/KCTR6DLpKmkAf8muzZqo1nDgQ="

        twilioValidatorWrapper.validate(params, url, signature)
    }

}