# partymode 🎉

Buzzing people into your apartment building when you're having a party (Or, during COVID, getting your food delivered)? 

Ain't nobody got time for that! That's why I built partymode. 
It's a service that:
- Auto-buzzes people into my building when enabled.
- Otherwise forwards calls to my phone. 

It uses [Twilio](https://www.twilio.com/) to handle calls and SMS. 
It integrates with Slack so I or anyone in my workspace can enable it.

Technical details:
- Written in Kotlin with the Micronaut framework.
- Has an SNS integration that downstream consumers can connect to (I use this for a Slack webhook/Alexa alert).
- Has a Google Assistant endpoint so you can be extra-lazy and just say "Hey Google, open partymode" to enable it for 3 hours.

# Installation

## Prerequisites

* Get a number from [Twilio](https://www.twilio.com/), which costs $1/month.
* Get an AWS account. 
* Get admin access to your Slack workspace.

## Secret Manager Setup

Add the following secrets to Secrets Manager:
`TWILIO_AUTH_TOKEN`

Next, grant "Secret Manager Secret Accessor" and "Secret Manager Secret Viewer" privileges to the 
IAM compute account for your service.

## Slack setup

- Create a Slack webhook and set the `SLACK_WEBHOOK_URI` env var to that uri.
- Create a Slack bot and subscribe to the following bot events: `app_mention`, `message.channels`, and `message.im`.
- Add oAuth scope for `chat:write`.

## Initial deployment

Set the `PARTYMODE_PHONE_MY_NUMBER` env var to your phone number. Make sure the format is `+19999999999`. 
Set the `PARTYMODE_PHONE_CALLBOX_NUMBER` env var to your call box's phone number. 
Make sure the format is `+19999999999`.
The app will reject calls from any other number, in the event you inevitably get spammed with robocalls.

## Twilio setup

In your Twilio dashboard, create a TwiML app and point the endpoints to the ones provisioned in AWS. 
Point your Twilio number to this app.

## Final steps

Finally, of course, have your apartment forward your call box to your Twilio number 😎.

# Reference:

- https://www.twilio.com/blog/2018/03/send-an-sms-message-with-kotlin-in-30-seconds.html
- [Respond to an incoming phone call with Java](https://www.twilio.com/docs/voice/quickstart/java#respond-to-an-incoming-phone-call)
- [Validating incoming requests from Twilio](https://www.twilio.com/docs/usage/tutorials/how-to-secure-your-servlet-app-by-validating-incoming-twilio-requests)
