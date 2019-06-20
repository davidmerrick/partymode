package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent.ProxyRequestContext
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.twilio.TwilioHeaders.TWILIO_SIGNATURE
import com.merricklabs.partymode.twilio.TwilioHelpers
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.shouldNotHave
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.net.URLEncoder

class CallHandlerLogicTest : PartymodeIntegrationTestBase() {

    private val callHandlerLogic: CallHandlerLogic by inject()

    private val mockInput = APIGatewayProxyRequestEvent().apply {
        headers = mapOf(TWILIO_SIGNATURE to "12345", "HOST" to "foo")
        requestContext = ProxyRequestContext()
                .withPath("/bar/baz")
        body = "From=${URLEncoder.encode(CALLBOX_NUMBER, "UTF-8")}" +
                "&To=12345" +
                "&CallSid=12345" +
                "&Caller=12345" +
                "&Digits=12345"
    }

    private val mockContext = mock(Context::class.java)

    @BeforeMethod
    fun `before method`() {
        declareMock<TwilioHelpers> {
            given(this.validateRequest(any(), any(), any())).willReturn(true)
        }
    }

    @Test
    fun `If partymode is enabled, should play digits`() {
        enablePartyMode(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("play digits")
    }

    @Test
    fun `If partymode is disabled, should forward to phone`() {
        enablePartyMode(false)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain(MY_NUMBER)
    }

    @Test
    fun `If call is from callbox number, allow it`() {
        enablePartyMode(true)
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldNotHave contain("reject")
    }

    @Test
    fun `If call is not from callbox number, reject it`() {
        enablePartyMode(true)
        val invalidNumber = "9999999999"
        val mockInput = APIGatewayProxyRequestEvent().apply {
            headers = mapOf(TWILIO_SIGNATURE to "12345", "HOST" to "foo")
            requestContext = ProxyRequestContext()
                    .withPath("/bar/baz")
            body = "From=${URLEncoder.encode(invalidNumber, "UTF-8")}" +
                    "&To=12345" +
                    "&CallSid=12345" +
                    "&Caller=12345" +
                    "&Digits=12345"
        }
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.body!!.toLowerCase() shouldHave contain("reject")
    }

    @Test
    fun `If request doesn't have header, reject it`() {
        enablePartyMode(true)
        val mockInput = APIGatewayProxyRequestEvent().apply {
            headers = emptyMap()
            requestContext = ProxyRequestContext()
                    .withPath("/bar/baz")
        }
        val response = callHandlerLogic.handleRequest(mockInput, mockContext)
        response.statusCode shouldBe HttpStatus.SC_BAD_REQUEST
    }
}