---
title: Triển khai Unit Test Consumer
type: task
status: completed
created: 2025-08-15T02:55:34
updated: 2025-08-15T03:10:24
id: TASK-KAFKA-M5-S5-002
priority: medium
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S3-004]
tags: [spring-kafka, testing, unit-test, consumer]
---

# TASK-KAFKA-M5-S5-002: Triển khai Unit Test Consumer

## Description
Nhiệm vụ này liên quan đến việc tạo các unit test cho `MessageConsumerService`. Các unit test sẽ tập trung vào việc xác minh logic xử lý tin nhắn trong dịch vụ, sử dụng mocking cho bất kỳ dependency bên ngoài nào và cho đối tượng `Acknowledgment`.

## Objectives
- Tạo `MessageConsumerServiceTest.java`.
- Viết các unit test để xác minh hành vi của phương thức `listen`, bao gồm các kịch bản xử lý thành công và lỗi.

## Checklist
- [x] Triển khai các unit test cho logic dịch vụ consumer.
    - Tệp: `src/test/java/com/example/kafka/consumer/MessageConsumerServiceTest.java`
    - Chú thích: `@ExtendWith(MockitoExtension.class)`
    - Ghi chú: Gọi trực tiếp phương thức `listen` với `Acknowledgment` mock và nội dung tin nhắn. Mock bất kỳ dependency nào mà `MessageConsumerService` có thể có (ví dụ: các dịch vụ khác mà nó gọi). Sử dụng `Mockito.verify()` để khẳng định rằng `acknowledgment.acknowledge()` được gọi khi thành công. Sử dụng `Assertions.assertThrows()` cho các trường hợp lỗi.
    - Thực hành tốt nhất: Tập trung vào kiểm thử logic nghiệp vụ trong consumer. Cô lập test khỏi cơ sở hạ tầng consumer Kafka thực tế.

## Progress
- Triển khai Kế hoạch Kiểm thử: [x]

## Dependencies
- TASK-KAFKA-M5-S3-004: Tạo Dịch vụ Tiêu thụ Tin nhắn

## Key Considerations
- **Kiểm thử `Acknowledgment`:** Điều quan trọng là phải xác minh rằng `acknowledgment.acknowledge()` được gọi khi xử lý thành công, vì điều này liên quan trực tiếp đến việc commit offset.
- **Mô phỏng lỗi:** Đối với unit test, bạn có thể mô phỏng lỗi bằng cách làm cho các dependency được mock ném ra exception để kiểm thử đường dẫn xử lý lỗi của consumer (trước khi `DefaultErrorHandler` tiếp quản).

## Notes
- Các test này xác nhận rằng dịch vụ consumer xử lý tin nhắn đúng cách và xử lý việc commit offset thủ công như mong đợi.

## Discussion
Unit test cho dịch vụ consumer đảm bảo rằng logic xử lý tin nhắn cốt lõi là hợp lý, độc lập với cơ sở hạ tầng Kafka. Điều này đặc biệt quan trọng đối với logic nghiệp vụ phức tạp.

## Next Steps
Sau khi unit test cả producer và consumer, các bước tiếp theo sẽ liên quan đến việc triển khai các test tích hợp để xác minh luồng end-to-end với một Kafka broker nhúng.

## Current Status
Hoàn thành.