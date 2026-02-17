# Build stage
FROM eclipse-temurin:23-jdk-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

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

