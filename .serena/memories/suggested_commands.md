# Suggested Commands

## Kafka Environment
*   Start Kafka environment: `docker-compose up -d`
*   Stop and remove Kafka environment: `docker-compose down`
*   Access Kafka UI: [http://localhost:9080](http://localhost:9080)

## Build and Run Services
*   Build all services: `.\gradlew.bat build` (Windows) or `./gradlew build` (Linux/macOS)
*   Run consumer service: `.\gradlew.bat :consumer-service:run` (Windows) or `./gradlew :consumer-service:run` (Linux/macOS)
*   Run producer service: `.\gradlew.bat :producer-service:run` (Windows) or `./gradlew :producer-service:run` (Linux/macOS)

## System Utilities (Linux)
*   List files and directories: `ls`, `ls -l`, `ls -la`
*   Change directory: `cd <directory_name>`
*   Search for patterns in files: `grep <pattern> <filename>`
*   Find files: `find . -name "<filename_pattern>"`
*   Version control: `git` commands (e.g., `git status`, `git add`, `git commit`, `git push`)

## Task Completion Commands
*   Linting/Formatting: Not explicitly defined, but standard Java/Gradle best practices should be followed.
*   Testing: `.\gradlew.bat test` (Windows) or `./gradlew test` (Linux/macOS)