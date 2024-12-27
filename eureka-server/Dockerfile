FROM openjdk:21-jdk-slim

RUN apt-get update && apt-get install -y curl && apt-get clean

WORKDIR /app

COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml application.yml

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://127.0.0.1:8761/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=application.yml"]

