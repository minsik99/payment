FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/gateway-server-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=application.yml"]