#./mvnw spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=dev"
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Drun.jvmArguments="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses"
