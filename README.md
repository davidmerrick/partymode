# partymode 🎉

Buzzing people into your apartment building when you're having a party? Ain't nobody got time for that! That's why I built partymode. It's a service that auto-buzzes people into my building when enabled, otherwise forwards calls to my phone. It uses [Twilio](https://www.twilio.com/) to handle calls and SMS, backed by AWS Lambda functions written in Kotlin and deployed with the [Serverless](https://serverless.com/) Framework. It integrates with Slack so I or anyone in my workspace can control when it's enabled.

# Installation

## Prerequisites

* Get a number from [Twilio](https://www.twilio.com/), which costs $1/month.
* Get an AWS account. 
* Install [Serverless](https://serverless.com/).
* Get admin access to your Slack workspace.

## Slack setup

* Create a Slack webhook and set the `SLACK_WEBHOOK_URI` env var to that uri.
* Create a Slack bot and subscribe to the following bot events: `app_mention`, `message.channels`, and `message.im`.

## Initial deployment

Set the `MY_NUMBER` env var to your phone number. This will get passed to your Lambda functions and used by Twilio
to decide where to forward the call when partymode is disabled.

In `serverless.yaml`, change `async` to `false` in the Slack handler:
```yaml
slack-handler:
    handler: com.merricklabs.partymode.handlers.SlackMessageHandler
    events:
      - http:
          path: slack/event
          method: post
          async: false 
```
Synchronous is required at first so that Slack can verify your url.

Deploy the app with `./gradlew deployDev`.

This will give you urls for Slack and Twilio.

In Slack, point your bot at the Slack url and verify it.

Change `async` back to `true` in your `serverless.yaml` and redeploy with `./gradlew deployDev`.

## Twilio setup

In your Twilio dashboard, create a TwiML app and point the endpoints to the ones provisioned in AWS. Point your Twilio number to this app.

## Optional: CircleCI Pipeline

This repo contains config for deploying to prod via CircleCI. Simply set the following environment variables in your pipeline:

* `AWS_ACCESS_KEY_ID`
* `AWS_SECRET_ACCESS_KEY`
* `BOT_TOKEN`
* `MY_NUMBER`
* `SLACK_WEBHOOK_URI`
* `SNS_TOPIC`

## Final steps

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Kotlin Gradle DSL

This project uses the [Kotlin Gradle DSL](https://github.com/gradle/kotlin-dsl).
To keep the `build.gradle.kts` clean, it uses [buildSrcVersions](https://github.com/jmfayard/buildSrcVersions).

After adding a new dependency, run this plugin with: `./gradlew buildSrcVersions`.

# Reference:

- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
