---
title: Triển khai dịch vụ Producer Kafka Avro
type: task
status: planned
created: 2025-08-12T14:05:40
updated: 2025-08-14T04:39:16
id: TASK-KAFKA-M4-S1-003
priority: high
memory_types: [procedural, semantic]
dependencies: [TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA, TASK-KAFKA-M4-S1-002-CONFIGURE-AVRO-COMPILER]
tags: [kafka, avro, producer, serdes, java]
---

## Mô tả

Nhiệm vụ này liên quan đến việc triển khai một dịch vụ Kafka Producer có thể gửi tin nhắn được tuần tự hóa bằng Avro. Dịch vụ sẽ sử dụng các lớp Java được tạo từ schema Avro (được định nghĩa trong Nhiệm vụ 1 và được biên dịch trong Nhiệm vụ 2) và cấu hình Kafka Producer để tích hợp với Schema Registry.

## Mục tiêu

*   Tạo một lớp Java để đóng gói logic Kafka Producer Avro.
*   Cấu hình thể hiện `KafkaProducer` với các bộ tuần tự hóa Avro và URL Schema Registry phù hợp.
*   Triển khai một phương thức để gửi các đối tượng `User` (lớp Avro được tạo) đến một topic Kafka.
*   Bao gồm các cơ chế xử lý lỗi và xác nhận cơ bản.

## Danh sách kiểm tra

### Producer

- [x] Tạo một lớp Java mới, ví dụ: `src/main/java/com/example/kafka/avro/producer/AvroUserProducer.java`. (Đã hoàn thành)
    - Ghi chú: Lớp này sẽ chứa logic cốt lõi để tạo tin nhắn Avro.
- [x] Khởi tạo `KafkaProducer` với các thuộc tính: (Đã hoàn thành)
    - `bootstrap.servers`: Địa chỉ các broker Kafka.
    - `key.serializer`: `org.apache.kafka.common.serialization.StringSerializer` (giả sử khóa là chuỗi).
    - `value.serializer`: `io.confluent.kafka.serializers.KafkaAvroSerializer`.
    - `schema.registry.url`: URL của Confluent Schema Registry.
    - Ghi chú: Các thuộc tính này rất quan trọng để bật tuần tự hóa Avro và tích hợp Schema Registry.
- [x] Triển khai phương thức `sendUserMessage(User user, String topic)`: (Đã hoàn thành)
    - Tạo một `ProducerRecord` với topic, khóa (ví dụ: `user.getId().toString()`), và đối tượng `User` Avro.
    - Sử dụng `producer.send(record, callback)` để gửi không đồng bộ và xử lý xác nhận.
    - Ghi chú: Gửi không đồng bộ thường được ưu tiên để đạt hiệu suất. Callback có thể được sử dụng để ghi log thành công hoặc xử lý lỗi.

### Cấu hình

- [x] Đảm bảo các phụ thuộc `io.confluent:kafka-avro-serializer` và `org.apache.kafka:kafka-clients` có trong file xây dựng của dự án. (Đã hoàn thành)
    - Ghi chú: Các thư viện này cung cấp các bộ tuần tự hóa cần thiết và chức năng client Kafka.

### Xử lý lỗi

- [x] Triển khai một `Callback` cho `producer.send()` để xử lý việc gửi thành công và các ngoại lệ. (Đã hoàn thành)
    - Ghi chú: Ghi log siêu dữ liệu khi thành công (topic, phân vùng, offset). Ghi log chi tiết lỗi khi thất bại. Điều này giúp gỡ lỗi và giám sát.

## Tiến độ

### Producer

- [x] Lớp AvroUserProducer đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA`: Yêu cầu schema `User.avsc` phải được định nghĩa.
*   `TASK-KAFKA-M4-S1-002-CONFIGURE-AVRO-COMPILER`: Yêu cầu lớp `User` Java phải được tạo và có sẵn trong classpath.

## Các cân nhắc chính

*   **Tích hợp Schema Registry:** `KafkaAvroSerializer` tự động đăng ký schema với Schema Registry nếu nó chưa tồn tại hoặc xác minh tính tương thích nếu đã có. Điều này đơn giản hóa đáng kể việc quản lý schema.
*   **Hiệu suất:** Đối với các kịch bản thông lượng cao, hãy cân nhắc việc nhóm tin nhắn (`linger.ms`, `batch.size`) và nén (`compression.type`).
*   **Producer bất biến và giao dịch:** Để đảm bảo phân phối mạnh mẽ hơn (ngữ nghĩa "exactly-once"), bật `enable.idempotence` và tìm hiểu về các producer giao dịch, mặc dù những điều này có thể được đề cập trong một module riêng, nâng cao hơn. Đối với nhiệm vụ này, tập trung vào tuần tự hóa Avro cơ bản.

## Ghi chú

*   **Nền tảng Confluent:** `KafkaAvroSerializer` là một phần của Nền tảng Confluent. Đảm bảo tính tương thích với phiên bản Kafka và Schema Registry của bạn.
*   **Vòng đời Producer:** Hãy nhớ đóng producer (`producer.close()`) khi ứng dụng tắt để đảm bảo tất cả các bản ghi đã được đệm được gửi và tài nguyên được giải phóng.

## Thảo luận

Việc triển khai Avro Producer là một bước thực tế để hiểu cách dữ liệu có cấu trúc có thể được gửi hiệu quả đến Kafka. Điểm mấu chốt là sự tích hợp liền mạch được cung cấp bởi `KafkaAvroSerializer` với Schema Registry, giúp trừu tượng hóa phần lớn sự phức tạp của việc quản lý schema. Cách tiếp cận này thúc đẩy tính nhất quán dữ liệu và đơn giản hóa tiến hóa schema.

## Các bước tiếp theo

Bước tiếp theo sẽ là triển khai dịch vụ Avro Kafka Consumer tương ứng để thể hiện luồng tin nhắn Avro từ đầu đến cuối.

## Trạng thái hiện tại

Việc triển khai dịch vụ Producer Kafka Avro đã được lên kế hoạch.