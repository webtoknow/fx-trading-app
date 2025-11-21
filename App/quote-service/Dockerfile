FROM maven:3.9.8-eclipse-temurin-21 AS maven_tool_chain
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM eclipse-temurin:21-jre-jammy
COPY --from=maven_tool_chain /tmp/target/quote-service-0.0.1-SNAPSHOT.jar /quote-service.jar
CMD ["java", "-jar", "/quote-service.jar"]
