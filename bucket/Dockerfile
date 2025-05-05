# Stage 1: Build the JAR
FROM maven:3.9.5-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY . .

# Skip tests for faster build (optional)
RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
