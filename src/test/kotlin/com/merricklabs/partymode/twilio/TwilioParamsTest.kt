package com.merricklabs.partymode.twilio

import io.kotlintest.matchers.haveKey
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
}