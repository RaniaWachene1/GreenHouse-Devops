# Stage 1: Build the application with Maven
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download the dependencies, go offline, and verify
RUN mvn dependency:go-offline

# Copy the source code to the container
COPY src ./src

# Build the application, skipping tests for faster builds
RUN mvn package -DskipTests

# Stage 2: Create the runtime container
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=build /app/target/GH_Backend-*.jar app.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
