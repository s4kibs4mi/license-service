# ------------ STEP 1: Build stage ------------
FROM gradle:8.6-jdk17 AS builder

WORKDIR /build

# Copy build files
COPY src src
COPY settings.gradle settings.gradle
COPY build.gradle build.gradle
COPY gradle gradle
COPY gradlew gradlew

# Build the JAR
RUN ./gradlew :bootJar --no-daemon

# ------------ STEP 2: Runtime stage ------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only the generated JAR
COPY --from=builder /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
