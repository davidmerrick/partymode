package com.merricklabs.partymode.storage

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.merricklabs.partymode.PartymodeConfig
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.time.Instant

private val log = KotlinLogging.logger {}

class PartymodeStorage : KoinComponent {
    private val config by inject<PartymodeConfig>()

    private val dynamoDbConfig: PartymodeConfig.DynamoDb
    private val table: Table

    init {
        this.dynamoDbConfig = config.dynamoDb

        val dynamoDbConfig = config.dynamoDb
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        AwsClientBuilder.EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region)
                )
                .build()

        val dynamoDB = DynamoDB(client)
        this.table = dynamoDB.getTable(dynamoDbConfig.tableName)
    }

    fun saveTimeToDb(numHours: Int) {
        log.info("Saving $numHours to db")
        val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "https://dynamodb.us-west-2.amazonaws.com"
        val region = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build()
        client.putItem(System.getenv("DYNAMODB_TABLE_NAME"),
                mapOf(
                        "date" to AttributeValue(Instant.now().toString()),
                        "timeout" to AttributeValue(numHours.toString())
                )
        )
    }
}