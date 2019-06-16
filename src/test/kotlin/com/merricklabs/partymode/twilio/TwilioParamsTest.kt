package com.merricklabs.partymode.twilio

import io.kotlintest.matchers.haveKey
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import org.junit.Test

class TwilioParamsTest {

    @Test
    fun `Should only return validation params`() {
        val testMap = mapOf(
                "CallSid" to "CA1234567890ABCDE",
                "Caller" to "+12349013030",
                "Digits" to "1234",
                "From" to "+12349013030",
                "To" to "+18005551212",
                "Foo" to "Bar"
        )
        val twilioParams = TwilioParams(testMap)
        twilioParams.validationParams() shouldNot haveKey("Foo")
    }

    @Test
    fun `If any fields missing, payload is invalid`() {
        val testMap = mapOf(
                "CallSid" to "CA1234567890ABCDE",
                "Caller" to "+12349013030",
                "Digits" to "1234"
        )
        val twilioParams = TwilioParams(testMap)
        twilioParams.isValidPayload() shouldBe false
    }

    @Test
    fun `If all fields present, payload is valid`() {
        val testMap = mapOf(
                "CallSid" to "CA1234567890ABCDE",
                "Caller" to "+12349013030",
                "Digits" to "1234",
                "From" to "+12349013030",
                "To" to "+18005551212"
        )
        val twilioParams = TwilioParams(testMap)
        twilioParams.isValidPayload() shouldBe true
    }
}