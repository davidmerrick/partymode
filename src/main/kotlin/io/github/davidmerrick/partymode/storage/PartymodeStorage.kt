package io.github.davidmerrick.partymode.storage

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import io.github.davidmerrick.partymode.config.PartymodeConfig
import io.github.davidmerrick.partymode.models.PartyLease
import mu.KotlinLogging
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

@Singleton
class PartymodeStorage(private val config: PartymodeConfig.FirestoreConfig) {

    private val firestoreOptions: FirestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId(config.projectId)
            .build()
    private val db: Firestore = firestoreOptions.service

    fun disablePartyMode() = enableForHours(0)

    fun enableForHours(numHours: Int) {
        log.info("Saving $numHours to db")
//        client.putItem(config.tableName,
//                mapOf("start_time" to AttributeValue(Instant.now().toString()),
//                        "timeout" to AttributeValue(numHours.toString()))
//        )
    }

    fun isPartymodeEnabled() = getLatestItem().isActive()

    private fun getLatestItem(): PartyLease {
//        val item = items.maxBy { Instant.parse(it["start_time"]!!.s) }
//
//        item?.let {
//            log.info("Got item: $item")
//            return PartyLease(item["start_time"]!!.s, item["timeout"]!!.s.toInt())
//        }

        log.info("No item found. Returning default.")
        return PartyLease.default()
    }
}