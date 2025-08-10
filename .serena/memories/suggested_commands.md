# Suggested Commands

This section lists common commands used in this project.

## General Commands
- `ls`: List directory contents.
- `pwd`: Print name of current working directory.
- `cd <directory>`: Change the shell working directory.
- `grep <pattern> <file>`: Search for patterns in each file.
- `find <path> -name "<pattern>"`: Search for files in a directory hierarchy.
- `cat <file>`: Concatenate and print files.
- `head <file>`: Output the first part of files.
- `mvn clean install`: Compile, test, and package the project (if using Maven).
- `docker-compose up -d`: Start Kafka and Zookeeper (or other services) in detached mode.
- `docker-compose down`: Stop and remove containers, networks, images, and volumes.

## Kafka CLI Tools
- `kafka-topics.sh --bootstrap-server localhost:9092 --create --topic <topic-name> --partitions <num-partitions> --replication-factor <num-replication>`: Create a Kafka topic.
- `kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic <topic-name>`: Describe a Kafka topic.
- `kafka-console-producer.sh --broker-list localhost:9092 --topic <topic-name>`: Start a console producer.
- `kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning`: Start a console consumer.

## Project Specific Commands
- **To run a Spring Boot application**: Use your IDE's run configuration or `mvn spring-boot:run` (if using Maven).
- **To run tests**: `mvn test` (if using Maven).
