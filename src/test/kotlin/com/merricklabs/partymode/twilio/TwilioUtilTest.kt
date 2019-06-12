package com.merricklabs.partymode.twilio

import io.kotlintest.shouldBe
import org.testng.annotations.Test

class TwilioUtilTest {

    @Test
    private fun `parse input`(){
        val twilioInput = TwilioUtil.parseInput("Body=foo&subject=bar&From=%2B11239999999")
        twilioInput.getValue("Body")[0] shouldBe  "foo"
        twilioInput.getValue("From")[0] shouldBe  "+11239999999"
    }
}