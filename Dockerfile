# Build stage
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:23-jre-alpine

WORKDIR /deployments

COPY --from=build /app/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/target/quarkus-app/*.jar ./
COPY --from=build /app/target/quarkus-app/app/ ./app/
COPY --from=build /app/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

CMD ["sh", "-c", "java $JAVA_OPTS -jar quarkus-run.jar"]
