FROM openjdk:11-slim

ADD build/libs/gatewayservice-0.0.1.jar gatewayservice.jar
EXPOSE 8002/tcp
ENTRYPOINT java -jar $JAVA_OPTS gatewayservice.jar