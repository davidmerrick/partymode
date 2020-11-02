package io.github.davidmerrick.partymode.external

import com.fasterxml.jackson.module.kotlin.readValue
import io.github.davidmerrick.partymode.TestApplication
import io.github.davidmerrick.partymode.external.google.GoogleEvent
import io.github.davidmerrick.partymode.external.google.GoogleObjectMapper
import io.kotlintest.shouldBe
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Test
import java.io.File
import javax.inject.Inject

@MicronautTest(application = TestApplication::class)
class GoogleObjectMapperTest {
    @Inject
    lateinit var mapper: GoogleObjectMapper

    @Test
    fun `Parse Google payload`() {
        val payload = File("src/test/resources/enable-request.json").readText()
        val event = mapper.readValue<GoogleEvent>(payload)
        event.session.id shouldBe "ABwppHH-EcG7m3S1zpeZ_NM8rhpanhh5h_3EGFH73nu-WTO8_fv493R5BhUCzxy32WO4JIRG-poc"
    }
}
