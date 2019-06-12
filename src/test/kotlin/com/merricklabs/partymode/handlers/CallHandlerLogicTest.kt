package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.models.PartyLease
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldHave
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class CallHandlerLogicTest: PartymodeIntegrationTestBase() {

    private val callHandlerLogic: CallHandlerLogic by inject()
    private val mockInput = mapOf("foo" to "bar")
    private val mockContext = Mockito.mock(Context::class.java)

    @Test
    fun `If mode is enabled, should play digits`(){
        val mockLease = Mockito.mock(PartyLease::class.java)
        Mockito.`when`(mockLease.isActive()).thenReturn(true)
        Mockito.`when`(partymodeStorage.getLatestItem()).thenReturn(mockLease)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("play digits")
    }
}