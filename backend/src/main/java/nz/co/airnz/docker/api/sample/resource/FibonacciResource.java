package nz.co.airnz.docker.api.sample.resource;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Api()
@Path("/sample")
@Produces({"application/json"})
public class FibonacciResource
{
  @GET
  @Path("/fibonacci/{num}")
  public Response computeFibonacci(
      @ApiParam(required = true)
      @PathParam("num") @Max(10000000) @Min(1) Long num
  )
  {
    Tuple<BigInteger, BigInteger> seed = new Tuple<>(BigInteger.ONE, BigInteger.ONE);
    UnaryOperator<Tuple<BigInteger, BigInteger>> f = x -> new Tuple<>(x._2, x._1.add(x._2));
    Stream<BigInteger> fiboStream = Stream.iterate(seed, f).map(x -> x._1).limit(num);

    BigInteger res = fiboStream.skip(num - 1).findFirst().orElseGet(() -> BigInteger.ONE);
    return Response.ok(res).build();
  }

  private static class Tuple<T, U>
  {
    final T _1;
    final U _2;

    public Tuple(T t, U u)
    {
      this._1 = t;
      this._2 = u;
    }
  }
}
