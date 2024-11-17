FROM maven:3.8.8-eclipse-temurin-21-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM openjdk:21-jdk
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/quote-service-0.0.1-SNAPSHOT.jar /quote-service.jar
CMD /usr/java/openjdk-21/bin/java -jar /quote-service.jar