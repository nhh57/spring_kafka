---
title: Xây dựng Kafka Producer cơ bản bằng Java
type: task
status: planned
created: 2025-08-10T12:25:49
updated: 2025-08-10T12:25:49
id: TASK-KAFKA-M2-US1-BASIC-PRODUCER
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M1-US1-SETUP-ENV]
tags: [kafka, producer, java, client]
---

# Xây dựng Kafka Producer cơ bản bằng Java

## Description
Là một người học, tôi muốn có thể xây dựng một ứng dụng Java Producer cơ bản để gửi tin nhắn đến Kafka.

## Objectives
*   Thiết lập dự án Java (Maven/Gradle) với `kafka-clients` dependency.
*   Khởi tạo `KafkaProducer` với các cấu hình cần thiết.
*   Gửi tin nhắn đồng bộ và bất đồng bộ đến một topic Kafka.

## Checklist
### Java Producer Setup
- [x] Thiết lập dự án Java (Gradle) và thêm `org.apache.kafka:kafka-clients` dependency.
    - **File Location**: `project_root/build.gradle`
    - **Notes**: Hướng dẫn tạo project Java cơ bản bằng Gradle (ví dụ: `gradle init --type java-application --dsl groovy --test-framework junit-jupiter --package com.example.kafka --project-name basic-producer`). Cung cấp đoạn code `build.gradle` với `org.apache.kafka:kafka-clients` dependency. Đảm bảo `java` plugin được áp dụng và `targetCompatibility` là `JavaVersion.VERSION_21`. Thêm dependency cho SLF4J và Logback.
    ```gradle
    plugins {
        id 'java'
    }
    group 'com.example.kafka'
    version '1.0-SNAPSHOT'
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation 'org.apache.kafka:kafka-clients:3.0.0' // Hoặc phiên bản tương thích với Kafka 7.0.1
        implementation 'org.slf4j:slf4j-api:2.0.7' // SLF4J API
        runtimeOnly 'ch.qos.logback:logback-classic:1.4.11' // Logback implementation
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
    ```
    - **Considerations**: Đảm bảo phiên bản `kafka-clients` tương thích với phiên bản Kafka Broker đã cài đặt ở Module 1 (ví dụ: Kafka Broker 7.0.1, `kafka-clients` 3.0.0).
    - **Best Practices**: Luôn sử dụng phiên bản dependency ổn định và được kiểm chứng. Sử dụng Gradle Wrapper để đảm bảo môi trường build nhất quán.
    - **Common Pitfalls**: Lỗi dependency do sai `groupId/artifactId/version`, lỗi tương thích phiên bản Java/Kafka (ví dụ: dùng JDK cũ hơn 21), không khai báo đúng repository, thiếu logging implementation.
- [x] Viết code để khởi tạo `KafkaProducer`.
    - **File Location**: `src/main/java/com/example/kafka/BasicProducer.java` (trong phương thức `main` hoặc một phương thức khởi tạo).
    - **Class Name**: `BasicProducer`
    - **Notes**: Tạo một `java.util.Properties` object để chứa các cấu hình Producer. Các cấu hình tối thiểu cần thiết là `bootstrap.servers` (địa chỉ của Kafka Broker, ví dụ: `localhost:9092`), `key.serializer` và `value.serializer` (ví dụ: `org.apache.kafka.common.serialization.StringSerializer.class.getName()`). Thêm khai báo `private static final Logger log = LoggerFactory.getLogger(BasicProducer.class);` ở đầu class.
    ```java
    import org.apache.kafka.clients.producer.KafkaProducer;
    import org.apache.kafka.clients.producer.ProducerRecord;
    import org.apache.kafka.clients.producer.RecordMetadata;
    import org.apache.kafka.clients.producer.Callback;
    import org.apache.kafka.common.serialization.StringSerializer;
    import java.util.Properties;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class BasicProducer {
        private static final Logger log = LoggerFactory.getLogger(BasicProducer.class);

        public static void main(String[] args) {
            Properties props = new Properties();
            props.put("bootstrap.servers", "localhost:9092");
            props.put("key.serializer", StringSerializer.class.getName());
            props.put("value.serializer", StringSerializer.class.getName());
            KafkaProducer<String, String> producer = new KafkaProducer<>(props);
            // ... (code gửi tin nhắn)
            producer.close();
        }
    }
    ```
    - **Considerations**: Giải thích rằng `key.serializer` và `value.serializer` là bắt buộc vì Kafka chỉ làm việc với byte arrays. Nhấn mạnh tầm quan trọng của việc cấu hình đúng `bootstrap.servers` để Producer có thể tìm thấy broker.
    - **Best Practices**: Luôn đóng Producer khi không sử dụng (`producer.close()`) để giải phóng tài nguyên và đảm bảo tất cả các tin nhắn đã được gửi. Sử dụng `try-with-resources` hoặc `finally` block để đảm bảo đóng Producer.
    - **Common Pitfalls**: Sai địa chỉ `bootstrap.servers`, quên hoặc sai tên lớp serializer (dẫn đến lỗi tuần tự hóa), không đóng producer gây rò rỉ tài nguyên hoặc dữ liệu không được gửi hết.
