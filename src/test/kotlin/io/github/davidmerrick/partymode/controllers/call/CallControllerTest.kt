package io.github.davidmerrick.partymode.controllers.call

import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.config.PartymodeConfig.PhoneConfig
import io.github.davidmerrick.partymode.config.TwilioConfig
import io.github.davidmerrick.partymode.external.twilio.PartymodeCallRequestValidator
import io.github.davidmerrick.partymode.external.twilio.TwilioHeaders.TWILIO_SIGNATURE
import io.github.davidmerrick.partymode.storage.PartymodeStorage
import io.kotlintest.shouldBe
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.net.URLEncoder
import javax.inject.Inject

private const val CALL_ENDPOINT = "/call"

@MicronautTest(application = TestApplication::class)
class CallControllerTest {
    @get:MockBean(TwilioConfig::class)
    val twilioConfig = mockk<TwilioConfig>()

    init {
        every { twilioConfig.authToken } returns "foo"
    }

    @get:MockBean(PartymodeStorage::class)
    val storage = mockk<PartymodeStorage>()

    @Inject
    lateinit var phoneConfig: PhoneConfig

    @get:MockBean(PartymodeCallRequestValidator::class)
    val twilioValidator = mockk<PartymodeCallRequestValidator>()

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `If request doesn't have header, reject it`() {
        val request = HttpRequest
                .POST(CALL_ENDPOINT, "foo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)

        try {
            client.toBlocking()
                    .retrieve(request, HttpStatus::class.java)
        } catch (e: Exception) {
            (e as HttpClientResponseException).status shouldBe HttpStatus.BAD_REQUEST
        }
    }

    @Test
    fun `If request validation fails, reject it`() {
        every {
            twilioValidator.validate(any(), any<Map<String, String>>(), any())
        } returns false

        val request = HttpRequest
                .POST(CALL_ENDPOINT, "foo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)

        try {
            client.toBlocking()
                    .retrieve(request, HttpStatus::class.java)
        } catch (e: Exception) {
            (e as HttpClientResponseException).status shouldBe HttpStatus.BAD_REQUEST
        }
    }

    @Test
    fun `If partymode is enabled, play digits`() {
        enablePartymode()
        every {
            twilioValidator.validate(any(), any<Map<String, String>>(), any())
        } returns true

        val body = "From=${URLEncoder.encode(phoneConfig.callboxNumber, "UTF-8")}" +
                "&To=12345" +
                "&CallSid=12345" +
                "&Caller=12345" +
                "&Digits=12345"
        val request = HttpRequest
                .POST(CALL_ENDPOINT, body)
                .header(TWILIO_SIGNATURE, "12345")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)

        val response = client.toBlocking()
                .retrieve(request)

        response.contains("<Play digits=\"ww999\"/>") shouldBe true
    }

    @Test
    fun `If partymode is disabled, forward call to phone`() {
        disablePartymode()
        every {
            twilioValidator.validate(any(), any<Map<String, String>>(), any())
        } returns true

        val body = "From=${URLEncoder.encode(phoneConfig.callboxNumber, "UTF-8")}" +
                "&To=12345" +
                "&CallSid=12345" +
                "&Caller=12345" +
                "&Digits=12345"
        val request = HttpRequest
                .POST(CALL_ENDPOINT, body)
                .header(TWILIO_SIGNATURE, "12345")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)

        val response = client.toBlocking()
                .retrieve(request)

        response.contains("<Dial><Number>${phoneConfig.myNumber}</Number></Dial>") shouldBe true
    }

    @Test
    fun `If call is not from my number or callbox, reject it`() {
        every {
            twilioValidator.validate(any(), any<Map<String, String>>(), any())
        } returns true

        val invalidNumber = "+19999999999"
        val body = "From=$invalidNumber" +
                "&To=12345" +
                "&CallSid=12345" +
                "&Caller=12345" +
                "&Digits=12345"
        val request = HttpRequest
                .POST(CALL_ENDPOINT, body)
                .header(TWILIO_SIGNATURE, "12345")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)

        val response = client.toBlocking()
                .retrieve(request)

        response.contains("<Reject/>") shouldBe true
    }

    private fun enablePartymode() = setPartymode(true)
    private fun disablePartymode() = setPartymode(false)
    private fun setPartymode(value: Boolean) = every {
        storage.isPartymodeEnabled()
    } returns value
}
