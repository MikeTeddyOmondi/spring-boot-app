# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only the pom.xml first for dependency caching
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the source and build
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre-jammy AS release

WORKDIR /app

# Copy the fat JAR from the build stage
COPY --from=build /app/target/todo-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
