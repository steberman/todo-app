package dev.steberman.todoapp.cdk;

import java.util.HashMap;
import java.util.Map;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import dev.stratospheric.cdk.Service;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class ServiceApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = Util.getEnvironmentName(app);

    String applicationName = Util.getApplicationName(app);

    String accountId = Util.getAccountId(app);

    String springProfile = Util.getContextParameter(app, "springProfile");

    String dockerImageUrl = Util.getContextParameter(app, "dockerImageUrl");

    String region = (String) Util.getRegion(app);

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
      applicationName,
      environmentName
    );

    Stack serviceStack = new Stack(app, "ServiceStack", StackProps.builder()
      .stackName(applicationEnvironment.prefix("Service"))
      .env(awsEnvironment)
      .build());

    Service.DockerImageSource dockerImageSource = new Service.DockerImageSource(dockerImageUrl);
    Network.NetworkOutputParameters networkOutputParameters = Network.getOutputParametersFromParameterStore(serviceStack, applicationEnvironment.getEnvironmentName());
    Service.ServiceInputParameters serviceInputParameters = new Service.ServiceInputParameters(dockerImageSource, environmentVariables(springProfile))
      .withHealthCheckIntervalSeconds(30);

    new Service(
      serviceStack,
      "Service",
      awsEnvironment,
      applicationEnvironment,
      serviceInputParameters,
      networkOutputParameters);

    app.synth();
  }

  static Map<String, String> environmentVariables(String springProfile) {
    Map<String, String> vars = new HashMap<>();
    vars.put("SPRING_PROFILES_ACTIVE", springProfile);
    return vars;
  }


  
}
