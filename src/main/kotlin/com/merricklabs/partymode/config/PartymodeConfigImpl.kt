package com.merricklabs.partymode.config

import org.koin.core.KoinComponent

class PartymodeConfigImpl : KoinComponent, PartymodeConfig {

    override val dynamoDb = DynamoDbConfig()
    override val sns = SnsConfig()
    override val phone = PhoneConfig()
    override val slack = SlackConfig()
    override val twilio = TwilioConfig()
}