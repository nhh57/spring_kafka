---
title: Triển khai dịch vụ Consumer Kafka Avro
type: task
status: planned
created: 2025-08-12T14:06:01
updated: 2025-08-12T14:17:27
id: TASK-KAFKA-M4-S1-004
priority: high
memory_types: [procedural, semantic]
dependencies: [TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA, TASK-KAFKA-M4-S1-002-CONFIGURE-AVRO-COMPILER]
tags: [kafka, avro, consumer, serdes, java]
---

## Mô tả

Nhiệm vụ này liên quan đến việc triển khai một dịch vụ Kafka Consumer có thể đọc các tin nhắn được tuần tự hóa bằng Avro. Dịch vụ sẽ sử dụng các lớp Java được tạo từ schema Avro và cấu hình Kafka Consumer để tích hợp với Schema Registry, thể hiện hiệu quả luồng tin nhắn Avro từ đầu đến cuối.

## Mục tiêu

*   Tạo một lớp Java để đóng gói logic Kafka Consumer Avro.
*   Cấu hình thể hiện `KafkaConsumer` với các bộ giải tuần tự hóa Avro và URL Schema Registry phù hợp.
*   Triển khai một phương thức để thăm dò tin nhắn, giải tuần tự hóa các đối tượng `User` Avro và xử lý chúng.
*   Quản lý offset của consumer theo cách thủ công (ví dụ: `commitSync()` hoặc `commitAsync()`).

## Danh sách kiểm tra

### Consumer

- [ ] Tạo một lớp Java mới, ví dụ: `src/main/java/com/example/kafka/avro/consumer/AvroUserConsumer.java`.
    - Ghi chú: Lớp này sẽ chứa logic cốt lõi để tiêu thụ tin nhắn Avro.
- [ ] Khởi tạo `KafkaConsumer` với các thuộc tính:
    - `bootstrap.servers`: Địa chỉ các broker Kafka.
    - `group.id`: ID nhóm consumer.
    - `key.deserializer`: `org.apache.kafka.common.serialization.StringDeserializer` (giả sử khóa là chuỗi).
    - `value.deserializer`: `io.confluent.kafka.serializers.KafkaAvroDeserializer`.
    - `schema.registry.url`: URL của Confluent Schema Registry.
    - `specific.avro.reader`: `true` để bật giải tuần tự hóa bản ghi Avro cụ thể.
    - `enable.auto.commit`: `false` (để quản lý offset thủ công).
    - `auto.offset.reset`: `earliest` hoặc `latest` tùy thuộc vào hành vi mong muốn.
    - Ghi chú: Các thuộc tính này rất quan trọng để bật giải tuần tự hóa Avro và tích hợp Schema Registry. `specific.avro.reader` quan trọng để lấy trực tiếp đối tượng Avro Java đã tạo.
- [ ] Triển khai một phương thức `run()` (hoặc tương tự) liên tục thăm dò tin nhắn:
    - Gọi `consumer.poll(Duration.ofMillis(100))` để lấy bản ghi.
    - Lặp qua `ConsumerRecords` và giải tuần tự hóa giá trị của mỗi bản ghi thành đối tượng `User` Avro.
    - Xử lý đối tượng `User` (ví dụ: in ra console).
    - Triển khai commit offset thủ công (`consumer.commitSync()` hoặc `consumer.commitAsync()`) sau khi xử lý.
    - Ghi chú: Thăm dò trong một vòng lặp là tiêu chuẩn cho các consumer Kafka. Commit offset thủ công cung cấp nhiều quyền kiểm soát hơn đối với các đảm bảo xử lý tin nhắn.

### Cấu hình

- [ ] Đảm bảo các phụ thuộc `io.confluent:kafka-avro-serializer` và `org.apache.kafka:kafka-clients` có trong file xây dựng của dự án.
    - Ghi chú: Các thư viện này cung cấp các bộ giải tuần tự hóa cần thiết và chức năng client Kafka.

### Xử lý lỗi

- [ ] Triển khai xử lý lỗi mạnh mẽ cho việc xử lý tin nhắn.
    - Ghi chú: Cân nhắc sử dụng các khối try-catch xung quanh logic xử lý tin nhắn. Quyết định chiến lược cho các tin nhắn lỗi (poison pills) (tin nhắn liên tục gây lỗi), ví dụ: ghi log và bỏ qua, hoặc gửi đến Dead Letter Queue (DLQ).

## Tiến độ

### Consumer

- [ ] Lớp AvroUserConsumer đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA`: Yêu cầu schema `User.avsc` phải được định nghĩa.
*   `TASK-KAFKA-M4-S1-002-CONFIGURE-AVRO-COMPILER`: Yêu cầu lớp `User` Java phải được tạo và có sẵn trong classpath.
*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Producer cần thiết để gửi tin nhắn mà consumer có thể tiêu thụ.

## Các cân nhắc chính

*   **Tính bất biến:** Nếu logic xử lý có tác dụng phụ, hãy đảm bảo consumer là bất biến để ngăn chặn các vấn đề từ việc xử lý trùng lặp (có thể xảy ra với đảm bảo phân phối "at-least-once").
*   **Tái cân bằng nhóm consumer:** Lưu ý cách `max.poll.interval.ms` và thời gian xử lý tương tác để tránh các vấn đề tái cân bằng. Nếu xử lý mất quá nhiều thời gian, consumer có thể bị loại khỏi nhóm.
*   **An toàn luồng:** Nếu xử lý liên quan đến trạng thái chia sẻ, hãy đảm bảo an toàn luồng, đặc biệt nếu sử dụng thiết lập consumer đa luồng (ví dụ: concurrency của Spring Kafka). Đối với ví dụ cơ bản này, một consumer đơn luồng là đủ.

## Ghi chú

*   **Vòng đời Consumer:** Hãy nhớ đóng consumer (`consumer.close()`) khi ứng dụng tắt để đảm bảo offset được commit và tài nguyên được giải phóng.
*   **Tiến hóa Schema:** `KafkaAvroDeserializer` tự động xử lý tiến hóa schema nếu schema của producer tương thích ngược hoặc tiến với schema của consumer, miễn là Schema Registry được cấu hình với các quy tắc tương thích phù hợp.

## Thảo luận

Việc triển khai Avro Consumer hoàn tất luồng dữ liệu từ đầu đến cuối bằng Avro. Nhiệm vụ này làm nổi bật cách các consumer giải tuần tự hóa tin nhắn Avro một cách liền mạch bằng cách sử dụng `KafkaAvroDeserializer` và Schema Registry. Quản lý offset thủ công là một phương pháp hay nhất quan trọng cho các ứng dụng sản xuất, cung cấp quyền kiểm soát chính xác đối với các đảm bảo xử lý tin nhắn.

## Các bước tiếp theo

Với cả Producer và Consumer đã được triển khai, các bước tiếp theo sẽ liên quan đến việc thiết lập và kiểm tra toàn bộ pipeline Avro Serdes.

## Trạng thái hiện tại

Việc triển khai dịch vụ Consumer Kafka Avro đã được lên kế hoạch.