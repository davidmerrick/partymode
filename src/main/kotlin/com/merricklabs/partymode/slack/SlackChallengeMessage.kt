package com.merricklabs.echobot.slack

class SlackChallengeMessage : SlackMessage {
    val token: String
    val challenge: String

    constructor(type: String, token: String, challenge: String) : super(type){
        this.token = token
        this.challenge = challenge
    }
}