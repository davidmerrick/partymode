package com.merricklabs.partymode.config

import org.koin.core.KoinComponent

class PartymodeConfig : KoinComponent {

    val dynamoDb = DynamoDbConfig()
    val sns = SnsConfig()
    val phone = PhoneConfig()
    val slack = SlackConfig()
    val twilio = TwilioConfig()
}