package nz.co.airnz.docker.api

import io.swagger.annotations.Api

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Api
@Path("/dashboard")
@Produces("application/json")
class DashboardResource {
    @GET
    @Path("/")
    fun all(): Response {
        val clusters = listOf(
            Cluster("cluster1"),
            Cluster("cluster2")
        )
        return Response.ok(clusters).build()
    }
}
