# Design Patterns and Guidelines

This project serves as a learning platform for Kafka, demonstrating fundamental concepts. It aims to showcase practical implementation of Kafka Producer and Consumer APIs in Java.

Key areas covered and implicitly following best practices:

*   **Microservices Architecture:** Separation of concerns into `producer-service` and `consumer-service`.
*   **Kafka API Usage:** Direct interaction with `org.apache.kafka:kafka-clients` for message production and consumption.
*   **Logging:** Integration with SLF4J and Logback for effective application logging.
*   **Testing:** Use of JUnit for unit testing.

The `docs/kafka_coding_plan.md` outlines further practical exercises and best practices related to Kafka, including:

*   Kafka Producer configurations (acks, retries, idempotence).
*   Manual Consumer Group and Offset management.
*   Kafka Streams and Kafka Connect usage.
*   Serialization/Deserialization with Avro and Schema Registry.
*   Kafka Security (SSL/TLS, SASL).
*   Spring for Apache Kafka (KafkaTemplate, @KafkaListener, Error Handling).
*   Event Sourcing principles.
*   Testing Kafka applications (unit and integration tests with Embedded Kafka).

These topics suggest an adherence to modern Kafka development practices and patterns, focusing on reliability, scalability, and maintainability.