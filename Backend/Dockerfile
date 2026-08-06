# ============================================
# Stage 1: Builder - Maven Build Stage
# ============================================
# This stage uses Maven to build the Spring Boot application
# from source code. It compiles Java code and packages it into a JAR.
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory for build operations
WORKDIR /build

# Copy pom.xml first to leverage Docker layer caching
# If dependencies haven't changed, this layer will be reused
# and Maven won't need to re-download all dependencies
COPY pom.xml .

# Download and cache Maven dependencies
# This is a separate step so that dependency changes don't invalidate
# the source code layer cache
RUN mvn dependency:resolve

# Copy source code into the builder
# This is done after pom.xml and dependency resolution to maximize
# caching efficiency - source changes won't re-download dependencies
COPY src ./src

# Build the application
# - clean: Remove any previous build artifacts
# - package: Compile and package the application into a JAR
# - -DskipTests: Skip tests in Docker build (tests run in CI/CD pipeline)
RUN mvn clean package -DskipTests

# ============================================
# Stage 2: Runtime - Java Runtime Only
# ============================================
# This stage contains only the JRE and the compiled application
# resulting in a much smaller production image (~250-300MB vs ~600MB with Maven)
FROM eclipse-temurin:17-jre-alpine

# Add labels for image metadata
LABEL maintainer="Portfolio Manager Team"
LABEL description="Portfolio Manager Spring Boot Application"
LABEL version="1.0"

# Set working directory in runtime container
WORKDIR /app

# Copy the compiled JAR from the builder stage
# The JAR contains all the compiled code, resources, and embedded Tomcat
COPY --from=builder /build/target/portfolioManager-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (container port - mapped to 8081 on host in docker-compose)
# This tells Docker that the application listens on port 8080
EXPOSE 8080

# Health check for Docker and Docker Compose
# Checks every 30 seconds if the application is responding
# Container is considered unhealthy after 3 consecutive failures
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set Java options for production-like environment
# -XX:+UseG1GC: Use G1 garbage collector (efficient for containers)
# -XX:MaxRAMPercentage=75.0: Use up to 75% of container memory
# -XX:InitialRAMPercentage=25.0: Start with 25% of container memory
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0"

# Set Spring profile to Docker
# Application will use database connection string from docker-compose environment
ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
# java -jar executes the Spring Boot application
# The JAVA_OPTS environment variable passes JVM configuration
CMD ["java", "-jar", "app.jar"]
