package com.merricklabs.partymode.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.merricklabs.partymode.slack.SlackMessageType.EVENT_CALLBACK

@JsonIgnoreProperties(ignoreUnknown = true)
class SlackCallbackMessage : SlackMessage {
    val token: String
    val event: EventPayload

    constructor(type: String = EVENT_CALLBACK, token: String, event: EventPayload) : super(type) {
        this.token = token
        this.event = event
    }
}