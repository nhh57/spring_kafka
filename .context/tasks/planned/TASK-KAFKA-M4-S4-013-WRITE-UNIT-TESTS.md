---
title: Viết unit test cho logic Producer/Consumer Avro
type: task
status: planned
created: 2025-08-12T14:09:02
updated: 2025-08-12T14:21:59
id: TASK-KAFKA-M4-S4-013
priority: medium
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER, TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER]
tags: [kafka, avro, testing, unit-test, java, junit, mockito]
---

## Mô tả

Nhiệm vụ này tập trung vào việc viết unit test cho logic cốt lõi của các dịch vụ Kafka Producer và Consumer Avro. Unit test đảm bảo rằng các thành phần riêng lẻ hoạt động chính xác một cách độc lập, không phụ thuộc vào các phụ thuộc bên ngoài như các broker Kafka thực tế hoặc Schema Registry.

## Mục tiêu

*   Tạo unit test cho `AvroUserProducer` để xác minh logic gửi tin nhắn của nó.
*   Tạo unit test cho `AvroUserConsumer` để xác minh logic xử lý tin nhắn và quản lý offset của nó.
*   Sử dụng các framework mocking (ví dụ: Mockito) để cô lập mã đang được kiểm thử khỏi các phụ thuộc Kafka bên ngoài.
*   Đảm bảo bao phủ kiểm thử cho các chức năng chính.

## Danh sách kiểm tra

### Kiểm thử

- [ ] Tạo một lớp kiểm thử mới cho Avro Producer, ví dụ: `src/test/java/com/example/kafka/avro/producer/AvroUserProducerTest.java`.
    - Ghi chú: Lớp này sẽ chứa các kiểm thử cho `AvroUserProducer`.
- [ ] Triển khai unit test cho `AvroUserProducer`:
    - [ ] Kiểm thử phương thức `sendUserMessage`.
    - [ ] Mock `KafkaProducer` để xác minh các lệnh gọi phương thức `send()` với các đối số `ProducerRecord` chính xác.
    - [ ] Xác minh rằng `Callback` được xử lý chính xác cho cả kịch bản thành công và thất bại (ví dụ: bằng cách bắt `ProducerRecord` và các đối số `Callback`).
    - Ghi chú: Tập trung vào tương tác với `KafkaProducer` được mock và logic callback.
- [ ] Tạo một lớp kiểm thử mới cho Avro Consumer, ví dụ: `src/test/java/com/example/kafka/avro/consumer/AvroUserConsumerTest.java`.
    - Ghi chú: Lớp này sẽ chứa các kiểm thử cho `AvroUserConsumer`.
- [ ] Triển khai unit test cho `AvroUserConsumer`:
    - [ ] Kiểm thử logic `run()` hoặc thăm dò.
    - [ ] Mock `KafkaConsumer` để mô phỏng việc nhận `ConsumerRecords`.
    - [ ] Xác minh rằng các tin nhắn được giải tuần tự hóa chính xác và logic xử lý được gọi.
    - [ ] Xác minh rằng `commitSync()` hoặc `commitAsync()` được gọi vào đúng thời điểm.
    - [ ] Kiểm thử xử lý lỗi trong vòng lặp xử lý tin nhắn của consumer.
    - Ghi chú: Tập trung vào logic nội bộ của consumer để thăm dò, xử lý và commit offset.

### Phụ thuộc

- [ ] Đảm bảo `junit-jupiter-api`, `junit-jupiter-engine` và `mockito-core` (hoặc các framework kiểm thử tương đương) có trong các phụ thuộc kiểm thử của dự án.
    - Ghi chú: JUnit cung cấp framework kiểm thử, và Mockito cho phép tạo các đối tượng mock để cô lập các đơn vị mã.

## Tiến độ

### Kiểm thử

- [ ] Cấu trúc unit test đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Cung cấp lớp Producer để kiểm thử.
*   `TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER`: Cung cấp lớp Consumer để kiểm thử.

## Các cân nhắc chính

*   **Cô lập:** Mục tiêu chính của unit testing là kiểm thử một đơn vị mã duy nhất một cách cô lập. Việc mocking các phụ thuộc bên ngoài là rất cần thiết để đạt được điều này.
*   **Dữ liệu kiểm thử:** Sử dụng dữ liệu kiểm thử thực tế nhưng đơn giản (đối tượng `User` Avro) để đảm bảo các kiểm thử phù hợp và dễ hiểu.
*   **Các trường hợp biên:** Cân nhắc kiểm thử các trường hợp biên, chẳng hạn như tin nhắn trống, tin nhắn bị định dạng sai (nếu áp dụng cho xử lý lỗi) hoặc tin nhắn rất lớn (nếu hiệu suất là một mối quan tâm đối với unit testing).
*   **Các lớp được tạo:** Khi mocking, bạn sẽ tương tác trực tiếp với lớp Avro `User` được tạo.

## Ghi chú

*   **Mockito:** Sử dụng `Mockito.mock()`, `when()` và `verify()` để kiểm soát hành vi của các đối tượng được mock và xác nhận các tương tác.
*   **`@BeforeEach` và `@AfterEach`:** Sử dụng các chú thích JUnit này để thiết lập và dọn dẹp môi trường kiểm thử cho mỗi phương thức kiểm thử.

## Thảo luận

Unit testing là một thực hành nền tảng để đảm bảo chất lượng và độ tin cậy của mã. Bằng cách kiểm thử kỹ lưỡng logic Producer và Consumer Avro một cách cô lập, chúng ta có thể phát hiện lỗi sớm trong chu kỳ phát triển, giảm thời gian gỡ lỗi và xây dựng niềm tin vào tính đúng đắn của việc triển khai tuần tự hóa và giải tuần tự hóa của chúng ta. Nhiệm vụ này nhấn mạnh tầm quan trọng của một bộ kiểm thử được cấu trúc tốt cho logic nghiệp vụ cốt lõi.

## Các bước tiếp theo

Sau khi các unit test được triển khai, bước tiếp theo sẽ là viết các integration test để xác minh luồng đầu cuối với các broker Kafka và Schema Registry thực tế.

## Trạng thái hiện tại

Viết unit test cho logic Producer/Consumer Avro đã được lên kế hoạch.