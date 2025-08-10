# Codebase Structure

The project is structured to facilitate learning and practical application of Kafka concepts. Key directories and their purposes include:

- **`.kilocode/`**: Contains internal rules and guidelines for Kilo Code's operation within this project.
- **`.serena/`**: Serena-specific configuration files for project activation and management.
- **`docs/`**: Comprehensive documentation for Kafka learning and coding plans, including best practices and common pitfalls.
  - **`docs/kafka_coding_plan.md`**: Outlines practical coding exercises and examples for each Kafka module.
  - **`docs/kafka_learning_plan.md`**: Provides a detailed theoretical teaching plan for Kafka.
  - **`docs/setup_guides/`**: Contains guides for setting up the development environment, e.g., `docker_compose_install.md`.
  - **`docs/technical_design/`**: Houses technical design documents (TDDs) for Kafka modules, e.g., `kafka_module1_coding_tdd.md`, `kafka_module2_coding_tdd.md`.
- **`docker-compose.yml`**: Defines the Docker services (e.g., Kafka, Zookeeper) for local development environments.

Future code implementations (Java applications, Spring Boot services, etc.) are expected to reside in standard Maven/Gradle project structures (e.g., `src/main/java`, `src/test/java`) once development begins.