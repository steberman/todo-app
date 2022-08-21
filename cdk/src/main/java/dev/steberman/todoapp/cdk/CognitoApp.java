package dev.steberman.todoapp.cdk;

import dev.stratospheric.cdk.ApplicationEnvironment;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;



public class CognitoApp {
  public static void main(final String[] args) {
    App app = new App();

    String environmentName = Util.getEnvironmentName(app);

    String applicationName = Util.getApplicationName(app);

    String accountId = Util.getAccountId(app);

    String region = Util.getRegion(app);

    String applicationUrl = Util.getContextParameter(app, "applicationUrl");

    String loginPageDomainPrefix = Util.getContextParameter(app, "loginPageDomainPrefix");

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
      applicationName,
      environmentName
    );

    new CognitoStack(app, "cognito", awsEnvironment, applicationEnvironment, 
      new CognitoStack.CognitoInputParameters(
        applicationName,
        applicationUrl,
        loginPageDomainPrefix));

    app.synth();
  }

  

}
