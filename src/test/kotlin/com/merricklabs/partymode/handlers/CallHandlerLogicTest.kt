package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.models.PartyLease
import com.merricklabs.partymode.storage.PartymodeStorage
import com.merricklabs.partymode.twilio.TwilioHeaders.X_TWILIO_SIGNATURE
import com.merricklabs.partymode.twilio.TwilioHelpers
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldHave
import io.kotlintest.shouldNotHave
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.net.URLEncoder

class CallHandlerLogicTest : PartymodeIntegrationTestBase() {

    private val partymodeStorage: PartymodeStorage by inject()
    private val callHandlerLogic: CallHandlerLogic by inject()

    private val mockInput = mapOf(
            "headers" to mapOf(X_TWILIO_SIGNATURE to "12345"),
            "body" to "From=${URLEncoder.encode(CALLBOX_NUMBER, "UTF-8")}" +
                    "&To=12345" +
                    "&CallSid=12345" +
                    "&Caller=12345" +
                    "&Digits=12345"
    )
    private val mockContext = mock(Context::class.java)

    @BeforeMethod
    fun `before method`() {
        declareMock<TwilioHelpers> {
            given(this.validateRequest(any(), any(), any())).willReturn(true)
        }
    }

    @Test
    fun `If partymode is enabled, should play digits`() {
        initMockLease(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("play digits")
    }

    @Test
    fun `If partymode is disabled, should forward to phone`() {
        initMockLease(false)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain(MY_NUMBER)
    }

    @Test
    fun `If call is from callbox number, allow it`() {
        initMockLease(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldNotHave contain("reject")
    }

    @Test
    fun `If call is not from callbox number, reject it`() {
        initMockLease(true)
        val invalidNumber = "9999999999"
        val mockInput = mapOf(
                "headers" to mapOf(X_TWILIO_SIGNATURE to "12345"),
                "body" to "From=${URLEncoder.encode(invalidNumber, "UTF-8")}" +
                        "&To=12345" +
                        "&CallSid=12345" +
                        "&Caller=12345" +
                        "&Digits=12345"
        )
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("reject")
    }

    private fun initMockLease(isActive: Boolean) {
        val mockLease = mock(PartyLease::class.java)
        `when`(mockLease.isActive()).thenReturn(isActive)
        `when`(partymodeStorage.getLatestItem()).thenReturn(mockLease)
    }
}