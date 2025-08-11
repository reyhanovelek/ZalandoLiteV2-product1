#=================================================================================
#  Dockerfile: Multi-Stage Build for Spring Boot + Docker
#
#  Date: 2025-08-11
#
#  ** Only for PRODUCT service ***
#=================================================================================

# ========================
# 1. BUILD STAGE
# Heavy wait because it has maven.
# ========================
# Use Maven with Java 17 (Eclipse Temurin) on Alpine Linux for a lightweight build image
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set the working directory inside the container to /app
# All subsequent commands (like COPY or RUN) will be relative to this directory
WORKDIR /app

# Copy only the pom.xml first to leverage Docker layer caching
# This speeds up rebuilds when dependencies haven't changed
COPY pom.xml .

# Pre-fetch all project dependencies defined in pom.xml (avoid downloading the dependancy again)
# This step runs Maven in "offline mode" to prepare dependencies before copying source code
RUN mvn dependency:go-offline

# Copy the entire source code into the container
COPY src ./src

# Build the application using Maven, skipping tests to speed up the build
# The output will be a .jar file inside /app/target/
# `-DskipTests` disables tests to reduce build time; remove this in production

RUN mvn clean package -DskipTests

# ========================
# 2. RUN STAGE
# ========================

# Use a minimal Java 17 JDK Alpine image for running the app
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the runtime container
WORKDIR /app

# Copy the generated JAR file from the build stage into the runtime container
# COPY --from=build /app/target/product-service-*.jar app.jar (use this if there are several .jar file)
COPY --from=build /app/target/*.jar app.jar

#Expose the default Spring Boot port
EXPOSE 8586

# Define the entrypoint command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]