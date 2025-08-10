---
title: Xây dựng Kafka Consumer cơ bản bằng Java
type: task
status: planned
created: 2025-08-10T12:26:35
updated: 2025-08-10T12:26:35
id: TASK-KAFKA-M2-US3-BASIC-CONSUMER
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M1-US1-SETUP-ENV]
tags: [kafka, consumer, java, client]
---

# Xây dựng Kafka Consumer cơ bản bằng Java

## Description
Là một người học, tôi muốn có thể xây dựng một ứng dụng Java Consumer cơ bản để tiêu thụ tin nhắn từ Kafka.

## Objectives
*   Thiết lập dự án Java (Maven/Gradle) với `kafka-clients` dependency.
*   Khởi tạo `KafkaConsumer` với các cấu hình cần thiết.
*   Subscribe vào một topic và poll tin nhắn.

## Checklist
### Java Consumer Setup
- [x] Thiết lập dự án Java (Gradle) và thêm `org.apache.kafka:kafka-clients` dependency.
    - **File Location**: `project_root/build.gradle` (nếu là project mới) hoặc sử dụng project từ `TASK-KAFKA-M2-US1-BASIC-PRODUCER`.
    - **Notes**: Nếu sử dụng lại project cũ, đảm bảo `build.gradle` đã có dependency và cấu hình Java 21. Nếu là project mới, hướng dẫn tương tự như `TASK-KAFKA-M2-US1-BASIC-PRODUCER`.
    ```gradle
    // ... (các phần khác của build.gradle)
    dependencies {
        implementation 'org.apache.kafka:kafka-clients:3.0.0' // Hoặc phiên bản tương thích
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
    ```
    - **Considerations**: Đảm bảo phiên bản `kafka-clients` tương thích với Kafka Broker và JDK 21.
    - **Best Practices**: Luôn sử dụng cùng một phiên bản dependency trong toàn bộ project để tránh xung đột.
    - **Common Pitfalls**: Lỗi dependency, lỗi tương thích phiên bản Java/Kafka.
- [x] Viết code để khởi tạo `KafkaConsumer`.
    - **File Location**: `src/main/java/com/example/kafka/BasicConsumer.java` (trong phương thức `main`).
    - **Class Name**: `BasicConsumer`
    - **Notes**: Hướng dẫn tạo một `java.util.Properties` object chứa các cấu hình như `bootstrap.servers` (địa chỉ của Kafka Broker, ví dụ: `localhost:9092`), `group.id` (ID của consumer group, ví dụ: `my-consumer-group`), `key.deserializer` và `value.deserializer` (phải khớp với serializer của Producer, ví dụ: `org.apache.kafka.common.serialization.StringDeserializer.class.getName()`).
    ```java
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "my-consumer-group");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    // Thêm khai báo logger
    private static final Logger log = LoggerFactory.getLogger(BasicConsumer.class);
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    ```
    - **Considerations**: Nhấn mạnh `group.id` là bắt buộc cho consumer để Kafka có thể quản lý trạng thái của nó trong consumer group. Giải thích rằng deserializer phải khớp với serializer của producer để tránh lỗi giải tuần tự hóa.
    - **Best Practices**: Luôn đóng Consumer khi không sử dụng (`consumer.close()`) để giải phóng tài nguyên và đảm bảo consumer thoát khỏi group một cách duyên dáng, tránh gây ra rebalance không cần thiết. Sử dụng `try-with-resources` hoặc `finally` block.
    - **Common Pitfalls**: Sai địa chỉ `bootstrap.servers`, quên hoặc sai tên lớp deserializer (dẫn đến lỗi giải tuần tự hóa), không đóng consumer gây rò rỉ tài nguyên hoặc ảnh hưởng đến rebalance.
- [x] Viết code để subscribe vào một topic.
    - **File Location**: `src/main/java/com/example/kafka/BasicConsumer.java`
    - **Method**: `consumer.subscribe(Collection<String> topics)`
    ```java
    String topic = "my-first-topic"; // Hoặc topic đã tạo từ Module 1
    consumer.subscribe(Collections.singletonList(topic));
    log.info("Subscribed to topic: {}", topic);
    ```
    - **Considerations**: Giải thích rằng consumer có thể subscribe vào một hoặc nhiều topic. Khi subscribe, consumer sẽ tham gia vào một consumer group.
    - **Best Practices**: Đảm bảo tên topic chính xác. Sử dụng `Collections.singletonList` cho một topic, `Arrays.asList` cho nhiều topic.
    - **Common Pitfalls**: Sai tên topic, không subscribe trước khi poll, hoặc subscribe vào một topic không tồn tại.
- [x] Viết code để poll tin nhắn trong một vòng lặp.
    - **File Location**: `src/main/java/com/example/kafka/BasicConsumer.java`
    - **Method**: `consumer.poll(Duration timeout)`
    ```java
    while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // Poll every 100ms
        for (ConsumerRecord<String, String> record : records) {
            log.info("Received message: partition = {}, offset = {}, key = {}, value = {}",
                     record.partition(), record.offset(), record.key(), record.value());
        }
        // Commit offset (sẽ được làm chi tiết hơn ở US4)
    }
    ```
    - **Considerations**: Giải thích rằng `poll()` là phương thức kéo (pull) tin nhắn từ broker. `Duration.ofMillis(100)` là thời gian timeout tối đa mà `poll()` sẽ chờ nếu không có tin nhắn ngay lập tức. `poll()` sẽ trả về một `ConsumerRecords` object chứa tất cả các tin nhắn đã nhận được trong khoảng thời gian poll. Nếu không có tin nhắn, nó sẽ trả về một `ConsumerRecords` trống.
    - **Best Practices**: Luôn có một vòng lặp (`while(true)` hoặc tương tự) để liên tục poll tin nhắn. Xử lý từng `ConsumerRecord` trong vòng lặp. Đảm bảo có logic xử lý khi không có tin nhắn để tránh lãng phí CPU.
    - **Common Pitfalls**: Quên vòng lặp poll (chỉ đọc được một lần), không xử lý từng `ConsumerRecord` đúng cách, xử lý logic quá nặng trong vòng lặp poll mà không có commit offset phù hợp.

## Progress
*   **Java Consumer Setup**: [ ]

## Dependencies
*   TASK-KAFKA-M1-US1-SETUP-ENV (Môi trường Kafka cục bộ đã được thiết lập và chạy).
*   Java Development Kit (JDK) đã được cài đặt.
*   Maven hoặc Gradle đã được cài đặt.

## Key Considerations
*   **Mô hình Consumer/Consumer Group**: Consumer API minh họa cách các consumer hoạt động trong một nhóm để chia sẻ tải.
*   **Poll Mechanism**: Hiểu rõ cơ chế poll tin nhắn và cách nó khác với push.

## Notes
*   Giải thích vai trò của deserializer trong việc chuyển đổi byte array thành đối tượng Java.
*   Nhấn mạnh tầm quan trọng của `group.id` trong việc quản lý tiến độ tiêu thụ.

## Discussion
Phần này sẽ đặt nền móng cho việc lập trình Consumer bằng Java, cho phép người học tạo ra các ứng dụng tiêu thụ dữ liệu một cách linh hoạt và hiệu quả.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có khả năng viết các ứng dụng Kafka Consumer cơ bản.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho Kafka Consumer cơ bản bằng Java.