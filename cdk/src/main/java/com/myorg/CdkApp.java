package com.myorg;

import static java.util.Objects.requireNonNull;

import dev.stratospheric.cdk.SpringBootApplicationStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;


public class CdkApp {

        //private static final String DOCKER_IMAGE_URL = "docker.io/stebeman/todo-app-v1:latest";
        private static final String DOCKER_IMAGE_URL = "docker.io/stratospheric/todo-app-v1:latest";
        
        public static void main(final String[] args) {
                App app = new App();

                String accountId = (String) app
                                .getNode()
                                .tryGetContext("accountId");
                requireNonNull(accountId, "context variable 'accountId' must not be null");

                String region = (String) app
                                .getNode()
                                .tryGetContext("region");
                requireNonNull(region, "context variable 'region' must not be null");

                new SpringBootApplicationStack(
                                app,
                                "SpringBootApplication",
                                makeEnv(accountId, region),
                                DOCKER_IMAGE_URL);

                app.synth();
        }

        private static Environment makeEnv(String accountId, String region) {
                return Environment.builder()
                                .account(accountId)
                                .region(region)
                                .build();
        }
}
