package com.merricklabs.partymode

import org.koin.standalone.KoinComponent

class PartymodeConfig : KoinComponent {

    val dynamoDb = DynamoDb()
    val phone = Phone()
    val slack = Slack()

    inner class DynamoDb {
        val endpoint: String = System.getenv("DYNAMODB_ENDPOINT") ?: "https://dynamodb.us-west-2.amazonaws.com"
        val region: String = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
        val tableName: String = System.getenv("DYNAMODB_TABLE_NAME")
    }

    inner class Phone {
        val myNumber: String = System.getenv("MY_NUMBER")
    }

    inner class Slack {
        val webhookUri: String = System.getenv("SLACK_WEBHOOK_URI")
        val botToken: String = System.getenv("BOT_TOKEN")
        val botUserName: String = System.getenv("BOT_USER_NAME")
    }
}