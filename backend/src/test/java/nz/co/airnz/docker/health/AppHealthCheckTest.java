package nz.co.airnz.docker.health;


import com.codahale.metrics.health.HealthCheck;
import nz.co.airnz.docker.health.AppHealthCheck;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class AppHealthCheckTest
{

  @Test
  public void testShouldReturnHealthy()
  {
    HealthCheck.Result actual = new AppHealthCheck().execute();

    assertThat(actual.isHealthy()).isTrue();
  }

}
