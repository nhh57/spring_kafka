# Kafka Learning Project: Producer/Consumer Services

This project demonstrates a basic, interactive producer-consumer setup using Apache Kafka. It has been structured as a multi-project Gradle build with two distinct microservices:
*   `producer-service`: An interactive console application that sends messages to a Kafka topic.
*   `consumer-service`: A background service that listens to the topic and prints the messages it receives.

## Prerequisites

Before you begin, ensure you have the following installed on your Windows machine:

*   **Docker Desktop**: To run the Kafka and Zookeeper environment.
*   **Java Development Kit (JDK) 21**: To compile and run the Java applications.
*   **Git**: To clone the repository.
*   **Windows Terminal or PowerShell**: For running the commands.

## How to Build and Run the Project

Follow these steps to get the project running on your local machine.

### Step 1: Start the Kafka Environment

The project includes a `docker-compose.yml` file to easily start Kafka, Zookeeper, and a helpful Kafka UI.

1.  Open a terminal (PowerShell or Command Prompt).
2.  Navigate to the root directory of the project.
3.  Run the following command to start the services in the background:

    ```bash
    docker-compose up -d
    ```
    This will start the necessary containers. You can verify they are running with `docker ps`.

### Step 2: Accessing the Kafka UI

You can monitor the Kafka cluster through the web interface.

1.  Open your web browser and navigate to **[http://localhost:9080](http://localhost:9080)**.
2.  Here you can view brokers, topics, messages, and consumer groups.

### Step 3: Build the Services

This project uses the Gradle wrapper (`gradlew.bat`). The following command will build both the `producer-service` and `consumer-service`.

1.  In your terminal, at the root of the project, run:

    ```bash
    .\gradlew.bat build
    ```

### Step 4: Run the Services

You will need **two separate terminals** to run the producer and consumer services so you can see them interact in real-time.

#### Terminal 1: Run the Consumer Service

1.  Open a new terminal.
2.  Run the following command to start the `consumer-service`. It will begin listening for messages immediately.

    ```bash
    .\gradlew.bat :consumer-service:run
    ```

#### Terminal 2: Run the Producer Service

1.  Open a second terminal.
2.  Run the following command to start the interactive `producer-service`.

    ```bash
    .\gradlew.bat :producer-service:run
    ```

3.  The application will wait for your input. Type any message at the `message>` prompt and press Enter.
4.  Observe the logs in **Terminal 1** (the consumer). You will see the message you just sent appear almost instantly.
5.  To stop the producer, type `exit` and press Enter. To stop the consumer, press `Ctrl+C` in its terminal.

### Step 5: Clean Up

Once you are finished, you can stop and remove the Docker containers with the following command from the project root:

```bash
docker-compose down