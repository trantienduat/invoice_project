FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
COPY src ./src

RUN chmod +x gradlew && \
    ./gradlew clean build -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENV SERVER_PORT=5001
EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"]
