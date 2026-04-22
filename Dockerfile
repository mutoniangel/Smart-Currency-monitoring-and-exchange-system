# Build stage
FROM maven:3.8-eclipse-temurin-17 AS build
WORKDIR /app
# Copy the smart_currency directory contents
COPY smart_currency/ ./
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/smartcurrency.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
