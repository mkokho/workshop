package nz.co.airnz.docker.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;

public class CORSFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    MultivaluedMap<String, Object> headers = responseContext.getHeaders();

    headers.add(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type, accept, authorization");
    headers.add(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS, HEAD");

  }
}