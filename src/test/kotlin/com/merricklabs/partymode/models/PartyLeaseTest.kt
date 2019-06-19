package com.merricklabs.partymode.models

import com.merricklabs.partymode.testutil.TestConstants.INTEGRATION_GROUP
import io.kotlintest.shouldBe
import org.testng.annotations.Test
import java.time.Instant

class PartyLeaseTest {
    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should not buzz after timeout is reached`() {
        // Pick a time that's at least an hour before now
        val partyLease = PartyLease("2019-04-01T10:00:00.00Z", 1)
        partyLease.isActive() shouldBe false
    }

    @Test(groups = [INTEGRATION_GROUP])
    private fun `Should buzz if within timeout window`() {
        val partyLease = PartyLease(Instant.now().toString(), 1)
        partyLease.isActive() shouldBe true
    }

    @Test(groups = [INTEGRATION_GROUP])
    private fun `Default lease should be inactive`() {
        val partyLease = PartyLease.default()
        partyLease.isActive() shouldBe false
    }

    @Test(groups = [INTEGRATION_GROUP])
    private fun `A 0 timeout means inactive`() {
        val partyLease = PartyLease(Instant.now().toString(), 0)
        partyLease.isActive() shouldBe false
    }
}