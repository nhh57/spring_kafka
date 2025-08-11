# Project Architecture

This project follows a microservice architecture pattern, with distinct producer and consumer services communicating via Apache Kafka. Key architectural aspects include:

*   **Multi-project Gradle build:** Facilitates independent development and deployment of services.
*   **Kafka as message broker:** Decouples producer and consumer, enabling asynchronous communication and scalability.
*   **Console-based interaction:** Simple command-line interfaces for demonstration purposes.
*   **Docker Compose:** Used for local development environment setup, ensuring consistency and ease of deployment for Kafka and Zookeeper.