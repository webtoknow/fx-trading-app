FROM openjdk:11.0.4-jre
COPY ./target/user-administration-0.0.1-SNAPSHOT.jar /user-admin.jar
CMD /usr/local/openjdk-11/bin/java -Daplication-secret=secret -jar /user-admin.jar