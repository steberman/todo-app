package dev.steberman.todoapp.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.PostgresDatabase;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class DatabaseApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = Util.getEnvironmentName(app);

    String applicationName = Util.getApplicationName(app);

    String accountId = Util.getAccountId(app);

    String region = Util.getRegion(app);

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
      applicationName,
      environmentName
    );

    Stack databaseStack = new Stack(app, "DatabaseStack", StackProps.builder()
      .stackName(applicationEnvironment.prefix("Database"))
      .env(awsEnvironment)
      .build());

    new PostgresDatabase(
      databaseStack,
      "Database",
      awsEnvironment,
      applicationEnvironment,
      new PostgresDatabase.DatabaseInputParameters());

    app.synth();
  }

}
