package io.github.davidmerrick.partymode.config

import io.github.davidmerrick.partymode.external.gcp.SecretsFetcher
import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("sns")
class SnsConfig(private val secretsFetcher: SecretsFetcher) {
    val topicArn by lazy { secretsFetcher.getSecret("SNS_TOPIC_ARN") }
    val accessKeyId by lazy { secretsFetcher.getSecret("AWS_ACCESS_KEY_ID") }
    val secretAccessKey by lazy { secretsFetcher.getSecret("AWS_SECRET_ACCESS_KEY") }
    var enabled: Boolean = false

    @get:NotBlank
    var region: String = ""
}
