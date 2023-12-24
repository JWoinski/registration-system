FROM openjdk:21
ADD target/registrationSystem-0.0.1-SNAPSHOT.jar .
EXPOSE 8000
CMD java -jar registrationSystem-0.0.1-SNAPSHOT.jar