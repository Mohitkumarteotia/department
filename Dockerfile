FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Mohit Kumar"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]