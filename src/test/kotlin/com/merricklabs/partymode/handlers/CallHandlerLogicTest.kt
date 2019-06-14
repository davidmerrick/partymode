package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.models.PartyLease
import com.merricklabs.partymode.testutil.MockPartymodeConfig
import com.merricklabs.partymode.testutil.MockPartymodeConfig.Companion.CALLBOX_NUMBER
import com.merricklabs.partymode.testutil.MockPartymodeConfig.Companion.MY_NUMBER
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldHave
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class CallHandlerLogicTest: PartymodeIntegrationTestBase() {

    private val callHandlerLogic: CallHandlerLogic by inject()
    private val mockInput = mapOf("body" to mapOf("From" to CALLBOX_NUMBER))
    private val mockContext = Mockito.mock(Context::class.java)

    @Test
    fun `If partymode is enabled, should play digits`(){
        val mockLease = Mockito.mock(PartyLease::class.java)
        Mockito.`when`(mockLease.isActive()).thenReturn(true)
        Mockito.`when`(partymodeStorage.getLatestItem()).thenReturn(mockLease)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("play digits")
    }

    @Test
    fun `If partymode is disabled, should forward to phone`(){
        val mockLease = Mockito.mock(PartyLease::class.java)
        Mockito.`when`(mockLease.isActive()).thenReturn(false)
        Mockito.`when`(partymodeStorage.getLatestItem()).thenReturn(mockLease)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain(MY_NUMBER)
    }

    @Test
    fun `If call is from callbox number, allow it`(){
        val testPayload = mapOf(
                "From" to CALLBOX_NUMBER
        )
    }

    @Test
    fun `If call is not from callbox number, reject it`(){
        // Todo: Implement this
    }
}