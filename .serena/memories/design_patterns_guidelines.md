# Design Patterns and Guidelines

This project emphasizes the following design patterns and guidelines:

- **Clean Architecture**: Adherence to the principles of Clean Architecture to ensure separation of concerns, testability, and maintainability.
- **Event-Driven Architecture**: Kafka is central to implementing event-driven patterns for microservices communication and data pipelines.
- **Best Practices for Kafka**: The `docs/best_practices/` directory contains detailed guidelines on various Kafka best practices, including:
  - Producer configurations (`acks=all`, `min.insync.replicas` for critical data).
  - Consumer offset management (avoiding auto-commit without understanding).
  - Topic design (defining clear purpose, avoiding generic topics).
  - Schema management with Schema Registry.
  - Error handling in Kafka Connect and Spring Kafka (using `DeadLetterPublishingRecoverer`).
  - Monitoring Kafka clusters.
  - Strategic use of Kafka Streams for light stream processing and Kafka Connect for data integration.
  - Avoiding manual client implementations when Kafka Connect is suitable.
- **Spring Framework Specifics**: Utilizing Spring Boot, Spring Data JPA, Spring Web, Spring Validation, Spring AOP, and Spring Security (if applicable) as per standard Spring best practices.