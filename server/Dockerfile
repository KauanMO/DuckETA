FROM gradle:8.4-jdk17 AS builder
WORKDIR /server
COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17.0.11_9-jdk-alpine
WORKDIR /server
COPY --from=builder /server/build/libs/*.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]