package nz.co.airnz.docker

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.ecs.AmazonECSClientBuilder
import com.amazonaws.services.ecs.model.Cluster
import com.amazonaws.services.ecs.model.DescribeClustersRequest
import com.amazonaws.services.ecs.model.ListClustersRequest
import org.slf4j.LoggerFactory

class AWSClient(private val config: AWSConfiguration) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val mapper = OurObjectMapper.INSTANCE

    val ecsClient: AmazonECS

    init {
        val awsCredentials = BasicAWSCredentials(config.key, config.secret)
        val awsCredentialsProvider = AWSStaticCredentialsProvider(awsCredentials)
        ecsClient = AmazonECSClientBuilder.standard()
            .withCredentials(awsCredentialsProvider)
            .withRegion(config.region)
            .build()
    }

    fun fetchECSClusters(): List<Cluster> {
        fun fetch(pageToken: String?, acc: List<Cluster>): List<Cluster> {
            val listRequest = ListClustersRequest()
                .withMaxResults(100)
                .withNextToken(pageToken)
            val listResponse = ecsClient.listClusters(listRequest)

            val describeRequest = DescribeClustersRequest()
                .withClusters(listResponse.clusterArns)
            val describeResponse = ecsClient.describeClusters(describeRequest)

            return if (describeResponse.failures.isEmpty()) {
                if (listResponse.nextToken == null) {
                    acc.plus(describeResponse.clusters)
                } else {
                    fetch(listResponse.nextToken, acc.plus(describeResponse.clusters))
                }
            } else {
                throw RuntimeException("Failed to describe clusters: " + describeResponse.failures)
            }
        }

        return try {
            fetch(null, listOf())
        } catch (e: Exception) {
            log.error("Unexpected exception: {}", e.message)
            listOf()
        }
    }
}