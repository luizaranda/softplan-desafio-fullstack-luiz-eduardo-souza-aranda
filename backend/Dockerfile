FROM maven:3.6.3-jdk-11-openj9 as build
WORKDIR /app

COPY mvnw .
COPY pom.xml .

COPY src src

RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp
ARG DEPENDENCY=/app/target

COPY --from=build ${DEPENDENCY}/process-0.0.1-SNAPSHOT.jar process-0.0.1-SNAPSHOT.jar
COPY --from=build ${DEPENDENCY}/classes /app

ENTRYPOINT ["java", "-cp","app:app/*", "-jar", "/process-0.0.1-SNAPSHOT.jar"]