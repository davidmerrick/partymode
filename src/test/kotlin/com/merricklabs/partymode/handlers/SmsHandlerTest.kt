package com.merricklabs.partymode.handlers

import com.merricklabs.partymode.PartymodeConfig
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.testutil.TestConstants.INTEGRATION_GROUP
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class SmsHandlerTest : PartymodeIntegrationTestBase() {

    private val smsHandlerImpl by inject<SmsHandlerImpl>()

    @Test(groups = [INTEGRATION_GROUP])
    fun `Invalid phone number should return 400`() {
        val mockPhone = Mockito.mock(PartymodeConfig.Phone::class.java)
        Mockito.`when`(mockPhone.myNumber).thenReturn("+19999999999")
        Mockito.`when`(partymodeConfig.phone).thenReturn(mockPhone)
        val badNumber = "1123456789"
        val input = mapOf(
                "body" to "Body=foo&subject=bar&From=%2B$badNumber"
        )

        val response = smsHandlerImpl.handleRequest(input, null)
        assertEquals(response.statusCode, 400)
    }
}