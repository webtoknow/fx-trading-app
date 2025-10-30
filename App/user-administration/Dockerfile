FROM maven:3.8.8-eclipse-temurin-21-alpine AS maven_tool_chain
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Pprod -DskipTests

FROM openjdk:21-jdk
COPY --from=maven_tool_chain /tmp/target/user-administration-0.0.1-SNAPSHOT.jar /user-admin.jar
CMD /usr/java/openjdk-21/bin/java -Daplication-secret=secret -jar /user-admin.jar