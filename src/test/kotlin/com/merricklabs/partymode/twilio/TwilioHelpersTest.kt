package com.merricklabs.partymode.twilio

import com.merricklabs.partymode.PartymodeIntegrationTestBase
import io.kotlintest.shouldBe
import org.koin.core.inject
import org.testng.annotations.Test

class TwilioHelpersTest : PartymodeIntegrationTestBase() {

    private val twilioHelpers: TwilioHelpers by inject()

    @Test(description = "Taken from example at https://www.twilio.com/docs/usage/security")
    fun `Valid request should pass validation`() {
        val exampleHeader = "0/KCTR6DLpKmkAf8muzZqo1nDgQ="
        val params = mapOf(
                "CallSid" to "CA1234567890ABCDE",
                "Caller" to "+12349013030",
                "Digits" to "1234",
                "From" to "+12349013030",
                "To" to "+18005551212"
        )
        val didValidate = twilioHelpers.validateRequest(params, exampleHeader)
        didValidate shouldBe true
    }

    @Test
    fun `Invalid request should not pass validation`() {
        val exampleHeader = "0/KCTR6DLpKmkAf8muzZqo1nDgQ="
        val params = mapOf(
                "CallSid" to "CA1234567890ABCDE",
                "Caller" to "+12349013030",
                "Digits" to "9999",
                "From" to "+12349013030",
                "To" to "+18005551212"
        )
        val didValidate = twilioHelpers.validateRequest(params, exampleHeader)
        didValidate shouldBe false
    }
}