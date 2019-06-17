package com.merricklabs.partymode.handlers

import com.amazonaws.services.lambda.runtime.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.slack.SlackChallengeMessage
import com.merricklabs.partymode.slack.SlackMessageType.URL_VERIFICATION
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.Test

class SlackMessageHandlerLogicTest : PartymodeIntegrationTestBase() {

    private val mockContext = Mockito.mock(Context::class.java)
    private val slackMessageHandlerLogic: SlackMessageHandlerLogic by inject()

    @Test
    fun `Should return challenge on challenge request`() {
        val challengeRequest = mapOf(
                "body" to SlackChallengeMessage(URL_VERIFICATION, "foo", "bar")
        )
        val response = slackMessageHandlerLogic.handleRequest(challengeRequest, mockContext)
        response.statusCode shouldBe 200
        response.body shouldHave contain("bar")
    }
}