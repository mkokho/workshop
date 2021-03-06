package nz.co.airnz.docker

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest
import com.amazonaws.services.ec2.model.SecurityGroup
import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.ecs.AmazonECSClientBuilder
import com.amazonaws.services.ecs.model.*
import org.slf4j.LoggerFactory

class AWSClient(private val config: AWSConfiguration) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val mapper = OurObjectMapper.INSTANCE

    private val ecsClient: AmazonECS
    private val ec2Client: AmazonEC2

    init {
        val awsCredentials = BasicAWSCredentials(config.key, config.secret)
        val awsCredentialsProvider = AWSStaticCredentialsProvider(awsCredentials)
        ecsClient = AmazonECSClientBuilder.standard()
            .withCredentials(awsCredentialsProvider)
            .withRegion(config.region)
            .build()
        ec2Client = AmazonEC2ClientBuilder.standard()
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


    fun fetchECSServices(cluster: Cluster): List<Service> {
        fun fetch(pageToken: String?, acc: List<Service>): List<Service> {
            val listRequest = ListServicesRequest()
                .withCluster(cluster.clusterArn)
                .withMaxResults(10)
                .withNextToken(pageToken)
            val listResponse = ecsClient.listServices(listRequest)
            if (listResponse.serviceArns.isEmpty()) {
                return acc
            }

            val describeRequest = DescribeServicesRequest()
                .withCluster(cluster.clusterArn)
                .withServices(listResponse.serviceArns)
            val describeResponse = ecsClient.describeServices(describeRequest)

            return if (describeResponse.failures.isEmpty()) {
                if (listResponse.nextToken == null) {
                    acc.plus(describeResponse.services)
                } else {
                    fetch(listResponse.nextToken, acc.plus(describeResponse.services))
                }
            } else {
                throw RuntimeException("Failed to describe services: " + describeResponse.failures)
            }
        }

        return try {
            fetch(null, listOf())
        } catch (e: Exception) {
            log.error("Unexpected exception: {}", e.message)
            listOf()
        }
    }

    fun fetchSecurityGroups(): List<SecurityGroup> {
        fun fetch(pageToken: String?, acc: List<SecurityGroup>): List<SecurityGroup> {
            val describeRequest = DescribeSecurityGroupsRequest()
                .withMaxResults(1000)
                .withNextToken(pageToken)
            val describeResponse = ec2Client.describeSecurityGroups(describeRequest)
            if (describeResponse.securityGroups.isEmpty()) {
                return acc
            }

            return if (describeResponse.nextToken == null) {
                acc.plus(describeResponse.securityGroups)
            } else {
                fetch(describeResponse.nextToken, acc.plus(describeResponse.securityGroups))
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