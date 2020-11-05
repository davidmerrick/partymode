package io.github.davidmerrick.partymode.external.gcp

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import io.github.davidmerrick.partymode.config.GoogleCloudConfig
import mu.KotlinLogging
import javax.inject.Singleton

private const val LATEST_VERSION = "latest"

private val log = KotlinLogging.logger {}

@Singleton
class SecretsFetcher(private val config: GoogleCloudConfig) {

    private val client by lazy { SecretManagerServiceClient.create() }

    fun getSecret(secretName: String): String {
        log.info("Fetching secret named $secretName")
        val versionName = SecretVersionName.of(
            config.projectId,
            secretName,
            LATEST_VERSION
        )

        return client.accessSecretVersion(versionName)
            .payload
            .data
            .toStringUtf8()
    }
}
