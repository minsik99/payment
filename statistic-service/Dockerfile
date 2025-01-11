FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/statistic-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/config/application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/config/application.yml"]