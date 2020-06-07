# partymode 🎉

Buzzing people into your apartment building when you're having a party? 
Ain't nobody got time for that! That's why I built partymode. 
It's a service that auto-buzzes people into my building when enabled, 
otherwise forwards calls to my phone. It uses [Twilio](https://www.twilio.com/) to handle calls and SMS, 
backed by AWS Lambda functions written in Kotlin. 
It integrates with Slack so I or anyone in my workspace can enable it.

# Installation

## Prerequisites

* Get a number from [Twilio](https://www.twilio.com/), which costs $1/month.
* Get an AWS account. 
* Get admin access to your Slack workspace.

## Slack setup

* Create a Slack webhook and set the `SLACK_WEBHOOK_URI` env var to that uri.
* Create a Slack bot and subscribe to the following bot events: `app_mention`, `message.channels`, and `message.im`.

## Initial deployment

Set the `MY_NUMBER` env var to your phone number. Make sure the format is `+19999999999`. 

This will get passed to your Lambda functions and used by Twilio to decide where to forward the call when partymode is disabled.

Set the `CALLBOX_NUMBER` env var to your call box's phone number. 
Make sure the format is `+19999999999`.
The app will reject calls from any other number, in the event you inevitably get spammed with robocalls.

## Twilio setup

In your Twilio dashboard, create a TwiML app and point the endpoints to the ones provisioned in AWS. Point your Twilio number to this app.

## Final steps

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Kotlin Gradle DSL

This project uses the [Kotlin Gradle DSL](https://github.com/gradle/kotlin-dsl).
To keep the `build.gradle.kts` clean, it uses [buildSrcVersions](https://github.com/jmfayard/buildSrcVersions).

After adding a new dependency, run this plugin with: `./gradlew buildSrcVersions`.

# Reference:

- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
- [Validating incoming requests from Twilio](https://www.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests)
