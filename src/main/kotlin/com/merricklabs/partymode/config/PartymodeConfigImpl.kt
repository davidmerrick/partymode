package com.merricklabs.partymode.config

import org.koin.standalone.KoinComponent

class PartymodeConfigImpl : KoinComponent, PartymodeConfig {

    override val dynamoDb = DynamoDbConfig()
    override val sns = SnsConfig()
    override val phone = PhoneConfig()
    override val slack = SlackConfig()
}