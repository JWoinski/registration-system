FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ./target/registrationSystem-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar" ,"/registrationSystem-0.0.1-SNAPSHOT.jar"]