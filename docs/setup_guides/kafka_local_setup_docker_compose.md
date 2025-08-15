# Hướng dẫn Thiết lập Môi trường Kafka Cục bộ với Docker Compose cho Spring Kafka

Tài liệu này cung cấp hướng dẫn chi tiết về cách thiết lập một môi trường Kafka cục bộ bằng Docker Compose, lý tưởng để chạy và kiểm thử các ứng dụng Spring for Apache Kafka.

## 1. Yêu cầu

*   **Docker Desktop** (hoặc Docker Engine và Docker Compose) đã được cài đặt trên hệ thống của bạn.
    *   Kiểm tra cài đặt:
        ```bash
        docker --version
        docker-compose --version
        ```

## 2. Tạo tệp `docker-compose.yml`

Tạo một tệp có tên `docker-compose.yml` trong thư mục gốc của dự án của bạn (hoặc một thư mục chuyên dụng cho môi trường Docker của bạn) với nội dung sau:

```yaml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    hostname: zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    hostname: kafka
    container_name: kafka
    ports:
      - "9092:9092"
      - "9093:9093"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_JMX_PORT: 9991
      KAFKA_JMX_HOSTNAME: localhost
    depends_on:
      - zookeeper
```

**Giải thích các cổng:**
*   `2181`: Cổng mặc định cho Zookeeper.
*   `9092`: Cổng nội bộ cho giao tiếp giữa các container Docker (Kafka broker). Các ứng dụng Spring Boot bên trong Docker network có thể sử dụng cổng này.
*   `9093`: Cổng được ánh xạ tới `localhost:9093` trên máy chủ của bạn. Các ứng dụng Spring Boot chạy trực tiếp trên máy chủ cục bộ của bạn sẽ kết nối tới Kafka thông qua cổng này.

## 3. Khởi chạy cụm Kafka

Mở terminal tại thư mục chứa tệp `docker-compose.yml` và chạy lệnh sau:

```bash
docker-compose up -d
```

*   `-d`: Chạy các container ở chế độ nền (detached mode).

Kiểm tra trạng thái của các container:

```bash
docker-compose ps
```

Bạn sẽ thấy `zookeeper` và `kafka` đang chạy.

## 4. Kiểm tra kết nối Kafka

Bạn có thể sử dụng các công cụ CLI của Kafka để kiểm tra kết nối với broker.

### 4.1. Tạo một Topic

```bash
docker-compose exec kafka kafka-topics --create --topic spring-kafka-test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### 4.2. Gửi tin nhắn bằng Console Producer

```bash
docker-compose exec kafka kafka-console-producer --topic spring-kafka-test-topic --bootstrap-server localhost:9092
# Sau đó, bạn có thể nhập tin nhắn và nhấn Enter để gửi.
```

### 4.3. Tiêu thụ tin nhắn bằng Console Consumer

```bash
docker-compose exec kafka kafka-console-consumer --topic spring-kafka-test-topic --from-beginning --bootstrap-server localhost:9092
```

## 5. Cấu hình ứng dụng Spring Boot

Trong tệp `application.yml` của ứng dụng Spring Boot của bạn, hãy đảm bảo rằng `bootstrap-servers` được cấu hình để trỏ đến Kafka broker đang chạy trên Docker:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092 # Hoặc localhost:9093 tùy thuộc vào cách bạn ánh xạ cổng
    consumer:
      group-id: my-spring-app-group
    producer:
      # Cấu hình Producer của bạn
```

## 6. Dừng và Xóa cụm Kafka

Khi bạn hoàn tất, bạn có thể dừng và xóa các container:

```bash
docker-compose down
```

*   `down`: Dừng và xóa các container, network và volumes được định nghĩa trong tệp `docker-compose.yml`.

## 7. Các lưu ý bổ sung

*   **Tên Host:** Trong `docker-compose.yml`, các service được đặt tên `zookeeper` và `kafka`. Trong môi trường Docker network, các service này có thể giao tiếp với nhau bằng tên hostname của chúng (ví dụ: `zookeeper:2181`). Đối với các ứng dụng bên ngoài Docker network (như ứng dụng Spring Boot của bạn chạy trên máy chủ), bạn phải sử dụng `localhost` và cổng được ánh xạ.
*   **Persistent Data:** Cấu hình trên không lưu trữ dữ liệu Kafka một cách bền vững. Nếu bạn dừng và khởi động lại container Kafka, dữ liệu sẽ bị mất. Để dữ liệu tồn tại, bạn cần cấu hình Docker volumes.

Tài liệu này nên cung cấp đủ thông tin để thiết lập một môi trường Kafka cục bộ cơ bản cho mục đích phát triển và kiểm thử Spring Kafka.