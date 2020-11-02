package io.github.davidmerrick.partymode.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.bots.helpText
import io.github.davidmerrick.partymode.external.slack.MessageFilter
import io.github.davidmerrick.partymode.storage.PartymodeStorage
import io.github.davidmerrick.slakson.client.SlackClient
import io.github.davidmerrick.slakson.messages.CreateMessagePayload
import io.github.davidmerrick.slakson.messages.SlackMessage
import io.kotlintest.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import javax.inject.Inject

private const val EVENTS_ENDPOINT = "/slack/events"

@MicronautTest(application = TestApplication::class)
class SlackControllerTest {

    @get:MockBean(PartymodeStorage::class)
    val storage = mockk<PartymodeStorage>()

    @get:MockBean(SlackClient::class)
    val slackClient = mockk<SlackClient>()

    @Inject
    lateinit var messageFilter: MessageFilter

    @Inject
    lateinit var mapper: ObjectMapper

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @BeforeEach
    private fun beforeMethod() {
        clearMocks(slackClient, storage)
    }

    @Test
    fun `Handle Slack challenge`() {
        val challenge = "foo"
        val payload = mapOf(
                "challenge" to challenge,
                "token" to "banana",
                "type" to "url_verification"
        )
        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val response = client
                .toBlocking()
                .retrieve(request)

        response.contains(challenge) shouldBe true
    }

    @Test
    fun `Filter out bot messages`() {
        val payload = mapOf(
                "type" to "event_callback",
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event_id" to "foo",
                "event_time" to 12345,
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "how long",
                        "channel" to "banana",
                        "channel_type" to "channel",
                        "subtype" to "bot_message"
                )
        )

        val messageString = mapper.writeValueAsString(payload)
        val message = mapper.readValue<SlackMessage>(messageString)
        messageFilter.apply(message) shouldBe false
    }

    @Test
    fun `On message containing "pm 1", should enable partymode for 1 hour`() {
        enablePartymode()
        val payload = mapOf(
                "type" to "event_callback",
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event_id" to "foo",
                "event_time" to 12345,
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "pm 1",
                        "channel" to "banana",
                        "channel_type" to "im"
                )
        )

        val slot = slot<CreateMessagePayload>()
        every {
            slackClient.postMessage(capture(slot))
        } just Runs

        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val status = client
                .toBlocking()
                .retrieve(request, HttpStatus::class.java)

        status shouldBe HttpStatus.OK
        slot.captured.text.contains("partymode enabled for 1 hour", true) shouldBe true
    }

    // Todo: Capture slot isn't initialized when multiple tests are running with capture slots
    // Find out why
    @Test
    @Disabled
    fun `On message containing "help", should display help text`() {
        enablePartymode()
        val payload = mapOf(
                "type" to "event_callback",
                "token" to "banana",
                "team_id" to "foo",
                "api_app_id" to "foo",
                "event_id" to "foo",
                "event_time" to 12345,
                "event" to mapOf(
                        "type" to "message",
                        "user" to "foo",
                        "text" to "pm help",
                        "channel" to "banana",
                        "channel_type" to "im"
                )
        )

        val slot = slot<CreateMessagePayload>()
        every {
            slackClient.postMessage(capture(slot))
        } just Runs

        val request = HttpRequest.POST(
                EVENTS_ENDPOINT,
                payload
        )

        val status = client
                .toBlocking()
                .retrieve(request, HttpStatus::class.java)

        status shouldBe HttpStatus.OK
        slot.captured.text.contains(helpText, true) shouldBe true
    }

    private fun enablePartymode() = setPartymode(true)
    private fun setPartymode(value: Boolean) {
        every {
            storage.isPartymodeEnabled()
        } returns value

        every {
            storage.enable(any())
        } just runs
    }
}
