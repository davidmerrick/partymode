# partymode

Service that auto-buzzes people into my building when enabled, otherwise forwards to my phone. Uses [Twilio](https://www.twilio.com/). Written in Kotlin as an AWS Lambda function.

# Usage

Use [serverless](https://serverless.com/) to deploy the app with `./gradlew deploy`.

This will provision a DynamoDb table, 2 Lambda functions, and 2 API Gateway endpoints.

After deployment, log into the AWS console and set the `MY_NUMBER` env var in your Lambda to your phone number.

# Reference:
- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
