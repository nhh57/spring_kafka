---
title: Định nghĩa Bean ConsumerFactory
type: task
status: completed
created: 2025-08-15T02:54:39
updated: 2025-08-15T03:08:44
id: TASK-KAFKA-M5-S3-002
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S3-001]
tags: [spring-kafka, consumer, configuration, bean]
---

# TASK-KAFKA-M5-S3-002: Định nghĩa Bean ConsumerFactory

## Description
Nhiệm vụ này liên quan đến việc định nghĩa bean `ConsumerFactory` trong lớp `KafkaConsumerConfig`. `ConsumerFactory` chịu trách nhiệm tạo các thể hiện của client Kafka `Consumer` gốc, và cấu hình của nó quyết định hành vi của tất cả các consumer được tạo từ đó.

## Objectives
- Triển khai phương thức `consumerFactory()` trong `KafkaConsumerConfig`.
- Cấu hình các thuộc tính consumer thiết yếu, bao gồm các bộ giải tuần tự hóa, `group.id` và `enable.auto.commit`.

## Checklist
- [x] Định nghĩa bean `ConsumerFactory` trong `KafkaConsumerConfig`.
    - Tệp: `src/main/java/com/example/kafka/config/KafkaConsumerConfig.java`
    - Phương thức: `consumerFactory()`
    - Lớp: `DefaultKafkaConsumerFactory`
    - Ghi chú: Bean này định nghĩa cách Kafka Consumer được tạo. Cấu hình các bộ giải tuần tự hóa (`key.deserializer`, `value.deserializer`) và `group.id`.
    - Thực hành tốt nhất:
        - Đặt `ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG` sử dụng giá trị từ `application.yml`.
        - Đặt `ConsumerConfig.GROUP_ID_CONFIG` sử dụng giá trị từ `application.yml`.
        - Đặt `ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG` (ví dụ: `StringDeserializer.class`).
        - Đặt `ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG` (ví dụ: `StringDeserializer.class`).
        - **QUAN TRỌNG:** Đặt `ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG` thành `false` để quản lý offset thủ công trong môi trường sản xuất. Điều này cung cấp quyền kiểm soát chính xác thời điểm offset được commit, ngăn chặn mất dữ liệu hoặc trùng lặp.
        - Đặt `ConsumerConfig.AUTO_OFFSET_RESET_CONFIG` (ví dụ: `"earliest"` hoặc `"latest"`) để xác định hành vi khi không tìm thấy offset ban đầu.
    - Lỗi thường gặp: Dựa vào `enable.auto.commit=true` có thể dẫn đến mất dữ liệu nếu ứng dụng gặp sự cố trước khi quá trình xử lý hoàn tất. Các bộ giải tuần tự hóa không chính xác sẽ ngăn tin nhắn được đọc đúng cách. Cấu hình sai `auto.offset.reset` có thể gây ra việc xử lý lại hoặc bỏ qua tin nhắn khi consumer khởi động.

## Progress
- Triển khai Kafka Consumer: [x]

## Dependencies
- TASK-KAFKA-M5-S3-001: Tạo Lớp Cấu hình Consumer

## Key Considerations
- **Vai trò của `ConsumerFactory`:** Đây là một thành phần cơ bản trong Spring Kafka, trừu tượng hóa việc tạo các thể hiện của Kafka Consumer.
- **Giải tuần tự hóa:** Chọn `Deserializer` chính xác là rất quan trọng. Nó phải khớp với `Serializer` được sử dụng bởi producer.
- **Quản lý Offset (`enable.auto.commit`, `auto.offset.reset`):** Đây là những yếu tố quan trọng để đảm bảo tiêu thụ tin nhắn đáng tin cậy. Commit thủ công mang lại quyền kiểm soát và độ tin cậy cao nhất.

## Notes
- Đây là nơi định nghĩa cấu hình cốt lõi cho hành vi tiêu thụ tin nhắn, bao gồm độ tin cậy và quản lý offset.

## Discussion
Cấu hình đúng đắn `ConsumerFactory` là tối quan trọng để đảm bảo tin nhắn được tiêu thụ một cách đáng tin cậy và hiệu quả từ Kafka. Nó ảnh hưởng trực tiếp đến tính toàn vẹn dữ liệu và khả năng chịu lỗi của ứng dụng consumer.

## Next Steps
Sau khi định nghĩa `ConsumerFactory`, bước tiếp theo là định nghĩa bean `ConcurrentKafkaListenerContainerFactory`, bean này sẽ sử dụng factory này và quản lý các thể hiện `@KafkaListener`.

## Current Status
Hoàn thành.