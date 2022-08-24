package dev.steberman.todoapp.cdk;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import dev.stratospheric.cdk.PostgresDatabase;
import dev.stratospheric.cdk.Service;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.secretsmanager.ISecret;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.constructs.Construct;

public class ServiceApp {

  public static void main(final String[] args) {
    App app = new App();

    String environmentName = Util.getEnvironmentName(app);

    String applicationName = Util.getApplicationName(app);

    String accountId = Util.getAccountId(app);

    String springProfile = Util.getContextParameter(app, "springProfile");

    String dockerRepositoryName = Util.getContextParameter(app, "dockerRepositoryName");

    String dockerImageTag = Util.getContextParameter(app, "dockerImageTag");
    

    String region = Util.getRegion(app);

    Environment awsEnvironment = Util.makeEnv(accountId, region);

    ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
      applicationName,
      environmentName
    );

    

    // This stack is just a container for the parameters below, because they need a Stack as a scope.
    // We're making this parameters stack unique with each deployment by adding a timestamp, because updating an existing
    // parameters stack will fail because the parameters may be used by an old service stack.
    // This means that each update will generate a new parameters stack that needs to be cleaned up manually!
    long timestamp = System.currentTimeMillis();
    Stack parametersStack = new Stack(app, "ServiceParameters-" + timestamp, StackProps.builder()
      .stackName(applicationEnvironment.prefix("Service-Parameters-" + timestamp))
      .env(awsEnvironment)
      .build());

    Stack serviceStack = new Stack(app, "ServiceStack", StackProps.builder()
      .stackName(applicationEnvironment.prefix("Service"))
      .env(awsEnvironment)
      .build());

    PostgresDatabase.DatabaseOutputParameters databaseOutputParameters =
      PostgresDatabase.getOutputParametersFromParameterStore(parametersStack, applicationEnvironment);

     CognitoStack.CognitoOutputParameters cognitoOutputParameters =
       CognitoStack.getOutputParametersFromParameterStore(parametersStack, applicationEnvironment);

    // MessagingStack.MessagingOutputParameters messagingOutputParameters =
    //   MessagingStack.getOutputParametersFromParameterStore(parametersStack, applicationEnvironment);

    // ActiveMqStack.ActiveMqOutputParameters activeMqOutputParameters =
    //   ActiveMqStack.getOutputParametersFromParameterStore(parametersStack, applicationEnvironment);

    // List<String> securityGroupIdsToGrantIngressFromEcs = Arrays.asList(
    //   databaseOutputParameters.getDatabaseSecurityGroupId(),
    //   activeMqOutputParameters.getActiveMqSecurityGroupId()
    // );

