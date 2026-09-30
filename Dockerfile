# Multi-stage Docker build for Spring Boot 3 / Java 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build project
COPY src ./src
RUN mvn clean package -DskipTests

# Production runner image (Lightweight Alpine Linux)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy executable jar
COPY --from=build /app/target/*.jar app.jar

# Render dynamic port assignment (defaults to 8080)
ENV PORT=8080
EXPOSE 8080

# Configure memory limits for Render 512MB RAM free tier
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

# Run Spring Boot app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
