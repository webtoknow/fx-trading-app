FROM openjdk:11.0.4-jre
COPY ./target/fx-trading-0.0.1-SNAPSHOT.jar /trading-service.jar
CMD /usr/local/openjdk-11/bin/java -Dspring.profiles.active=prod -jar /trading-service.jar