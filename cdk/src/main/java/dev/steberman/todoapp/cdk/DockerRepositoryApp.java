package dev.steberman.todoapp.cdk;

import dev.stratospheric.cdk.DockerRepository;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class DockerRepositoryApp {

  public static void main(final String[] args) {
    App app = new App();

    String accountId = Util.getAccountId(app);

    String region = Util.getRegion(app);

    String applicationName = Util.getApplicationName(app);

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    Stack dockerRepositoryStack = new Stack(app, "DockerRepositoryStack", StackProps.builder()
        .stackName(applicationName + "-DockerRepository")
        .env(awsEnvironment)
        .build());

    new DockerRepository(
        dockerRepositoryStack,
        "DockerRepository",
        awsEnvironment,
        new DockerRepository.DockerRepositoryInputParameters(
            applicationName, accountId, 10, false));

    app.synth();
  }

}
