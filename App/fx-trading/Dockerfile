FROM maven:3.8.8-eclipse-temurin-21-alpine AS maven_tool_chain
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM openjdk:21-jdk
COPY --from=maven_tool_chain /tmp/target/fx-trading-0.0.1-SNAPSHOT.jar /trading-service.jar
CMD /usr/java/openjdk-21/bin/java -jar /trading-service.jar