    new Service(
      serviceStack,
      "Service",
      awsEnvironment,
      applicationEnvironment,
      new Service.ServiceInputParameters(
        new Service.DockerImageSource(dockerRepositoryName, dockerImageTag),
        Collections.emptyList(), // securityGroupIdsToGrantIngressFromEcs,
        environmentVariables(
          serviceStack,
          databaseOutputParameters,
          cognitoOutputParameters,
          // messagingOutputParameters,
          // activeMqOutputParameters,
          springProfile,
          environmentName))
        .withTaskRolePolicyStatements(List.of(
          // PolicyStatement.Builder.create()
          //   .sid("AllowSQSAccess")
          //   .effect(Effect.ALLOW)
          //   .resources(List.of(
          //     String.format("arn:aws:sqs:%s:%s:%s", region, accountId, messagingOutputParameters.getTodoSharingQueueName())
          //   ))
          //   .actions(Arrays.asList(
          //     "sqs:DeleteMessage",
          //     "sqs:GetQueueUrl",
          //     "sqs:ListDeadLetterSourceQueues",
          //     "sqs:ListQueues",
          //     "sqs:ListQueueTags",
          //     "sqs:ReceiveMessage",
          //     "sqs:SendMessage",
          //     "sqs:ChangeMessageVisibility",
          //     "sqs:GetQueueAttributes"))
          //   .build(),

          PolicyStatement.Builder.create()
            .sid("AllowCreatingUsers")
            .effect(Effect.ALLOW)
            .resources(
              List.of(String.format("arn:aws:cognito-idp:%s:%s:userpool/%s", region, accountId, cognitoOutputParameters.getUserPoolId()))
            )
            .actions(List.of(
              "cognito-idp:AdminCreateUser"
            ))
            .build()

          // PolicyStatement.Builder.create()
          //   .sid("AllowSendingEmails")
          //   .effect(Effect.ALLOW)
          //   .resources(
          //     List.of(String.format("arn:aws:ses:%s:%s:identity/stratospheric.dev", region, accountId))
          //   )
          //   .actions(List.of(
          //     "ses:SendEmail",
          //     "ses:SendRawEmail"
          //   ))
          //   .build(),

          // PolicyStatement.Builder.create()
          //   .sid("AllowDynamoTableAccess")
          //   .effect(Effect.ALLOW)
          //   .resources(
          //     List.of(String.format("arn:aws:dynamodb:%s:%s:table/%s", region, accountId, applicationEnvironment.prefix("breadcrumbs")))
          //   )
          //   .actions(List.of(
          //     "dynamodb:Scan",
          //     "dynamodb:Query",
          //     "dynamodb:PutItem",
          //     "dynamodb:GetItem",
          //     "dynamodb:BatchWriteItem",
          //     "dynamodb:BatchWriteGet"
          //     ))
          //   .build(),

          // PolicyStatement.Builder.create()
          //   .sid("AllowSendingMetricsToCloudWatch")
          //   .effect(Effect.ALLOW)
          //   .resources(singletonList("*")) // CloudWatch does not have any resource-level permissions, see https://stackoverflow.com/a/38055068/9085273
          //   .actions(singletonList("cloudwatch:PutMetricData"))
          //   .build()
        )
        )
        .withStickySessionsEnabled(true)
        .withHealthCheckPath("/actuator/health")
        .withAwsLogsDateTimeFormat("%Y-%m-%dT%H:%M:%S.%f%z")
        .withHealthCheckIntervalSeconds(30), // needs to be long enough to allow for slow start up with low-end computing instances

      Network.getOutputParametersFromParameterStore(serviceStack, applicationEnvironment.getEnvironmentName()));

    app.synth();
  }

  static Map<String, String> environmentVariables(
    Construct scope,
    PostgresDatabase.DatabaseOutputParameters databaseOutputParameters,
    CognitoStack.CognitoOutputParameters cognitoOutputParameters,
    // MessagingStack.MessagingOutputParameters messagingOutputParameters,
    // ActiveMqStack.ActiveMqOutputParameters activeMqOutputParameters,
    String springProfile,
    String environmentName
  ) {
    Map<String, String> vars = new HashMap<>();

    String databaseSecretArn = databaseOutputParameters.getDatabaseSecretArn();
    ISecret databaseSecret = Secret.fromSecretCompleteArn(scope, "databaseSecret", databaseSecretArn);

    vars.put("SPRING_PROFILES_ACTIVE", springProfile);

    vars.put("SPRING_DATASOURCE_URL",
      String.format("jdbc:postgresql://%s:%s/%s",
        databaseOutputParameters.getEndpointAddress(),
        databaseOutputParameters.getEndpointPort(),
        databaseOutputParameters.getDbName()));
    vars.put("SPRING_DATASOURCE_USERNAME",
      databaseSecret.secretValueFromJson("username").toString());
    vars.put("SPRING_DATASOURCE_PASSWORD",
      databaseSecret.secretValueFromJson("password").toString());

    vars.put("COGNITO_CLIENT_ID", cognitoOutputParameters.getUserPoolClientId());
    vars.put("COGNITO_CLIENT_SECRET", cognitoOutputParameters.getUserPoolClientSecret());
    vars.put("COGNITO_USER_POOL_ID", cognitoOutputParameters.getUserPoolId());
    vars.put("COGNITO_LOGOUT_URL", cognitoOutputParameters.getLogoutUrl());
    vars.put("COGNITO_PROVIDER_URL", cognitoOutputParameters.getProviderUrl());

    // vars.put("TODO_SHARING_QUEUE_NAME", messagingOutputParameters.getTodoSharingQueueName());
    // vars.put("WEB_SOCKET_RELAY_ENDPOINT", activeMqOutputParameters.getStompEndpoint());
    // vars.put("WEB_SOCKET_RELAY_USERNAME", activeMqOutputParameters.getActiveMqUsername());
    // vars.put("WEB_SOCKET_RELAY_PASSWORD", activeMqOutputParameters.getActiveMqPassword());
    vars.put("ENVIRONMENT_NAME", environmentName);

    return vars;
  }

  
}
