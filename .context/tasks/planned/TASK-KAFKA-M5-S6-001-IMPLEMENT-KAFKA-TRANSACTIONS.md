---
title: Triển khai Ví dụ Kafka Transactions trong Spring Kafka
type: task
status: completed
created: 2025-08-15T03:36:40
updated: 2025-08-15T03:38:55
id: TASK-KAFKA-M5-S6-001
priority: high
memory_types: [procedural]
dependencies: []
tags: [spring-kafka, transactions, exactly-once]
---

# TASK-KAFKA-M5-S6-001: Triển khai Ví dụ Kafka Transactions trong Spring Kafka

## Description
Nhiệm vụ này nhằm mục đích cung cấp một ví dụ thực tế về cách triển khai Kafka Transactions trong ứng dụng Spring Boot sử dụng Spring Kafka. Kafka Transactions đảm bảo ngữ nghĩa "exactly-once" (chính xác một lần) khi gửi tin nhắn đến Kafka và trong các hoạt động tiêu thụ-chuyển đổi-sản xuất (consume-transform-produce - CTP).

## Objectives
- Cấu hình Kafka Producer cho các giao dịch.
- Triển khai một ví dụ về Producer giao dịch.
- Triển khai một ví dụ về giao dịch CTP (Consumer-Transform-Produce).
- Đảm bảo các giao dịch được xử lý đúng cách, bao gồm cả rollback khi có lỗi.

## Checklist
- [x] Cấu hình `ProducerFactory` để hỗ trợ giao dịch.
    - Tệp: `spring-kafka-module5-example/src/main/java/com/example/kafka/config/KafkaProducerConfig.java`
    - Ghi chú: Thêm thuộc tính `transactional.id` vào `ProducerFactory`.
- [x] Triển khai một Producer giao dịch đơn giản.
    - Tệp: `spring-kafka-module5-example/src/main/java/com/example/kafka/producer/TransactionalProducerService.java`
    - Ghi chú: Sử dụng `KafkaTemplate.executeInTransaction()` hoặc `@Transactional` với `KafkaTemplate`.
- [x] Cấu hình Consumer để đọc các tin nhắn đã commit (read_committed).
    - Tệp: `spring-kafka-module5-example/src/main/java/com/example/kafka/config/KafkaConsumerConfig.java`
    - Ghi chú: Đặt `isolation.level` của Consumer thành `read_committed`.
- [x] Triển khai một ví dụ CTP (Consumer-Transform-Produce) giao dịch.
    - Tệp: `spring-kafka-module5-example/src/main/java/com/example/kafka/consumer/TransactionalCtoPService.java`
    - Ghi chú: Một consumer đọc từ một topic, xử lý/chuyển đổi tin nhắn, và sau đó gửi tin nhắn mới đến một topic khác trong cùng một giao dịch.
- [x] Viết các test tích hợp để xác minh hành vi giao dịch.
    - Tệp: `spring-kafka-module5-example/src/test/java/com/example/kafka/integration/KafkaTransactionIntegrationTest.java`
    - Ghi chú: Kiểm tra các kịch bản thành công và thất bại để đảm bảo tin nhắn chỉ được commit khi toàn bộ giao dịch thành công.

## Progress
- Triển khai Kafka Transactions: [x]

## Dependencies
- Các cấu hình Producer và Consumer cơ bản đã được thiết lập.

## Key Considerations
- **Ngữ nghĩa Exactly-Once:** Kafka Transactions là cách chính để đạt được ngữ nghĩa chính xác một lần, đảm bảo rằng tin nhắn không bị mất hoặc trùng lặp ngay cả khi có lỗi hệ thống hoặc ứng dụng.
- **`transactional.id`:** Đây là một ID duy nhất cho mỗi Producer giao dịch, được Kafka sử dụng để phục hồi trạng thái giao dịch.
- **`isolation.level=read_committed`:** Consumer phải được cấu hình để chỉ đọc các tin nhắn đã được commit thành công, bỏ qua các tin nhắn trong các giao dịch chưa hoàn thành hoặc đã bị hủy.

## Notes
- Triển khai giao dịch có thể phức tạp và yêu cầu hiểu biết sâu sắc về cơ chế của Kafka.
- Cần có một Kafka broker hỗ trợ giao dịch (Kafka 0.11.0 trở lên).

## Discussion
Việc bao gồm Kafka Transactions sẽ nâng cao đáng kể giá trị của module, cung cấp cho người học kiến thức cần thiết để xây dựng các ứng dụng Kafka đáng tin cậy cho các trường hợp sử dụng quan trọng.

## Next Steps
Sau khi triển khai ví dụ giao dịch, cần cập nhật tài liệu chính để phản ánh việc bổ sung này.

## Current Status
Hoàn thành.