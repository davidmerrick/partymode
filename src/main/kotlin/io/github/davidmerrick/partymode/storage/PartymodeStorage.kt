package io.github.davidmerrick.partymode.storage

import com.google.cloud.Timestamp
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.Query
import io.github.davidmerrick.partymode.config.PartymodeConfig
import mu.KotlinLogging
import java.time.temporal.ChronoUnit
import javax.inject.Singleton

private val log = KotlinLogging.logger {}

private const val CREATED_AT_FIELD = "created_at"
private const val TTL_FIELD = "ttl_hours"
private const val DEFAULT_DURATION_HOURS = 3

@Singleton
class PartymodeStorage(private val config: PartymodeConfig.FirestoreConfig) {

    private val db: Firestore by lazy {
        val firestoreOptions: FirestoreOptions = FirestoreOptions.getDefaultInstance()
            .toBuilder()
            .setProjectId(config.projectId)
            .build()
        firestoreOptions.service
    }

    fun disablePartyMode() = enable(-1)

    fun enable(numHours: Int = DEFAULT_DURATION_HOURS) {
        log.info("Saving $numHours to db")
        val lease = mapOf(
            CREATED_AT_FIELD to Timestamp.now(),
            TTL_FIELD to numHours
        )

        db.collection(config.collectionName)
            .add(lease)
            .get()
    }

    fun isPartymodeEnabled(): Boolean {
        val snapshot = db.collection(config.collectionName)
            .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .get()

        if (snapshot.isEmpty) {
            return false
        }

        // Check if expiration time is after now
        val lease = snapshot.documents[0]
        val createdAt = lease[CREATED_AT_FIELD] as Timestamp
        log.info("Found lease created at $createdAt")

        val ttlHours = lease[TTL_FIELD] as Long
        val expiresAt = createdAt
            .toDate()
            .toInstant()
            .plus(ttlHours, ChronoUnit.HOURS)
        val now = Timestamp.now().toDate().toInstant()

        return expiresAt.isAfter(now)
    }
}
