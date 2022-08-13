package dev.steberman.todoapp.cdk;

import dev.stratospheric.cdk.Network;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class NetworkApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = Util.getEnvironmentName(app);

    String accountId = Util.getAccountId(app);

    String region = Util.getRegion(app);

    // 'String sslCertificateArn = Util.getContextParameter(app, "sslCertificateArn");'

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    Stack networkStack = new Stack(app, "NetworkStack", StackProps.builder()
      .stackName(environmentName + "-Network")
      .env(awsEnvironment)
      .build());

    new Network(
      networkStack,
      "Network",
      awsEnvironment,
      environmentName,
      new Network.NetworkInputParameters()
        //.withSslCertificateArn(sslCertificateArn)
        );

    app.synth();
  }

}
