package com.merricklabs.echobot.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SlackChallengeMessage : SlackMessage {
    val token: String
    val challenge: String

    constructor(type: String, token: String, challenge: String) : super(type){
        this.token = token
        this.challenge = challenge
    }
}