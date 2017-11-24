package nz.co.airnz.docker.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Reports health status.
 */
public class AppHealthCheck extends HealthCheck
{
  @Override
  protected Result check() throws Exception
  {
    return Result.healthy();
  }
}
