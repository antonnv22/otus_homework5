FROM openjdk:11-slim

ADD build/libs/profileservice-0.0.1.jar profileservice.jar
EXPOSE 8000/tcp
ENTRYPOINT java -jar $JAVA_OPTS profileservice.jar