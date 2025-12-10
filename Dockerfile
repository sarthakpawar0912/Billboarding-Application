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

# Run the built JAR
CMD ["java", "-jar", "target/*.jar"]
