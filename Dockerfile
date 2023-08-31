FROM ubuntu:latest
LABEL authors="Thang"

ENTRYPOINT ["top", "-b"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file into the container
COPY auth-course.jar app.jar

# Specify the command to run the application
CMD ["java", "-jar", "a\pp.jar"]