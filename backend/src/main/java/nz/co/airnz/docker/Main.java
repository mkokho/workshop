package nz.co.airnz.docker;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.listing.ApiListingResource;
import nz.co.airnz.docker.api.DashboardResource;
import nz.co.airnz.docker.filter.CORSFilter;
import nz.co.airnz.docker.health.AppHealthCheck;

/**
 * Entry point to the app.
 */
public class Main extends Application<MainConfiguration> {
  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }

  @Override
  public void initialize(Bootstrap<MainConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
      new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(),
        new EnvironmentVariableSubstitutor(true)
      )
    );
    bootstrap.setObjectMapper(OurObjectMapper.INSTANCE);
  }

  @Override
  public void run(MainConfiguration config, Environment environment) throws Exception {
    AWSClient awsClient = new AWSClient(config.getAws());

    environment.healthChecks().register("status", new AppHealthCheck());
    environment.jersey().register(new ApiListingResource());
    environment.jersey().register(new DashboardResource(awsClient));
    environment.jersey().register(new CORSFilter());
  }
}
