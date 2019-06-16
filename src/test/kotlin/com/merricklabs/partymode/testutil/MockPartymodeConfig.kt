package com.merricklabs.partymode.testutil

import com.merricklabs.partymode.config.DynamoDbConfig
import com.merricklabs.partymode.config.PartymodeConfig
import com.merricklabs.partymode.config.PhoneConfig
import com.merricklabs.partymode.config.SlackConfig
import com.merricklabs.partymode.config.SnsConfig
import com.merricklabs.partymode.config.TwilioConfig
import org.mockito.Mockito

class MockPartymodeConfig : PartymodeConfig {

    val mockPhone = Mockito.mock(PhoneConfig::class.java)
    val mockDynamo = Mockito.mock(DynamoDbConfig::class.java)
    val mockSlack = Mockito.mock(SlackConfig::class.java)
    val mockSns = Mockito.mock(SnsConfig::class.java)
    val mockTwilio = Mockito.mock(TwilioConfig::class.java)

    override val dynamoDb: DynamoDbConfig
        get() = mockDynamo
    override val sns: SnsConfig
        get() = mockSns
    override val phone: PhoneConfig
        get() = mockPhone
    override val slack: SlackConfig
        get() = mockSlack
    override val twilio: TwilioConfig
        get() = mockTwilio


}