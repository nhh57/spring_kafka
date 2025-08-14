---
title: Viết integration test cho Avro Serdes với Schema Registry
type: task
status: planned
created: 2025-08-12T14:09:23
updated: 2025-08-12T14:22:31
id: TASK-KAFKA-M4-S4-014
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER, TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER]
tags: [kafka, avro, testing, integration-test, schema-registry, testcontainers]
---

## Mô tả

Nhiệm vụ này liên quan đến việc viết các integration test để xác minh chức năng đầu cuối của tuần tự hóa và giải tuần tự hóa Avro với broker Kafka và Schema Registry. Các kiểm thử này sẽ mô phỏng một kịch bản thế giới thực bằng cách khởi tạo các thể hiện Kafka và Schema Registry thực tế (ví dụ: sử dụng Testcontainers) và sau đó gửi và tiêu thụ tin nhắn Avro để đảm bảo luồng dữ liệu và quản lý schema phù hợp.

## Mục tiêu

*   Thiết lập môi trường kiểm thử bao gồm một broker Kafka và một Confluent Schema Registry.
*   Triển khai các integration test để gửi tin nhắn Avro thông qua `AvroUserProducer`.
*   Triển khai các integration test để tiêu thụ và giải tuần tự hóa tin nhắn Avro thông qua `AvroUserConsumer`.
*   Xác minh rằng các tin nhắn được tuần tự hóa chính xác, lưu trữ trong Kafka và giải tuần tự hóa trở lại thành các đối tượng Avro.
*   Kiểm thử các tương tác cơ bản của schema registry (ví dụ: đăng ký schema).

## Danh sách kiểm tra

### Kiểm thử

- [ ] Tạo một lớp kiểm thử mới, ví dụ: `src/test/java/com/example/kafka/avro/AvroSerdesIntegrationTest.java`.
    - Ghi chú: Lớp này sẽ chứa các integration test.
- [ ] **Thiết lập môi trường kiểm thử:**
    - [ ] Sử dụng Testcontainers để khởi tạo một broker Kafka và một Confluent Schema Registry.
    - [ ] Cấu hình Producer và Consumer trong kiểm thử để kết nối với các thể hiện Testcontainer này.
    - Ghi chú: Testcontainers cung cấp một môi trường cô lập và có thể tái tạo để kiểm thử tích hợp.
- [ ] **Triển khai kiểm thử Producer:**
    - [ ] Khởi tạo `AvroUserProducer` với các cấu hình dành riêng cho kiểm thử (ví dụ: địa chỉ broker Kafka của Testcontainers và URL Schema Registry).
    - [ ] Tạo một đối tượng `User` Avro.
    - [ ] Gửi đối tượng `User` đến một topic kiểm thử bằng cách sử dụng producer.
    - [ ] Khẳng định rằng tin nhắn đã được gửi thành công (ví dụ: bằng cách kiểm tra `RecordMetadata` từ `Future`).
    - Ghi chú: Tập trung vào việc xác minh khả năng tuần tự hóa và gửi tin nhắn Avro của producer.
- [ ] **Triển khai kiểm thử Consumer:**
    - [ ] Khởi tạo `AvroUserConsumer` với các cấu hình dành riêng cho kiểm thử.
    - [ ] Đăng ký consumer với topic kiểm thử.
    - [ ] Thăm dò tin nhắn.
    - [ ] Khẳng định rằng tin nhắn nhận được có thể được giải tuần tự hóa thành đối tượng `User` Avro chính xác.
    - [ ] Xác minh nội dung của đối tượng `User` đã giải tuần tự hóa khớp với đối tượng gốc đã gửi.
    - Ghi chú: Điều này xác minh khả năng giải tuần tự hóa tin nhắn Avro của consumer.
- [ ] **Kiểm thử luồng đầu cuối:**
    - [ ] Kết hợp logic producer và consumer trong một trường hợp kiểm thử duy nhất để mô phỏng một luồng tin nhắn đầu cuối hoàn chỉnh.
    - [ ] Gửi một tin nhắn, sau đó tiêu thụ ngay lập tức và xác minh.
    - Ghi chú: Kiểm thử này cung cấp độ tin cậy cao nhất trong thiết lập Avro Serdes tổng thể.
- [ ] **Kiểm thử tương tác Schema Registry (Tùy chọn):**
    - [ ] Sau khi gửi tin nhắn, truy vấn client Schema Registry để xác minh rằng schema cho đối tượng `User` đã được đăng ký.
    - Ghi chú: Điều này xác nhận rằng `KafkaAvroSerializer` đang tương tác chính xác với Schema Registry.

## Tiến độ

### Kiểm thử

- [ ] Cấu trúc integration test đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Yêu cầu triển khai `AvroUserProducer`.
*   `TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER`: Yêu cầu triển khai `AvroUserConsumer`.

## Các cân nhắc chính

*   **Testcontainers:** Đảm bảo các phụ thuộc `testcontainers` và `testcontainers-kafka` được thêm vào phạm vi kiểm thử.
*   **Quản lý tài nguyên:** Đảm bảo Testcontainers được dừng đúng cách sau khi kiểm thử hoàn tất để giải phóng tài nguyên. Sử dụng chú thích `@Container` với JUnit 5.
*   **Thời gian:** Kafka là không đồng bộ. Sử dụng các thư viện như Awaitility hoặc các cơ chế tương tự để đợi tin nhắn có sẵn trong consumer trước khi khẳng định.
*   **Topic kiểm thử:** Sử dụng các topic kiểm thử duy nhất cho mỗi lần chạy kiểm thử để tránh nhiễu giữa các kiểm thử.

## Ghi chú

*   **Embedded Kafka:** Mặc dù Testcontainers thường được ưu tiên cho các integration test, Spring Kafka cũng cung cấp `EmbeddedKafkaBroker` cho các thể hiện Kafka trong bộ nhớ nhẹ hơn, đây có thể là một lựa chọn thay thế tùy thuộc vào phạm vi.
*   **Gỡ lỗi:** Các integration test có thể phức tạp hơn để gỡ lỗi. Đảm bảo ghi log được cấu hình đúng cách cho cả ứng dụng kiểm thử và Testcontainers.

## Thảo luận

Integration testing rất quan trọng để xác thực toàn bộ pipeline dữ liệu, bao gồm các tương tác với các hệ thống bên ngoài như Kafka và Schema Registry. Bằng cách mô phỏng các kịch bản thế giới thực, các kiểm thử này cung cấp độ tin cậy cao vào tính đúng đắn và mạnh mẽ của việc triển khai Avro Serdes. Nhiệm vụ này nhấn mạnh tầm quan trọng của một chiến lược kiểm thử toàn diện ngoài các unit test.

## Các bước tiếp theo

Sau khi xác minh chức năng Avro Serdes, bước tiếp theo sẽ là viết các integration test cho các kết nối Kafka an toàn (SSL/TLS và SASL).

## Trạng thái hiện tại

Viết integration test cho Avro Serdes với Schema Registry đã được lên kế hoạch.