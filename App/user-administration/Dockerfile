FROM maven:3.9.8-eclipse-temurin-21 AS maven_tool_chain
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM eclipse-temurin:21-jre-jammy
COPY --from=maven_tool_chain /tmp/target/user-administration-0.0.1-SNAPSHOT.jar /user-admin.jar
CMD ["java", "-Daplication-secret=secret", "-jar", "/user-admin.jar"]
