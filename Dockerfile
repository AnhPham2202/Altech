# Stage 1: Build với Maven 3.9.9 và JDK 22
FROM maven:3.9.9-eclipse-temurin-22 AS build
WORKDIR /app

# Copy file pom.xml trước để cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime với JDK 22
FROM eclipse-temurin:22-jdk
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /app/target/altech-0.0.1-SNAPSHOT.jar app.jar
# Expose port 8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
