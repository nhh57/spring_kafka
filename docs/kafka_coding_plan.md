# Kế hoạch Giảng dạy Kafka Thực hành (Code)

Đây là kế hoạch giảng dạy code chi tiết, bổ sung cho Kế hoạch Giảng dạy Kafka Chuyên sâu lý thuyết ([`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md)). Mục tiêu là giúp bạn áp dụng các kiến thức lý thuyết vào thực tế thông qua các ví dụ code cụ thể và bài tập thực hành.

## Cấu trúc tổng quan

Mỗi module thực hành sẽ tương ứng với một module lý thuyết trong `docs/kafka_learning_plan.md` và sẽ bao gồm:
*   **Mục tiêu thực hành**: Nắm vững kỹ năng code cụ thể.
*   **Môi trường thiết lập**: Hướng dẫn thiết lập môi trường phát triển (nếu cần).
*   **Ví dụ code**: Các đoạn code mẫu minh họa các khái niệm và API.
*   **Bài tập thực hành**: Các thử thách nhỏ để củng cố kiến thức.

## Module 1: Kafka Fundamentals - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-1-kafka-fundamentals-nền-tảng-của-kafka)

### 1.1 Thiết lập môi trường Kafka cục bộ
*   **Mục tiêu**: Khởi chạy một cụm Kafka đơn giản (hoặc multi-broker) trên máy cục bộ.
*   **Công cụ**: Docker và Docker Compose.
*   **Ví dụ code/Hướng dẫn**:
    *   File `docker-compose.yml` để khởi động Zookeeper và Kafka Broker.
    *   Hướng dẫn chạy các lệnh Kafka CLI cơ bản (`kafka-topics.sh`, `kafka-console-producer.sh`, `kafka-console-consumer.sh`).

### 1.2 Hiểu các khái niệm cốt lõi qua CLI
*   **Mục tiêu**: Trực quan hóa Messages, Topics, Partitions, Brokers qua các lệnh CLI.
*   **Ví dụ code/Hướng dẫn**:
    *   Sử dụng `kafka-topics.sh --describe` để xem thông tin phân vùng và leader/follower.
    *   Gửi tin nhắn với key để thấy chúng được gửi đến cùng một phân vùng.

## Module 2: Kafka Architecture & Components Deep Dive - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-2-kafka-architecture--components-deep-dive-đi-sâu-vào-kiến-trúc--thành-phần-kafka)

### 2.1 Xây dựng `producer-service`
*   **Mục tiêu**: Xây dựng một microservice độc lập có khả năng gửi tin nhắn đến Kafka, sử dụng Kafka Producer API của Java.
*   **Môi trường thiết lập**: Module `producer-service` trong dự án Gradle.
*   **Ví dụ code**:
    *   `MessageProducerService.java`: Đóng gói logic tạo và cấu hình `KafkaProducer`.
    *   `ProducerApp.java`: Ứng dụng chính, cho phép người dùng nhập và gửi tin nhắn từ console.

### 2.2 Cấu hình Producer và đảm bảo phân phối (Acks, Retries, Idempotence)
*   **Mục tiêu**: Trải nghiệm ảnh hưởng của các cấu hình Producer đến độ bền và hiệu suất.
*   **Ví dụ code**:
    *   Xem lại các cấu hình `acks`, `retries`, `enable.idempotence` trong `MessageProducerService.java`.

### 2.3 Xây dựng `consumer-service`
*   **Mục tiêu**: Xây dựng một microservice độc lập có khả năng tiêu thụ tin nhắn từ Kafka, sử dụng Kafka Consumer API của Java.
*   **Môi trường thiết lập**: Module `consumer-service` trong dự án Gradle.
*   **Ví dụ code**:
    *   `MessageConsumerService.java`: Đóng gói logic tạo `KafkaConsumer`, chạy trong một thread riêng và xử lý tin nhắn.
    *   `ConsumerApp.java`: Ứng dụng chính để khởi chạy consumer service.

### 2.4 Quản lý Consumer Group và Offset thủ công
*   **Mục tiêu**: Kiểm soát việc commit offset và hiểu cơ chế rebalance.
*   **Ví dụ code**:
    *   Cấu hình `enable.auto.commit=false`.
    *   Sử dụng `consumer.commitSync()` và `consumer.commitAsync()`.

## Module 3: Kafka APIs Deep Dive - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-3-kafka-apis-deep-dive-đi-sâu-vào-các-api-kafka)

### 3.1 Thực hành Kafka Streams
*   **Mục tiêu**: Xây dựng ứng dụng xử lý luồng đơn giản.
*   **Môi trường thiết lập**: Module `kafka-streams-example` trong dự án Gradle.
*   **Ví dụ code**:
    *   `kafka-streams-example/src/main/java/com/example/kafka/streams/SimpleStreamProcessor.java`
    *   `kafka-streams-example/src/test/java/com/example/kafka/streams/SimpleStreamProcessorTest.java` (Unit tests)
*   **Hướng dẫn chạy**:
    1.  Đảm bảo môi trường Kafka cục bộ đang chạy (`docker-compose up -d`).
    2.  Tạo topic đầu vào (`input-topic`) và đầu ra (`output-topic-counts`):
        ```bash
        docker-compose exec kafka1 kafka-topics --create --topic input-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
        docker-compose exec kafka1 kafka-topics --create --topic output-topic-counts --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
        ```
    3.  Chạy ứng dụng Kafka Streams:
        ```bash
        ./gradlew :kafka-streams-example:run
        ```
    4.  Sử dụng console producer để gửi tin nhắn vào `input-topic`:
        ```bash
        docker-compose exec kafka1 kafka-console-producer --topic input-topic --bootstrap-server localhost:9092
        # Gõ các tin nhắn, ví dụ:
        # hello world
        # this is an important message
        # another important one
        # regular message
        ```
    5.  Sử dụng console consumer để xem kết quả từ `output-topic-counts`:
        ```bash
        docker-compose exec kafka1 kafka-console-consumer --topic output-topic-counts --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
        ```

### 3.2 Thực hành Kafka Connect
*   **Mục tiêu**: Sử dụng các Kafka Connectors có sẵn để di chuyển dữ liệu.
*   **Môi trường thiết lập**: Cài đặt Kafka Connect (xem hướng dẫn tại [`docs/setup_guides/kafka_connect_setup.md`](docs/setup_guides/kafka_connect_setup.md)).
*   **Ví dụ code/Hướng dẫn**:
    *   **Source Connector (`FileStreamSourceConnector`)**:
        1.  Tạo file `source.txt` trong thư mục gốc của dự án: [`source.txt`](source.txt)
        2.  Tạo file cấu hình Connector: [`kafka-connect-examples/configs/file-source-connector.properties`](kafka-connect-examples/configs/file-source-connector.properties)
        3.  Chạy Kafka Connect (từ thư mục cài đặt Kafka của bạn, ví dụ `/opt/kafka` hoặc `D:\kafka` trên Windows):
            ```bash
            # Trên Linux/macOS
            bin/connect-standalone.sh config/connect-standalone.properties /path/to/your/project/kafka-connect-examples/configs/file-source-connector.properties

            # Trên Windows (sử dụng PowerShell hoặc Command Prompt)
            .\bin\windows\connect-standalone.bat .\config\connect-standalone.properties .\kafka-connect-examples\configs\file-source-connector.properties
            ```
        4.  Kiểm tra tin nhắn trong `input-topic` bằng console consumer:
            ```bash
            docker-compose exec kafka1 kafka-console-consumer --topic input-topic --from-beginning --bootstrap-server localhost:9092
            ```
    *   **Sink Connector (`FileStreamSinkConnector`)**:
        1.  Tạo file cấu hình Connector: [`kafka-connect-examples/configs/file-sink-connector.properties`](kafka-connect-examples/configs/file-sink-connector.properties)
        2.  Chạy Kafka Connect (trong một terminal khác):
            ```bash
            # Trên Linux/macOS
            bin/connect-standalone.sh config/connect-standalone.properties /path/to/your/project/kafka-connect-examples/configs/file-sink-connector.properties

            # Trên Windows (sử dụng PowerShell hoặc Command Prompt)
            .\bin\windows\connect-standalone.bat .\config\connect-standalone.properties .\kafka-connect-examples\configs\file-sink-connector.properties
            ```
        3.  Kiểm tra nội dung của file `sink.txt` được tạo ra trong thư mục cài đặt Kafka Connect của bạn (ví dụ: `D:\kafka\sink.txt`).
    *   **Single Message Transforms (SMTs)**:
        1.  Cập nhật file cấu hình Source Connector: [`kafka-connect-examples/configs/file-source-connector.properties`](kafka-connect-examples/configs/file-source-connector.properties) (đã có SMT `InsertTimestamp`).
        2.  Chạy lại Source Connector với cấu hình đã cập nhật.
        3.  Kiểm tra tin nhắn trong `input-topic` bằng console consumer để thấy trường `recordTimestamp` đã được thêm vào tin nhắn.
            ```bash
            docker-compose exec kafka1 kafka-console-consumer --topic input-topic --from-beginning --bootstrap-server localhost:9092 --property print.timestamp=true --property print.key=true --property print.value=true
            ```

## Module 4: Advanced Kafka Concepts & Operations - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-4-advanced-kafka-concepts--operations-các-khái-niệm-nâng-cao--vận-hành-kafka)

### 4.1 Thực hành Serialization/Deserialization (Serdes) với Avro
*   **Mục tiêu**: Sử dụng Avro và Schema Registry để quản lý Schema.
*   **Môi trường thiết lập**: Cài đặt Schema Registry, dự án Java với `avro` và `kafka-avro-serializer` dependencies.
*   **Ví dụ code**:
    *   Định nghĩa Avro Schema (`.avsc` file).
    *   Tạo Avro Producer và Consumer.
    *   Cấu hình Producer/Consumer để tích hợp với Schema Registry.

### 4.2 Thực hành bảo mật Kafka (SSL/TLS, SASL)
*   **Mục tiêu**: Cấu hình Producer/Consumer để kết nối với cụm Kafka bảo mật.
*   **Môi trường thiết lập**: Cụm Kafka với SSL/TLS và/hoặc SASL được kích hoạt.
*   **Ví dụ code**:
    *   Cấu hình Producer/Consumer với các thuộc tính SSL/TLS (truststore, keystore).
    *   Cấu hình Producer/Consumer với các thuộc tính SASL (username, password, mechanism).

## Module 5: Spring for Apache Kafka - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-5-spring-for-apache-kafka-spring-cho-apache-kafka)

### 5.1 Thiết lập dự án Spring Boot với Spring Kafka
*   **Mục tiêu**: Tạo một dự án Spring Boot cơ bản tích hợp Spring Kafka.
*   **Môi trường thiết lập**: Spring Initializr, Maven/Gradle.
*   **Ví dụ code**:
    *   `pom.xml` hoặc `build.gradle` với `spring-kafka` dependency.
    *   `application.yml` hoặc `application.properties` với cấu hình Kafka cơ bản.

### 5.2 Sử dụng KafkaTemplate để gửi tin nhắn
*   **Mục tiêu**: Gửi tin nhắn đến Kafka bằng `KafkaTemplate`.
*   **Ví dụ code**:
    *   Inject `KafkaTemplate`.
    *   Sử dụng `kafkaTemplate.send()` để gửi tin nhắn.
    *   Xử lý kết quả gửi (lắng nghe `ListenableFuture`).

### 5.3 Sử dụng @KafkaListener để tiêu thụ tin nhắn
*   **Mục tiêu**: Tiêu thụ tin nhắn bằng annotation `@KafkaListener`.
*   **Ví dụ code**:
    *   Tạo một class với phương thức được chú thích `@KafkaListener`.
    *   Cấu hình `group.id` và `topics`.

### 5.4 Xử lý lỗi trong Spring Kafka
*   **Mục tiêu**: Triển khai các chiến lược xử lý lỗi mạnh mẽ.
*   **Ví dụ code**:
    *   Cấu hình `DefaultErrorHandler` với `DeadLetterPublishingRecoverer` và `FixedBackOff`/`ExponentialBackOff`.
    *   Mô phỏng lỗi trong consumer và quan sát tin nhắn được gửi đến DLT.

## Module 6: Real-world Applications & Best Practices - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](../docs/kafka_learning_plan.md#module-6-real-world-applications--best-practices-ứng-dụng-thực-tế--các-phương-pháp-hay-nhất)

### 6.1 Thực hành Event Sourcing đơn giản
*   **Mục tiêu**: Xây dựng một ví dụ nhỏ về Event Sourcing.
*   **Ví dụ code**:
    *   Định nghĩa các `Event` (ví dụ: `OrderCreatedEvent`, `OrderUpdatedEvent`).
    *   Tạo một `Aggregate` nhận `Command` và publish `Event` vào Kafka.
    *   Xây dựng một `Projection` (Materialized View) từ các event để tạo ra trạng thái hiện tại.

### 6.2 Kiểm thử ứng dụng Kafka
*   **Mục tiêu**: Viết unit và integration test cho Producer/Consumer.
*   **Môi trường thiết lập**: JUnit 5, Mockito, Spring Kafka Test (Embedded Kafka).
*   **Ví dụ code**:
    *   Unit test cho Producer/Consumer logic.
    *   Integration test sử dụng `EmbeddedKafkaBroker` để kiểm tra luồng dữ liệu end-to-end trong môi trường test.