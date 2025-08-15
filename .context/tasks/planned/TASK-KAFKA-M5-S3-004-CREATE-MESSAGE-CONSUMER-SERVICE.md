---
title: Tạo Dịch vụ Tiêu thụ Tin nhắn
type: task
status: completed
created: 2025-08-15T02:55:01
updated: 2025-08-15T03:09:29
id: TASK-KAFKA-M5-S3-004
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S3-003]
tags: [spring-kafka, consumer, service]
---

# TASK-KAFKA-M5-S3-004: Tạo Dịch vụ Tiêu thụ Tin nhắn

## Description
Nhiệm vụ này liên quan đến việc tạo một lớp dịch vụ sẽ chứa các phương thức được chú thích bằng `@KafkaListener`. Các phương thức này sẽ chịu trách nhiệm tiêu thụ tin nhắn từ các topic Kafka được chỉ định và xử lý chúng.

## Objectives
- Tạo `MessageConsumerService.java` được chú thích bằng `@Service`.
- Triển khai một phương thức được chú thích bằng `@KafkaListener` để tiêu thụ tin nhắn.
- Đảm bảo việc commit offset thủ công được xử lý trong phương thức listener.

## Checklist
- [x] Tạo một lớp dịch vụ với các phương thức `@KafkaListener`.
    - Tệp: `src/main/java/com/example/kafka/consumer/MessageConsumerService.java`
    - Lớp: `MessageConsumerService`
    - Chú thích: `@Service`
    - Ghi chú: Lớp này sẽ chứa logic xử lý tin nhắn thực tế.
- [x] Triển khai một phương thức trong `MessageConsumerService` được chú thích bằng `@KafkaListener`.
    - Tệp: `src/main/java/com/example/kafka/consumer/MessageConsumerService.java`
    - Phương thức: `listen(String message, Acknowledgment acknowledgment)`
    - Chú thích: `@KafkaListener(topics = "my-topic", groupId = "spring-kafka-group", containerFactory = "kafkaListenerContainerFactory")`
    - Ghi chú:
        - `topics` chỉ định topic Kafka để tiêu thụ.
        - `groupId` phải khớp với cái được cấu hình trong `application.yml` và `ConsumerFactory`.
        - `containerFactory` liên kết rõ ràng listener này với bean `ConcurrentKafkaListenerContainerFactory`.
        - `Acknowledgment` rất quan trọng cho việc commit offset thủ công.
    - Thực hành tốt nhất:
        - Gọi `acknowledgment.acknowledge()` sau khi xử lý thành công để commit offset thủ công. Điều này đảm bảo offset chỉ được commit nếu tin nhắn được xử lý hoàn toàn, ngăn ngừa mất dữ liệu hoặc xử lý lại.
        - Giữ các phương thức listener gọn gàng; ủy quyền logic nghiệp vụ phức tạp cho các dịch vụ hoặc phương thức trợ giúp khác.
    - Lỗi thường gặp: Quên gọi `acknowledgment.acknowledge()` khi `enable.auto.commit` là `false` sẽ khiến tin nhắn được xử lý lại liên tục, dẫn đến các vòng lặp vô hạn và cạn kiệt tài nguyên.

## Progress
- Triển khai Kafka Consumer: [x]

## Dependencies
- TASK-KAFKA-M5-S3-003: Định nghĩa Bean ConcurrentKafkaListenerContainerFactory

## Key Considerations
- **`@KafkaListener`:** Chú thích này là cốt lõi của chức năng consumer của Spring Kafka, cho phép tiêu thụ tin nhắn khai báo.
- **Quản lý Offset thủ công:** Việc commit offset rõ ràng cung cấp quyền kiểm soát mạnh mẽ đối với các đảm bảo xử lý tin nhắn (ví dụ: ít nhất một lần, chính xác một lần với giao dịch). Đây là một thực hành tốt nhất quan trọng cho các ứng dụng sản xuất.

## Notes
- Dịch vụ này là nơi logic nghiệp vụ của ứng dụng để xử lý tin nhắn Kafka sẽ nằm.

## Discussion
Một dịch vụ consumer chuyên dụng với các phương thức `@KafkaListener` cung cấp một cách rõ ràng, theo kiểu Spring để xử lý các tin nhắn Kafka đến. Việc nhấn mạnh vào quản lý offset thủ công đảm bảo độ tin cậy cao.

## Next Steps
Với việc triển khai producer và consumer cơ bản, tập hợp các tác vụ tiếp theo sẽ tập trung vào việc triển khai xử lý lỗi mạnh mẽ cho consumer.

## Current Status
Hoàn thành.