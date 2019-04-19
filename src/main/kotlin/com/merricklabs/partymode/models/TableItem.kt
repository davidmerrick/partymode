package com.merricklabs.partymode.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "partymode")
data class TableItem(
        @DynamoDBHashKey(attributeName = "start_time") val startTime: String,
        @DynamoDBHashKey(attributeName = "timeout") val timeout: Int
)