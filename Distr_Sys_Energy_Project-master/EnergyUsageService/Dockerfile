# Stage 1: build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /app/wait-for-it.sh
RUN apk add --no-cache bash
RUN chmod +x /app/wait-for-it.sh
ENTRYPOINT ["/app/wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "/app/app.jar"]