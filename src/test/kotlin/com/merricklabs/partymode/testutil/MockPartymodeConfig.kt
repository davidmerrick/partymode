package com.merricklabs.partymode.testutil

import com.merricklabs.partymode.config.DynamoDbConfig
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.config.PhoneConfig
import com.merricklabs.partymode.config.SlackConfig
import com.merricklabs.partymode.config.SnsConfig
import org.mockito.Mockito

class MockPartymodeConfig : PartymodeConfig {

    private val mockPhone = Mockito.mock(PhoneConfig::class.java)
    private val mockSns = Mockito.mock(SnsConfig::class.java)
    private val mockDynamo = Mockito.mock(DynamoDbConfig::class.java)
    private val mockSlack = Mockito.mock(SlackConfig::class.java)

    init {
        Mockito.`when`(mockPhone.myNumber).thenReturn(MY_NUMBER)
    }

    override val dynamoDb: DynamoDbConfig
        get() = mockDynamo
    override val sns: SnsConfig
        get() = mockSns
    override val phone: PhoneConfig
        get() = mockPhone
    override val slack: SlackConfig
        get() = mockSlack

    companion object {
        const val MY_NUMBER = "8675309"
    }
}