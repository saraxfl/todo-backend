//FROM eclipse-temurin:17-jre-alpine
/WORKDIR /app
/COPY target/quarkus-app/lib/ /app/lib/
/COPY target/quarkus-app/*.jar /app/
/COPY target/quarkus-app/app/ /app/app/
/COPY target/quarkus-app/quarkus/ /app/quarkus/
/EXPOSE 8080

FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/quarkus-app/ /app/

EXPOSE 8080

CMD ["java", "-jar", "/app/quarkus-run.jar"]