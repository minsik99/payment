FROM openjdk:21-jdk-slim

EXPOSE 9000

RUN apt-get update && apt-get install -y curl && apt-get clean

WORKDIR /app

COPY build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/config/application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yml"]
