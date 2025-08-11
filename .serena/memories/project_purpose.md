# Project Purpose

This project demonstrates a basic, interactive producer-consumer setup using Apache Kafka. It is structured as a multi-project Gradle build with two distinct microservices:

*   `producer-service`: An interactive console application that sends messages to a Kafka topic.
*   `consumer-service`: A background service that listens to the topic and prints the messages it receives.