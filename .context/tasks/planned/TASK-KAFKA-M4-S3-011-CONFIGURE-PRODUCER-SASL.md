---
title: Cấu hình Avro Producer cho kết nối SASL/SCRAM
type: task
status: planned
created: 2025-08-12T14:08:23
updated: 2025-08-12T14:21:00
id: TASK-KAFKA-M4-S3-011
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL, TASK-KAFKA-M4-S3-010-CREATE-SASL-USERS, TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER]
tags: [kafka, security, sasl, scram, producer, configuration]
---

## Mô tả

Nhiệm vụ này liên quan đến việc sửa đổi Avro Kafka Producer (được triển khai trong Nhiệm vụ 3) để xác thực với broker Kafka bằng SASL/SCRAM. Điều này yêu cầu thêm các thuộc tính cấu hình SASL/SCRAM cụ thể vào cấu hình của Producer, bao gồm tên người dùng và mật khẩu được tạo trong Nhiệm vụ 10.

## Mục tiêu

*   Cập nhật lớp `AvroUserProducer` (hoặc cấu hình của nó) để bao gồm các thuộc tính SASL/SCRAM.
*   Đảm bảo Producer có thể xác thực thành công với cluster Kafka bằng thông tin xác thực được cung cấp.
*   Xác minh rằng các tin nhắn được gửi qua kênh đã xác thực.

## Danh sách kiểm tra

### Producer

- [ ] Định vị cấu hình Kafka Producer trong `AvroUserProducer.java` (hoặc nguồn cấu hình của nó).
- [ ] Thêm các thuộc tính SASL/SCRAM sau vào `ProducerConfig` (hoặc `properties` map):
    - `security.protocol`: Đặt thành `SASL_SSL` (nếu kết hợp với SSL/TLS) hoặc `SASL_PLAINTEXT`.
    - `sasl.mechanism`: Đặt thành `SCRAM-SHA-512`.
    - `sasl.jaas.config`: Cấu hình với tên người dùng và mật khẩu của Producer. Ví dụ:
        ```
        org.apache.kafka.common.security.scram.ScramLoginModule required username="producer_user" password="your-producer-password";
        ```
    - Ghi chú: `SASL_SSL` rất được khuyến nghị cho môi trường sản xuất để cũng mã hóa dữ liệu khi truyền tải. `sasl.jaas.config` cung cấp thông tin xác thực.
- [ ] Kiểm tra kết nối của Producer:
    - [ ] Chạy ứng dụng Avro Producer.
    - [ ] Xác minh rằng nó có thể kết nối với broker Kafka mà không có lỗi xác thực.
    - [ ] Kiểm tra log của broker Kafka để tìm kết nối client xác thực thành công qua SASL/SCRAM.
    - Ghi chú: Xác thực kết nối ban đầu rất quan trọng.

## Tiến độ

### Producer

- [ ] Cấu hình SASL/SCRAM của Producer đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL`: Yêu cầu các broker Kafka đã được cấu hình và chạy với SASL/SCRAM.
*   `TASK-KAFKA-M4-S3-010-CREATE-SASL-USERS`: Yêu cầu `producer_user` đã được tạo trên các broker Kafka.
*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Nhiệm vụ này sửa đổi triển khai Avro Producer hiện có.

## Các cân nhắc chính

*   **Bảo mật thông tin xác thực:** Không bao giờ mã hóa cứng mật khẩu trong `sasl.jaas.config` trong sản xuất. Sử dụng các biến môi trường, dịch vụ quản lý bí mật hoặc các file cấu hình JAAS bên ngoài.
*   **Tính nhất quán của giao thức:** Đảm bảo `security.protocol` khớp với listener được cấu hình trên broker Kafka.
*   **Khắc phục sự cố:** Lỗi xác thực sẽ dẫn đến `SaslAuthenticationException` hoặc các lỗi tương tự. Kiểm tra log của ứng dụng client và log của broker để tìm thông báo chi tiết (ví dụ: tên người dùng/mật khẩu không chính xác, cơ chế không khớp).

## Ghi chú

*   **Cấu hình JAAS:** Đối với các ứng dụng client, cấu hình JAAS có thể được đặt trực tiếp trong các thuộc tính của producer hoặc thông qua một file cấu hình JAAS riêng biệt được tham chiếu bởi một đối số JVM.
*   **Vị trí thuộc tính:** Các thuộc tính này nên được thêm vào đối tượng `Properties` hoặc `Map` được sử dụng để tạo thể hiện `KafkaProducer`.

## Thảo luận

Cấu hình Producer cho SASL/SCRAM cung cấp một cơ chế xác thực mạnh mẽ, đảm bảo rằng chỉ các ứng dụng đáng tin cậy mới có thể gửi dữ liệu đến cluster Kafka. Đây là một biện pháp bảo mật quan trọng để ngăn chặn việc đưa dữ liệu trái phép vào. Khi kết hợp với SSL/TLS, nó tạo thành một giải pháp bảo mật toàn diện cho dữ liệu đang truyền.

## Các bước tiếp theo

Bước tiếp theo sẽ là cấu hình Avro Kafka Consumer tương ứng cho kết nối SASL/SCRAM để hoàn tất giao tiếp đầu cuối đã xác thực.

## Trạng thái hiện tại

Cấu hình Avro Producer cho kết nối SASL/SCRAM đã được lên kế hoạch.