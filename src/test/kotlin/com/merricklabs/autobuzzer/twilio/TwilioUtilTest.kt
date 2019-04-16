package com.merricklabs.autobuzzer.twilio

import org.testng.Assert
import org.testng.annotations.Test

class TwilioUtilTest {

    @Test
    private fun `parse input`(){
        val twilioInput = TwilioUtil.parseInput("Body=foo&subject=bar&From=%2B11239999999")
        Assert.assertEquals(twilioInput.getValue("Body")[0], "foo")
        Assert.assertEquals(twilioInput.getValue("From")[0], "+11239999999")
    }
}