- [x] Viết code để gửi tin nhắn đồng bộ (synchronous send).
    - **File Location**: `src/main/java/com/example/kafka/BasicProducer.java` (trong phương thức `main` hoặc một phương thức gửi tin nhắn).
    - **Method**: `producer.send(new ProducerRecord<>("topic_name", "message_value")).get()`
    ```java
    try {
        String topic = "my-sync-topic";
        String message = "Hello, Kafka (sync)!";
        RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, message)).get();
        log.info("Sent synchronously: {} to topic {}, partition {}, offset {}", message, metadata.topic(), metadata.partition(), metadata.offset());
    } catch (Exception e) {
        log.error("Error sending message synchronously", e);
    }
    ```
    - **Considerations**: Giải thích rằng phương thức `get()` sẽ chặn luồng chính cho đến khi nhận được xác nhận từ broker hoặc một ngoại lệ xảy ra.
    - **Best Practices**: Chỉ sử dụng gửi đồng bộ cho các trường hợp cần đảm bảo ngay lập tức rằng tin nhắn đã được ghi nhận hoặc khi thông lượng không phải là yếu tố quan trọng.
    - **Common Pitfalls**: Bị chặn quá lâu nếu broker không phản hồi hoặc có vấn đề mạng, dẫn đến timeout và lỗi.
- [x] Viết code để gửi tin nhắn bất đồng bộ (asynchronous send) với callback.
    - **File Location**: `src/main/java/com/example/kafka/BasicProducer.java` (trong phương thức `main` hoặc một phương thức gửi tin nhắn).
    - **Method**: `producer.send(new ProducerRecord<>("topic_name", "key", "message_value"), Callback callback)`
    - **Interface Implementation**: `org.apache.kafka.clients.producer.Callback`
    ```java
    String topic = "my-async-topic";
    String message = "Hello, Kafka (async)!";
    producer.send(new ProducerRecord<>(topic, "key1", message), (metadata, exception) -> {
        if (exception == null) {
            log.info("Sent asynchronously: {} to topic {}, partition {}, offset {}", message, metadata.topic(), metadata.partition(), metadata.offset());
        } else {
            log.error("Error sending message asynchronously", exception);
        }
    });
    ```
    - **Considerations**: Giải thích rằng gửi bất đồng bộ không chặn luồng chính, cho phép Producer tiếp tục gửi tin nhắn khác trong khi chờ phản hồi. Kết quả gửi (metadata hoặc lỗi) sẽ được xử lý trong callback.
    - **Best Practices**: Luôn xử lý lỗi trong callback để đảm bảo tin nhắn được xử lý đúng cách (ví dụ: ghi log lỗi, thử lại thủ công) hoặc ghi log thành công. Điều này rất quan trọng để tránh mất tin nhắn mà không được thông báo.
    - **Common Pitfalls**: Quên xử lý lỗi trong callback (dẫn đến tin nhắn bị mất mà không được biết), xử lý logic nặng trong callback (ảnh hưởng đến hiệu suất của Producer).

## Progress
*   **Java Producer Setup**: [ ]

## Dependencies
*   TASK-KAFKA-M1-US1-SETUP-ENV (Môi trường Kafka cục bộ đã được thiết lập và chạy).
*   Java Development Kit (JDK) đã được cài đặt.
*   Maven hoặc Gradle đã được cài đặt.

## Key Considerations
*   **Kafka Client API**: Đây là nền tảng cho mọi ứng dụng Kafka bằng Java. Việc hiểu rõ cách sử dụng Producer API là rất quan trọng.
*   **Đồng bộ vs Bất đồng bộ**: Hiểu sự khác biệt và khi nào nên sử dụng từng phương pháp gửi tin nhắn.

## Notes
*   Giải thích vai trò của serializer trong việc chuyển đổi đối tượng Java thành byte array để gửi qua mạng.
*   Nhấn mạnh tầm quan trọng của việc xử lý lỗi và log trong callback của gửi bất đồng bộ.

## Discussion
Phần này sẽ đặt nền móng cho việc lập trình Producer bằng Java, cho phép người học tạo ra các ứng dụng gửi dữ liệu một cách linh hoạt và hiệu quả.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có khả năng viết các ứng dụng Kafka Producer cơ bản.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho Kafka Producer cơ bản bằng Java.