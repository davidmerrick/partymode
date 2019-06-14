package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.models.PartyLease
import com.merricklabs.partymode.testutil.MockPartymodeConfig.Companion.CALLBOX_NUMBER
import com.merricklabs.partymode.testutil.MockPartymodeConfig.Companion.MY_NUMBER
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldHave
import io.kotlintest.shouldNotHave
import org.koin.standalone.inject
import org.mockito.Mockito
import org.testng.annotations.Test
import java.net.URLEncoder

class CallHandlerLogicTest: PartymodeIntegrationTestBase() {

    private val callHandlerLogic: CallHandlerLogic by inject()
    private val mockInput = mapOf("body" to "From=${URLEncoder.encode(CALLBOX_NUMBER, "UTF-8")}")
    private val mockContext = Mockito.mock(Context::class.java)

    @Test
    fun `If partymode is enabled, should play digits`(){
        initMockLease(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("play digits")
    }

    @Test
    fun `If partymode is disabled, should forward to phone`(){
        initMockLease(false)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain(MY_NUMBER)
    }

    @Test
    fun `If call is from callbox number, allow it`(){
        initMockLease(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldNotHave contain("reject")
    }

    @Test
    fun `If call is not from callbox number, reject it`(){
        initMockLease(true)
        val invalidNumber = "9999999999"
        val mockInput = mapOf("body" to "From=${URLEncoder.encode(invalidNumber, "UTF-8")}")
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("reject")
    }

    private fun initMockLease(isActive: Boolean){
        val mockLease = Mockito.mock(PartyLease::class.java)
        Mockito.`when`(mockLease.isActive()).thenReturn(isActive)
        Mockito.`when`(partymodeStorage.getLatestItem()).thenReturn(mockLease)
    }
}