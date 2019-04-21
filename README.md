# partymode 🎉

Buzzing people into your apartment building when you're having a party? Ain't nobody got time for that! That's why I built partymode. It's a service that auto-buzzes people into my building when enabled, otherwise forwards calls to my phone. It uses [Twilio](https://www.twilio.com/) to handle calls and SMS, backed by a pair of AWS Lambda functions written in Kotlin and deployed with the [Serverless](https://serverless.com/) Framework.

# Installation

1. Get a number from [Twilio](https://www.twilio.com/), which costs $1/month.
2. Get an AWS account. 
3. Use [Serverless](https://serverless.com/) to deploy the app with `./gradlew deploy`.
4. This will provision a DynamoDb table, 2 Lambda functions, and 2 API Gateway endpoints.
5. After deployment, log into the AWS console and set the `MY_NUMBER` env var in your Lambda to your phone number.
6. Create a TwiML app and point the endpoints to the ones provisioned in AWS. Point your Twilio number to this app.

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Usage 

To enable partymode, text the number of hours to your Twilio number. This will enable partymode from now with a timeout of that number of hours.

# Reference:
- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call
