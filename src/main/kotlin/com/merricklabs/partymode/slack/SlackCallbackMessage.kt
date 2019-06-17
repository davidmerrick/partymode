package com.merricklabs.partymode.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SlackCallbackMessage : SlackMessage {
    val token: String
    val event: EventPayload

    constructor(type: String, token: String, event: EventPayload) : super(type){
        this.token = token
        this.event = event
    }
}