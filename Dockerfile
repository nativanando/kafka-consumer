FROM openjdk:8-jdk-alpine
MAINTAINER Fernando Natividade Luiz - fernando.luiz@pti.org.br

RUN apk update && apk add bash
RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY target/kafkaConsumer-0.0.1-SNAPSHOT.jar broker.jar
ENTRYPOINT ["java","-jar","broker.jar"]
