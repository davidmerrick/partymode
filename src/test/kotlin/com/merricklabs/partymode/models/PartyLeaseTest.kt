package com.merricklabs.partymode.models

import com.merricklabs.partymode.testutil.TestConstants.INTEGRATION_GROUP
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.time.Instant

class PartyLeaseTest {
    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should not buzz after timeout is reached`() {
        // Pick a time that's at least an hour before now
        val partyLease = PartyLease("2019-04-01T10:00:00.00Z", 1)
        assertFalse(partyLease.isActive())
    }

    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should buzz if within timeout window`() {
        val partyLease = PartyLease(Instant.now().toString(), 1)
        assertTrue(partyLease.isActive())
    }
}