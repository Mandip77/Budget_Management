# Use the official OpenJDK image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot application jar into the container
COPY target/SuperBudget-0.0.1-SNAPSHOT.jar SuperBudget.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "SuperBudget.jar"]
