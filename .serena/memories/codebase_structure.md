# Codebase Structure

The project is a multi-project Gradle build with two main sub-projects:

*   **`producer-service/`**: Contains the Kafka producer application.
    *   `src/main/java/com/example/kafka/producer/MessageProducerService.java`: Handles sending messages to Kafka.
    *   `src/main/java/com/example/kafka/producer/ProducerApp.java`: Main application entry point for the producer.
*   **`consumer-service/`**: Contains the Kafka consumer application.
    *   `src/main/java/com/example/kafka/consumer/MessageConsumerService.java`: Handles consuming messages from Kafka.
    *   `src/main/java/com/example/kafka/consumer/ConsumerApp.java`: Main application entry point for the consumer.

Shared build configuration is in the root `build.gradle` file, and sub-project inclusion is in `settings.gradle`.