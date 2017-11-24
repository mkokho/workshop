package nz.co.airnz.docker.api.sample.resource;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Api()
@Path("/sample-other/apple")
@Produces({"application/json"})
public class AppleResource
{
  @GET
  @Path("/all")
  public Response all() {
    List<String> apples = Arrays.asList("granny", "royal gala", "mariri");
    return Response.ok(apples).build();
  }
}
