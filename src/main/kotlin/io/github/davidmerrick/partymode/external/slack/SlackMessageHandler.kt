package io.github.davidmerrick.partymode.external.slack

import io.github.davidmerrick.slakson.messages.EventCallbackMessage
import io.github.davidmerrick.slakson.messages.SlackChallenge
import io.github.davidmerrick.slakson.messages.SlackMessage
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class SlackMessageHandler(
        private val eventHandler: SlackEventHandler,
        private val filter: MessageFilter,
        private val cache: SlackEventMessageCache
) {
    fun handle(message: SlackMessage): String? {
        log.info("Received Slack message of type ${message.type}")
        if (!filter.apply(message)) {
            log.info("Skipping message")
            return null
        }
        when (message) {
            is SlackChallenge -> run {
                log.info("Handling Slack challenge message")
                return message.challenge
            }
            is EventCallbackMessage -> run {
                log.info("Handling event callback message with id ${message.eventId}")
                if (cache.isMessageCached(message)) {
                    log.info("Message is cached. Skipping.")
                    return null
                }
                eventHandler.handle(message.event)
            }
            else -> run {
                log.warn("Unhandled Slack message type ${message.type}")
            }
        }
        return null
    }
}