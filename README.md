# partymode 🎉

Buzzing people into your apartment building when you're having a party? Ain't nobody got time for that! That's why I built partymode. It's a service that auto-buzzes people into my building when enabled, otherwise forwards calls to my phone. It uses [Twilio](https://www.twilio.com/) to handle calls and SMS, backed by AWS Lambda functions written in Kotlin and deployed with the [Serverless](https://serverless.com/) Framework. It integrates with Slack so I or anyone in my workspace can control when it's enabled.

# Installation

1. Get a number from [Twilio](https://www.twilio.com/), which costs $1/month.
2. Get an AWS account. 
3. Use [Serverless](https://serverless.com/) to deploy the app with `./gradlew deploy`.
4. This will provision a DynamoDb table, 2 Lambda functions, and 2 API Gateway endpoints.
5. After deployment, log into the AWS console and set the `MY_NUMBER` env var in your Lambda to your phone number.
6. Create a Slack webhook and set the `SLACK_WEBHOOK_URI` env var to that uri.
7. Create a TwiML app and point the endpoints to the ones provisioned in AWS. Point your Twilio number to this app.
8. Create a Slack bot and subscribe to the following bot events: `app_mention`, `message.channels`, and `message.im`.

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Reference:
- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
