FROM maven:3.8.7 as build
COPY . .
RUN mvn package -DskipTests

# Build stage
FROM openjdk:19-jdk-alpine
WORKDIR /app
# Copy the packaged Spring Boot application JAR file into the container
COPY --from=build target/*.jar app/app.jar
# Set the working directory inside the container
# Expose the port that your Spring Boot application listens on
EXPOSE 8092
# Set the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "app/app.jar"]