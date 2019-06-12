package com.merricklabs.partymode.config

interface PartymodeConfig {
    val dynamoDb: DynamoDbConfig
    val sns: SnsConfig
    val phone: PhoneConfig
    val slack: SlackConfig
}