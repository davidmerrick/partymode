# partymode 🎉

Service that auto-buzzes people into my building when enabled, otherwise forwards to my phone. Uses [Twilio](https://www.twilio.com/). Written in Kotlin as an AWS Lambda function.

# Usage

First, get a number from [Twilio](https://www.twilio.com/), which costs $1/month.

Use [serverless](https://serverless.com/) to deploy the app with `./gradlew deploy`.

This will provision a DynamoDb table, 2 Lambda functions, and 2 API Gateway endpoints.

After deployment, log into the AWS console and set the `MY_NUMBER` env var in your Lambda to your phone number.

Create a TwiML app and point the endpoints to the ones provisioned in AWS. Point your Twilio number to this app.

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Reference:
- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
