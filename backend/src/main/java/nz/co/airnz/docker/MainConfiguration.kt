package nz.co.airnz.docker

import io.dropwizard.Configuration

class MainConfiguration(
    val aws: AWSConfiguration
): Configuration()

data class AWSConfiguration(
    val key: String,
    val secret: String,
    val region: String
)