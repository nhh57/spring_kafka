---
title: Cấu hình Avro Consumer cho kết nối SASL/SCRAM
type: task
status: planned
created: 2025-08-12T14:08:43
updated: 2025-08-12T14:21:30
id: TASK-KAFKA-M4-S3-012
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL, TASK-KAFKA-M4-S3-010-CREATE-SASL-USERS, TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER]
tags: [kafka, security, sasl, scram, consumer, configuration]
---

## Mô tả

Nhiệm vụ này liên quan đến việc sửa đổi Avro Kafka Consumer (được triển khai trong Nhiệm vụ 4) để xác thực với broker Kafka bằng SASL/SCRAM. Điều này yêu cầu thêm các thuộc tính cấu hình SASL/SCRAM cụ thể vào cấu hình của Consumer, sử dụng tên người dùng và mật khẩu được tạo trong Nhiệm vụ 10. Điều này hoàn tất kênh liên lạc đã xác thực đầu cuối bằng SASL.

## Mục tiêu

*   Cập nhật lớp `AvroUserConsumer` (hoặc cấu hình của nó) để bao gồm các thuộc tính SASL/SCRAM.
*   Đảm bảo Consumer có thể xác thực thành công với cluster Kafka bằng thông tin xác thực được cung cấp.
*   Xác minh rằng các tin nhắn được nhận qua kênh đã xác thực.

## Danh sách kiểm tra

### Consumer

- [ ] Định vị cấu hình Kafka Consumer trong `AvroUserConsumer.java` (hoặc nguồn cấu hình của nó).
- [ ] Thêm các thuộc tính SASL/SCRAM sau vào `ConsumerConfig` (hoặc `properties` map):
    - `security.protocol`: Đặt thành `SASL_SSL` (nếu kết hợp với SSL/TLS) hoặc `SASL_PLAINTEXT`.
    - `sasl.mechanism`: Đặt thành `SCRAM-SHA-512`.
    - `sasl.jaas.config`: Cấu hình với tên người dùng và mật khẩu của Consumer. Ví dụ:
        ```
        org.apache.kafka.common.security.scram.ScramLoginModule required username="consumer_user" password="your-consumer-password";
        ```
    - Ghi chú: `SASL_SSL` rất được khuyến nghị cho môi trường sản xuất để cũng mã hóa dữ liệu khi truyền tải. `sasl.jaas.config` cung cấp thông tin xác thực.
- [ ] Kiểm tra kết nối của Consumer:
    - [ ] Chạy ứng dụng Avro Consumer.
    - [ ] Xác minh rằng nó có thể kết nối với broker Kafka mà không có lỗi xác thực.
    - [ ] Kiểm tra log của broker Kafka để tìm kết nối client xác thực thành công qua SASL/SCRAM.
    - [ ] Xác minh rằng các tin nhắn được gửi bởi SASL Producer (Nhiệm vụ 11) được nhận và xử lý chính xác bởi Consumer này.
    - Ghi chú: Kiểm thử toàn diện là điều cần thiết để xác nhận kênh liên lạc đã xác thực.

## Tiến độ

### Consumer

- [ ] Cấu hình SASL/SCRAM của Consumer đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL`: Yêu cầu các broker Kafka đã được cấu hình và chạy với SASL/SCRAM.
*   `TASK-KAFKA-M4-S3-010-CREATE-SASL-USERS`: Yêu cầu `consumer_user` đã được tạo trên các broker Kafka.
*   `TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER`: Nhiệm vụ này sửa đổi triển khai Avro Consumer hiện có.

## Các cân nhắc chính

*   **Bảo mật thông tin xác thực:** Không bao giờ mã hóa cứng mật khẩu trong `sasl.jaas.config` trong sản xuất. Sử dụng các biến môi trường, dịch vụ quản lý bí mật hoặc các file cấu hình JAAS bên ngoài.
*   **Tính nhất quán của giao thức:** Đảm bảo `security.protocol` khớp với listener được cấu hình trên broker Kafka.
*   **Khắc phục sự cố:** Lỗi xác thực sẽ dẫn đến `SaslAuthenticationException` hoặc các lỗi tương tự. Kiểm tra log của ứng dụng client và log của broker để tìm thông báo chi tiết (ví dụ: tên người dùng/mật khẩu không chính xác, cơ chế không khớp).

## Ghi chú

*   **Cấu hình JAAS:** Đối với các ứng dụng client, cấu hình JAAS có thể được đặt trực tiếp trong các thuộc tính của consumer hoặc thông qua một file cấu hình JAAS riêng biệt được tham chiếu bởi một đối số JVM.
*   **Vị trí thuộc tính:** Các thuộc tính này nên được thêm vào đối tượng `Properties` hoặc `Map` được sử dụng để tạo thể hiện `KafkaConsumer`.

## Thảo luận

Cấu hình Consumer cho SASL/SCRAM hoàn tất việc xác thực đầu cuối cho các tin nhắn Kafka. Điều này đảm bảo rằng chỉ các ứng dụng được ủy quyền mới có thể tiêu thụ dữ liệu từ cluster Kafka, bảo vệ thông tin nhạy cảm khỏi truy cập trái phép. Khi kết hợp với SSL/TLS, nó tạo thành một pipeline dữ liệu mạnh mẽ và an toàn.

## Các bước tiếp theo

Với cấu hình SASL/SCRAM hoàn tất cho cả Producer và Consumer, tập hợp các nhiệm vụ tiếp theo sẽ tập trung vào kiểm thử toàn diện tất cả các tính năng bảo mật và Serdes đã triển khai.

## Trạng thái hiện tại

Cấu hình Avro Consumer cho kết nối SASL/SCRAM đã được lên kế hoạch.