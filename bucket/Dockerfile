# ===== Stage 1 : Build (JDK + cache Maven) =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# 0) Obtaining .m2 secret for construction of the build. Will need to be change in prod
RUN mkdir -p /root/.m2
RUN --mount=type=secret,id=maven_settings \
    cp /run/secrets/maven_settings /root/.m2/settings.xml

WORKDIR /app

# 1) Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies separately (important caching)
RUN mvn -B -q dependency:go-offline

# 2) Copy the actual source code
COPY src ./src

# Build the JAR
RUN mvn -B -DskipTests clean package


# ===== Stage 2 : Run (minimal JRE) =====
FROM eclipse-temurin:17-jre AS runner

WORKDIR /app

# Copy the JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# JVM options can be overridden on docker run
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
