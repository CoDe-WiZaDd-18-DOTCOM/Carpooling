# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-alpine AS build
WORKDIR /app

# Copy Maven wrapper and project files
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Give execute permissions to mvnw
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source files
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install CA certs so rediss:// works
RUN apk add --no-cache ca-certificates \
 && update-ca-certificates

# Copy jar from builder stage
COPY --from=build /app/target/carpooling-0.0.1-SNAPSHOT.jar app.jar

# Use dynamic port
EXPOSE 5001
ENTRYPOINT ["java", "-jar", "app.jar"]
