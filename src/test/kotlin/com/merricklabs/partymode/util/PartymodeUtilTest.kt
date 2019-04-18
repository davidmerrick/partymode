package com.merricklabs.partymode.util

import com.merricklabs.partymode.testutil.TestConstants.INTEGRATION_GROUP
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.time.Instant


class PartymodeUtilTest {

    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should not buzz after timeout is reached`() {
        // Pick a time that's at least an hour before now
        val savedTime = Instant.parse("2019-04-01T10:00:00.00Z")
        val timeout = 1
        assertFalse(PartymodeUtil.shouldBuzz(savedTime, timeout))
    }

    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should buzz if within timeout window`() {
        val savedTime = Instant.now()
        val timeout = 1
        assertTrue(PartymodeUtil.shouldBuzz(savedTime, timeout))
    }
}