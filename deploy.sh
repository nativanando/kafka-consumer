#!/bin/bash

set -e

OPTION=$1
PRODUCER_PATH=~/eclipse-workspace/kafkaProducer/

KAFKA_CONNECT_ADDRESS=kafka:29092

CASSANDRA_CONNECT_PORT=9042
CASSANDRA_CONNECT_ADDRESS=cassandra
CASSANDRA_KEYSPACE=time_series_raw_data
CASSANDRA_SCHEMA_ACTION=CREATE_IF_NOT_EXISTS
CASSANDRA_SCHEMA_NAME=data
CASSANDRA_CLUSTER_NAME=cassandra_cluster

deployBroker() {
    mvn clean package
    docker-compose up -d
}

updateProducerApp() {
    cd $PRODUCER_PATH && mvn clean package
}

removeTests() {
    docker-compose down && docker system prune --volumes -f && docker rmi lasse/broker:0.0.1 && docker rmi lasse/producer:0.0.1
}

stopTests() {
    docker-compose stop
}

restartTests() {
    docker-compose restart
}

case $OPTION in
    --up) deployBroker ;;
    --down) removeTests ;;
    --stop) stopTests ;;
    --restart) restartTests ;;
    --update-producer) updateProducerApp ;;
    *) echo "Expression not found" ;;
esac        
