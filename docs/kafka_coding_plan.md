# Kế hoạch Giảng dạy Kafka Thực hành (Code)

Đây là kế hoạch giảng dạy code chi tiết, bổ sung cho Kế hoạch Giảng dạy Kafka Chuyên sâu lý thuyết ([`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md)). Mục tiêu là giúp bạn áp dụng các kiến thức lý thuyết vào thực tế thông qua các ví dụ code cụ thể và bài tập thực hành.

## Cấu trúc tổng quan

Mỗi module thực hành sẽ tương ứng với một module lý thuyết trong `docs/kafka_learning_plan.md` và sẽ bao gồm:
*   **Mục tiêu thực hành**: Nắm vững kỹ năng code cụ thể.
*   **Môi trường thiết lập**: Hướng dẫn thiết lập môi trường phát triển (nếu cần).
*   **Ví dụ code**: Các đoạn code mẫu minh họa các khái niệm và API.
*   **Bài tập thực hành**: Các thử thách nhỏ để củng cố kiến thức.

## Module 1: Kafka Fundamentals - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-1-kafka-fundamentals-nền-tảng-của-kafka)

### 1.1 Thiết lập môi trường Kafka cục bộ
*   **Mục tiêu**: Khởi chạy một cụm Kafka đơn giản (hoặc multi-broker) trên máy cục bộ.
*   **Công cụ**: Docker và Docker Compose.
*   **Ví dụ code/Hướng dẫn**:
    *   File `docker-compose.yml` để khởi động Zookeeper và Kafka Broker.
    *   Hướng dẫn chạy các lệnh Kafka CLI cơ bản (`kafka-topics.sh`, `kafka-console-producer.sh`, `kafka-console-consumer.sh`).
*   **Bài tập thực hành**:
    *   Tạo một topic mới với 3 phân vùng và 1 replication factor.
    *   Gửi 5 tin nhắn từ console producer.
    *   Tiêu thụ 5 tin nhắn đó từ console consumer.

### 1.2 Hiểu các khái niệm cốt lõi qua CLI
*   **Mục tiêu**: Trực quan hóa Messages, Topics, Partitions, Brokers qua các lệnh CLI.
*   **Ví dụ code/Hướng dẫn**:
    *   Sử dụng `kafka-topics.sh --describe` để xem thông tin phân vùng và leader/follower.
    *   Gửi tin nhắn với key để thấy chúng được gửi đến cùng một phân vùng.
*   **Bài tập thực hành**:
    *   Tạo một topic mới, gửi tin nhắn với key và quan sát chúng được gửi đến cùng một phân vùng.
    *   Thử tắt một broker và quan sát thay đổi leader (nếu có cụm multi-broker).

## Module 2: Kafka Architecture & Components Deep Dive - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-2-kafka-architecture--components-deep-dive-đi-sâu-vào-kiến-trúc--thành-phần-kafka)

### 2.1 Viết Kafka Producer cơ bản bằng Java
*   **Mục tiêu**: Hiểu cách gửi tin nhắn đến Kafka bằng Kafka Producer API của Java.
*   **Môi trường thiết lập**: Dự án Maven/Gradle với `kafka-clients` dependency.
*   **Ví dụ code**:
    *   Tạo `KafkaProducer` với các cấu hình cơ bản (`bootstrap.servers`, `key.serializer`, `value.serializer`).
    *   Gửi `ProducerRecord` đồng bộ và bất đồng bộ.
*   **Bài tập thực hành**:
    *   Viết một ứng dụng Producer gửi 100 tin nhắn đến một topic.
    *   Đo thời gian gửi tin nhắn và thông lượng.

### 2.2 Cấu hình Producer và đảm bảo phân phối (Acks, Retries, Idempotence)
*   **Mục tiêu**: Trải nghiệm ảnh hưởng của các cấu hình Producer đến độ bền và hiệu suất.
*   **Ví dụ code**:
    *   Thử nghiệm `acks=0`, `acks=1`, `acks=all`.
    *   Bật `enable.idempotence=true` và `retries` để thấy `exactly-once delivery` (trong phạm vi session của Producer).
*   **Bài tập thực hành**:
    *   Chạy Producer với các cấu hình `acks` khác nhau và quan sát độ trễ/thông lượng.
    *   Mô phỏng lỗi broker (ví dụ: tắt broker) và quan sát hành vi với/không có `idempotence` và `retries`.

### 2.3 Viết Kafka Consumer cơ bản bằng Java
*   **Mục tiêu**: Hiểu cách tiêu thụ tin nhắn từ Kafka bằng Kafka Consumer API của Java.
*   **Môi trường thiết lập**: Dự án Maven/Gradle với `kafka-clients` dependency.
*   **Ví dụ code**:
    *   Tạo `KafkaConsumer` với các cấu hình cơ bản (`bootstrap.servers`, `group.id`, `key.deserializer`, `value.deserializer`).
    *   `subscribe` vào một topic và `poll` tin nhắn.
*   **Bài tập thực hành**:
    *   Viết một ứng dụng Consumer tiêu thụ tin nhắn từ topic đã tạo.
    *   Quan sát cách các consumer trong cùng một group chia sẻ phân vùng.

### 2.4 Quản lý Consumer Group và Offset thủ công
*   **Mục tiêu**: Kiểm soát việc commit offset và hiểu cơ chế rebalance.
*   **Ví dụ code**:
    *   Cấu hình `enable.auto.commit=false`.
    *   Sử dụng `consumer.commitSync()` và `consumer.commitAsync()`.
*   **Bài tập thực hành**:
    *   Viết một consumer commit offset thủ công sau khi xử lý một batch tin nhắn.
    *   Mô phỏng consumer crash và quan sát hành vi phục hồi từ offset đã commit.

## Module 3: Kafka APIs Deep Dive - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-3-kafka-apis-deep-dive-đi-sâu-vào-các-api-kafka)

### 3.1 Thực hành Kafka Streams
*   **Mục tiêu**: Xây dựng ứng dụng xử lý luồng đơn giản.
*   **Môi trường thiết lập**: Dự án Maven/Gradle với `kafka-streams` dependency.
*   **Ví dụ code**:
    *   Tạo `StreamsBuilder` và `KStream` từ một topic.
    *   Thực hiện các phép biến đổi như `filter`, `map`, `groupByKey`, `count`.
    *   Ghi kết quả ra một topic mới.
*   **Bài tập thực hành**:
    *   Viết ứng dụng Kafka Streams đọc từ topic đầu vào, lọc các tin nhắn theo điều kiện, chuyển đổi định dạng và ghi ra topic đầu ra.
    *   Thử nghiệm với `windowing` và `aggregation`.

### 3.2 Thực hành Kafka Connect
*   **Mục tiêu**: Sử dụng các Kafka Connectors có sẵn để di chuyển dữ liệu.
*   **Môi trường thiết lập**: Cài đặt Kafka Connect (Standalone/Distributed mode).
*   **Ví dụ code/Hướng dẫn**:
    *   Cấu hình và chạy `FileStreamSourceConnector` để đọc từ file và ghi vào Kafka.
    *   Cấu hình và chạy `FileStreamSinkConnector` để đọc từ Kafka và ghi vào file.
    *   Giới thiệu về SMTs (Single Message Transforms).
*   **Bài tập thực hành**:
    *   Tạo một Source Connector đọc dữ liệu từ một file CSV và publish vào Kafka.
    *   Tạo một Sink Connector đọc dữ liệu từ Kafka và ghi vào một file khác, sử dụng SMT để biến đổi dữ liệu.

## Module 4: Advanced Kafka Concepts & Operations - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-4-advanced-kafka-concepts--operations-các-khái-niệm-nâng-cao--vận-hành-kafka)

### 4.1 Thực hành Serialization/Deserialization (Serdes) với Avro
*   **Mục tiêu**: Sử dụng Avro và Schema Registry để quản lý Schema.
*   **Môi trường thiết lập**: Cài đặt Schema Registry, dự án Java với `avro` và `kafka-avro-serializer` dependencies.
*   **Ví dụ code**:
    *   Định nghĩa Avro Schema (`.avsc` file).
    *   Tạo Avro Producer và Consumer.
    *   Cấu hình Producer/Consumer để tích hợp với Schema Registry.
*   **Bài tập thực hành**:
    *   Gửi và nhận các bản ghi Avro với Schema Registry.
    *   Thử nghiệm Schema Evolution (thêm trường tùy chọn) và quan sát khả năng tương thích ngược.

### 4.2 Thực hành bảo mật Kafka (SSL/TLS, SASL)
*   **Mục tiêu**: Cấu hình Producer/Consumer để kết nối với cụm Kafka bảo mật.
*   **Môi trường thiết lập**: Cụm Kafka với SSL/TLS và/hoặc SASL được kích hoạt.
*   **Ví dụ code**:
    *   Cấu hình Producer/Consumer với các thuộc tính SSL/TLS (truststore, keystore).
    *   Cấu hình Producer/Consumer với các thuộc tính SASL (username, password, mechanism).
*   **Bài tập thực hành**:
    *   Kết nối thành công Producer/Consumer đến một cụm Kafka có bảo mật.

## Module 5: Spring for Apache Kafka - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-5-spring-for-apache-kafka-spring-cho-apache-kafka)

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
*   **Bài tập thực hành**:
    *   Tạo một REST Controller nhận request và gửi tin nhắn vào Kafka thông qua `KafkaTemplate`.

### 5.3 Sử dụng @KafkaListener để tiêu thụ tin nhắn
*   **Mục tiêu**: Tiêu thụ tin nhắn bằng annotation `@KafkaListener`.
*   **Ví dụ code**:
    *   Tạo một class với phương thức được chú thích `@KafkaListener`.
    *   Cấu hình `group.id` và `topics`.
*   **Bài tập thực hành**:
    *   Tạo một `@KafkaListener` đơn giản để nhận tin nhắn và in ra console.
    *   Thử nghiệm với `concurrency` của `@KafkaListener`.

### 5.4 Xử lý lỗi trong Spring Kafka
*   **Mục tiêu**: Triển khai các chiến lược xử lý lỗi mạnh mẽ.
*   **Ví dụ code**:
    *   Cấu hình `DefaultErrorHandler` với `DeadLetterPublishingRecoverer` và `FixedBackOff`/`ExponentialBackOff`.
    *   Mô phỏng lỗi trong consumer và quan sát tin nhắn được gửi đến DLT.
*   **Bài tập thực hành**:
    *   Tạo một consumer gây lỗi và cấu hình DLT để bắt các tin nhắn lỗi.
    *   Kiểm tra nội dung tin nhắn trong DLT.

## Module 6: Real-world Applications & Best Practices - Thực hành

**Liên kết lý thuyết**: [`docs/kafka_learning_plan.md`](docs/kafka_learning_plan.md#module-6-real-world-applications--best-practices-ứng-dụng-thực-tế--các-phương-pháp-hay-nhất)

### 6.1 Thực hành Event Sourcing đơn giản
*   **Mục tiêu**: Xây dựng một ví dụ nhỏ về Event Sourcing.
*   **Ví dụ code**:
    *   Định nghĩa các `Event` (ví dụ: `OrderCreatedEvent`, `OrderUpdatedEvent`).
    *   Tạo một `Aggregate` nhận `Command` và publish `Event` vào Kafka.
    *   Xây dựng một `Projection` (Materialized View) từ các event để tạo ra trạng thái hiện tại.
*   **Bài tập thực hành**:
    *   Triển khai một hệ thống quản lý đơn hàng đơn giản sử dụng Event Sourcing.

### 6.2 Kiểm thử ứng dụng Kafka
*   **Mục tiêu**: Viết unit và integration test cho Producer/Consumer.
*   **Môi trường thiết lập**: JUnit 5, Mockito, Spring Kafka Test (Embedded Kafka).
*   **Ví dụ code**:
    *   Unit test cho Producer/Consumer logic.
    *   Integration test sử dụng `EmbeddedKafkaBroker` để kiểm tra luồng dữ liệu end-to-end trong môi trường test.
*   **Bài tập thực hành**:
    *   Viết test cho các ví dụ code đã tạo ở các module trước.