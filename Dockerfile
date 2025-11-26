FROM maven:3.9.9-eclipse-temurin-22 AS builder

WORKDIR /app

COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 1406

ENTRYPOINT ["java", "-jar", "app.jar"]
