FROM openjdk:21-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl && apt-get clean

COPY build/libs/video-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /app/config/application.yml

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://127.0.0.1:9001/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/config/application.yml"]
