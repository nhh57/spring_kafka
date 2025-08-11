# Project Architecture Summary

This document provides a high-level overview of the `kafka-learning-project` architecture for Serena.

## Overview

The project is a multi-project Gradle build designed to teach Apache Kafka concepts through a practical, microservice-style application. It consists of two primary, independent services: a producer and a consumer.

## Modules

### 1. `producer-service`

*   **Purpose**: A standalone Java application that acts as a Kafka producer. It provides an interactive command-line interface for the user to send messages.
*   **Main Class**: `com.example.kafka.producer.ProducerApp`
*   **Core Logic**: Encapsulated in `com.example.kafka.producer.MessageProducerService`.
*   **Functionality**: Reads user input from the console and publishes it as a message to the `learning-topic` in Kafka.

### 2. `consumer-service`

*   **Purpose**: A standalone Java application that acts as a Kafka consumer. It runs as a background service, continuously listening for and processing messages.
*   **Main Class**: `com.example.kafka.consumer.ConsumerApp`
*   **Core Logic**: Encapsulated in `com.example.kafka.consumer.MessageConsumerService`.
*   **Functionality**: Subscribes to the `learning-topic` and logs any messages it receives to the console and a dedicated log file.

## Interaction

The two services are decoupled and communicate asynchronously via the Kafka cluster.
1.  The `consumer-service` is started first and begins listening.
2.  The `producer-service` is then started.
3.  When a message is sent from the `producer-service`, it is published to the `learning-topic`.
4.  The `consumer-service` polls this topic, receives the message, and logs it.

## Environment

The required Kafka and Zookeeper environment, along with a Kafka UI for monitoring, is defined in the root `docker-compose.yml` file and can be started with `docker-compose up -d`.