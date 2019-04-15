package com.merricklabs.autobuzzer

import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber

fun main(args:Array<String>){
    Twilio.init(
            System.getenv("TWILIO_ACCOUNT_SID"),
            System.getenv("TWILIO_AUTH_TOKEN")
    )

    val message = Message.creator(
            PhoneNumber(System.getenv("YOUR_PHONE_NUMBER")),
            PhoneNumber(System.getenv("TWILIO_PHONE_NUMBER")),
            "Ahoy from Twilio"
    ).create()

    println(message.sid)
}