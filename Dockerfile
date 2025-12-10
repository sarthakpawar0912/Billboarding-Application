# Use Java 21 (LTS)
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy everything into container
COPY . .

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Render exposes this port automatically
EXPOSE 8080


# ✅ Use EXACT jar name (no wildcard)
CMD ["java", "-jar", "target/BillBoarding-And-Hording-0.0.1-SNAPSHOT.jar"]