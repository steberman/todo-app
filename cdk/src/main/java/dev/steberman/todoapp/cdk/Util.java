package dev.steberman.todoapp.cdk;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;

public class Util {

    private Util() {}

    public static void requireNonEmpty(String string, String message) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }


  static Environment makeEnv(String account, String region) {
    return Environment.builder()
      .account(account)
      .region(region)
      .build();
  }

  static String getAccountId(App app) {
    String accountId = (String) app.getNode().tryGetContext("accountId");
    requireNonEmpty(accountId, "context variable 'accountId' must not be null");
    return accountId;
  }

  static String getRegion(App app) {
    String region = (String) app.getNode().tryGetContext("region");
    requireNonEmpty(region, "context variable 'region' must not be null");
    return region;
  }

  static String getApplicationName(App app) {
    String applicationName = (String) app.getNode().tryGetContext("applicationName");
    requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");
    return applicationName;
  }

  static String getEnvironmentName(App app) {
    String environmentName = (String) app.getNode().tryGetContext("environmentName");
    requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");
    return environmentName;
  }

  static String getContextParameter(App app, String key) {
    String param = (String) app.getNode().tryGetContext(key);
    requireNonEmpty(param, "context variable '" + key + "' must not be null");
    return param;
  }
}
