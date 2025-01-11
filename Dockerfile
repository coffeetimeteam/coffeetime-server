# Base image for the build stage (Gradle)
FROM --platform=linux/amd64 openjdk:17-jdk-slim AS build

# Set the working directory in the container for the build stage
WORKDIR /app

# Copy the Gradle wrapper and build scripts
COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle /app/
COPY settings.gradle /app/

# Copy the source code into the container
COPY src /app/src/main

# Run Gradle build to generate the JAR file
RUN ./gradlew build

# Create a new stage for the runtime environment
FROM --platform=linux/amd64 openjdk:17-jdk-slim

# Set the working directory in the container for the runtime stage
WORKDIR /app

# Copy the application configuration file to the container
COPY src/main/resources/application.yml /app/application.yml

# Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Define the entrypoint for the container
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dotel.resource.attributes=service.name=auth-server", "-jar", "app.jar"]
