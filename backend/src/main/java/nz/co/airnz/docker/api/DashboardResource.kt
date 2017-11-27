package nz.co.airnz.docker.api

import io.swagger.annotations.Api
import nz.co.airnz.docker.AWSClient

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Api
@Path("/dashboard")
@Produces("application/json")
class DashboardResource(val aws: AWSClient) {


    @GET
    @Path("/")
    fun all(): Response {
        val clusters = aws.fetchECSClusters()
        val services = clusters.flatMap { aws.fetchECSServices(it) }

        val response = DashboardData(
            clusters = clusters,
            services = services
        )
        return Response.ok(response).build()
    }
}
