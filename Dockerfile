FROM eclipse-temurin:22-jre-alpine

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod

COPY app.jar app.jar

EXPOSE 1406

ENTRYPOINT ["java", "-jar", "app.jar"]
