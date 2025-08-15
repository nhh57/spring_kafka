---
title: Triển khai Test Tích hợp Kafka
type: task
status: completed
created: 2025-08-15T02:55:44
updated: 2025-08-15T03:10:40
id: TASK-KAFKA-M5-S5-003
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S2-004, TASK-KAFKA-M5-S3-004, TASK-KAFKA-M5-S4-001]
tags: [spring-kafka, testing, integration-test]
---

# TASK-KAFKA-M5-S5-003: Triển khai Test Tích hợp Kafka

## Description
Nhiệm vụ này liên quan đến việc tạo các test tích hợp để xác minh chức năng đầu cuối của ứng dụng Spring Kafka, bao gồm sản xuất tin nhắn, tiêu thụ và xử lý lỗi. Các test này sẽ sử dụng `EmbeddedKafkaBroker` của Spring Kafka để mô phỏng môi trường Kafka thực.

## Objectives
- Tạo `KafkaIntegrationTest.java` được chú thích bằng `@SpringBootTest` và `@EmbeddedKafka`.
- Triển khai các test case để xác minh luồng tin nhắn thành công từ producer đến consumer.
- Triển khai các test case để xác minh xử lý lỗi và chức năng Dead Letter Topic (DLT).

## Checklist
- [x] Tạo một lớp test tích hợp sử dụng `EmbeddedKafkaBroker`.
    - Tệp: `src/test/java/com/example/kafka/integration/KafkaIntegrationTest.java`
    - Chú thích: `@SpringBootTest`, `@EmbeddedKafka(partitions = 1, topics = {"my-topic", "my-topic.DLT"})`
    - Ghi chú: `@EmbeddedKafka` khởi động một Kafka broker cho mục đích kiểm thử, làm cho các test tích hợp tự chứa. Cấu hình các topic mà nó nên tạo.
- [x] Triển khai một test case để xác minh sản xuất và tiêu thụ tin nhắn.
    - Tệp: `src/test/java/com/example/kafka/integration/KafkaIntegrationTest.java`
    - Ghi chú: Inject `KafkaTemplate` và `MessageConsumerService`. Gửi một tin nhắn qua `KafkaTemplate`. Sử dụng `Consumer` trực tiếp (từ `KafkaTestUtils` hoặc một consumer được tạo thủ công) để xác minh việc nhận tin nhắn từ topic. Ngoài ra, đối với xác minh bất đồng bộ, sử dụng `AtomicBoolean` hoặc `CountDownLatch` trong `MessageConsumerService` để báo hiệu việc nhận tin nhắn.
- [x] Triển khai một test case để xác minh xử lý lỗi và chức năng DLT.
    - Tệp: `src/test/java/com/example/kafka/integration/KafkaIntegrationTest.java`
    - Ghi chú:
        - Sửa đổi `MessageConsumerService` (hoặc sử dụng mock/spy) để ném ra một ngoại lệ cho một tin nhắn cụ thể (ví dụ: dựa trên nội dung hoặc khóa).
        - Gửi tin nhắn "xấu" đó bằng `KafkaTemplate`.
        - Sử dụng `Consumer` để thăm dò DLT (`my-topic.DLT`) để xác minh tin nhắn lỗi đã đến đó.
        - Đảm bảo các cơ chế chờ đợi thích hợp (ví dụ: `Awaitility`, `Thread.sleep` cho các trường hợp đơn giản) được áp dụng cho các hoạt động bất đồng bộ.

## Progress
- Triển khai Kế hoạch Kiểm thử: [x]

## Dependencies
- TASK-KAFKA-M5-S2-004: Tạo Dịch vụ Sản xuất Tin nhắn
- TASK-KAFKA-M5-S3-004: Tạo Dịch vụ Tiêu thụ Tin nhắn
- TASK-KAFKA-M5-S4-001: Cấu hình Trình xử lý Lỗi với Dead Letter Topic (DLT)

## Key Considerations
- **`EmbeddedKafkaBroker`:** Điều này rất có giá trị cho test tích hợp, cung cấp một Kafka broker nhẹ, trong bộ nhớ có thể được kiểm soát theo chương trình.
- **Xác minh đầu cuối:** Test tích hợp xác nhận rằng tất cả các thành phần (producer, consumer, trình xử lý lỗi, Kafka) hoạt động cùng nhau như mong đợi.
- **Test bất đồng bộ:** Do bản chất bất đồng bộ của Kafka, các test thường cần chờ các sự kiện xảy ra. Các kỹ thuật như `CountDownLatch` hoặc `Awaitility` là rất cần thiết.

## Notes
- Các test này rất quan trọng để đảm bảo độ tin cậy và tính đúng đắn tổng thể của ứng dụng Spring Kafka trong môi trường gần thực tế.

## Discussion
Test tích hợp với `EmbeddedKafkaBroker` cung cấp mức độ tin cậy cao về chức năng của ứng dụng. Nó bắc cầu giữa các test đơn vị cô lập và các test hệ thống đầy đủ.

## Current Status
Hoàn thành.