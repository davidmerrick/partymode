package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.config.PartymodeConfig
import io.github.davidmerrick.partymode.external.twilio.TwilioCallPayload
import io.github.davidmerrick.partymode.external.twilio.TwilioFields.FROM
import io.kotlintest.shouldBe
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(application = TestApplication::class)
class CallFilterTest {

    @Inject
    lateinit var phoneConfig: PartymodeConfig.PhoneConfig

    @Inject
    lateinit var filter: CallFilter

    @Test
    fun `Accept calls from my callbox`() {
        val params = TwilioCallPayload("$FROM=${phoneConfig.callboxNumber}")
        filter.apply(params) shouldBe true
    }

    @Test
    fun `Accept calls from my phone number`() {
        val params = TwilioCallPayload("$FROM=${phoneConfig.myNumber}")
        filter.apply(params) shouldBe true
    }

    @Test
    fun `Reject all other calls`() {
        val invalidNumber = "+19999999999"
        val params = TwilioCallPayload("$FROM=$invalidNumber")
        filter.apply(params) shouldBe false
    }
}
