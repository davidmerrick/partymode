package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.slack.SlackChallengeMessage
import com.merricklabs.partymode.slack.SlackHeaders.SLACK_SIGNATURE
import com.merricklabs.partymode.slack.SlackMessageType.URL_VERIFICATION
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class SlackMessageHandlerLogicTest : PartymodeIntegrationTestBase() {

    private val mockContext = Mockito.mock(Context::class.java)
    private val slackMessageHandlerLogic: SlackMessageHandlerLogic by inject()

    @Test
    fun `Should return challenge on challenge request`() {
        val challengeRequest = mapOf(
                "headers" to mapOf(SLACK_SIGNATURE to "foo"),
                "body" to SlackChallengeMessage(URL_VERIFICATION, "foo", "bar")
        )
        val response = slackMessageHandlerLogic.handleRequest(challengeRequest, mockContext)
        response.statusCode shouldBe HttpStatus.SC_OK
        response.body shouldHave contain("bar")
    }

    @Test
    fun `Should return bad request if signature header missing`() {
        val challengeRequest = mapOf(
                "headers" to emptyMap<String, String>(),
                "body" to SlackChallengeMessage(URL_VERIFICATION, "foo", "bar")
        )
        val response = slackMessageHandlerLogic.handleRequest(challengeRequest, mockContext)
        response.statusCode shouldBe HttpStatus.SC_BAD_REQUEST
    }
}