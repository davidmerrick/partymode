package com.merricklabs.partymode

import com.merricklabs.partymode.config.DynamoDbConfig
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.config.PhoneConfig
import com.merricklabs.partymode.config.SlackConfig
import com.merricklabs.partymode.config.SnsConfig
import com.merricklabs.partymode.config.TwilioConfig
import com.merricklabs.partymode.storage.PartymodeStorage
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass

@Suppress("UNCHECKED_CAST")
open class PartymodeIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeClass
    protected fun beforeMethod() {
        val mockPhone = Mockito.mock(PhoneConfig::class.java)
        val mockDynamo = Mockito.mock(DynamoDbConfig::class.java)
        val mockSlack = Mockito.mock(SlackConfig::class.java)
        val mockSns = Mockito.mock(SnsConfig::class.java)
        val mockTwilio = Mockito.mock(TwilioConfig::class.java)

        Mockito.`when`(mockPhone.myNumber).thenReturn(MY_NUMBER)
        Mockito.`when`(mockPhone.callboxNumber).thenReturn(CALLBOX_NUMBER)
        Mockito.`when`(mockTwilio.authToken).thenReturn(TWILIO_AUTH_TOKEN)

        startKoin {
            modules(PartymodeModule)
        }
        declareMock<PartymodeStorage>()
        declareMock<PartymodeConfig> {
            given(this.phone).willReturn(mockPhone)
            given(this.twilio).willReturn(mockTwilio)
            given(this.dynamoDb).willReturn(mockDynamo)
            given(this.slack).willReturn(mockSlack)
            given(this.sns).willReturn(mockSns)
        }
    }

    @AfterClass
    protected fun afterMethod() {
        stopKoin()
    }

    companion object {
        const val MY_NUMBER = "8675309"
        const val CALLBOX_NUMBER = "8675309"
        const val TWILIO_AUTH_TOKEN = "12345"
    }

    protected fun enablePartyMode(enabled: Boolean) {
        val partymodeStorage by inject<PartymodeStorage>()
        Mockito.`when`(partymodeStorage.isPartymodeEnabled()).thenReturn(enabled)
    }
}