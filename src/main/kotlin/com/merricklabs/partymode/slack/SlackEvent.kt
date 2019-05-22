package com.merricklabs.echobot.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventPayload(
        val client_msg_id: String,
        val type: String,
        val text: String,
        val user: String,
        val ts: String,
        val channel: String,
        val event_ts: String,
        val channel_type: String?
){
    fun isAtMention() = type == "app_mention"
}