---
title: Cấu hình Avro Producer cho kết nối SSL/TLS
type: task
status: planned
created: 2025-08-12T14:07:02
updated: 2025-08-12T14:19:00
id: TASK-KAFKA-M4-S2-007
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS, TASK-KAFKA-M4-S2-006-CONFIGURE-BROKER-SSL, TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER]
tags: [kafka, security, ssl, tls, producer, configuration]
---

## Mô tả

Nhiệm vụ này liên quan đến việc sửa đổi Avro Kafka Producer (được triển khai trong Nhiệm vụ 3) để thiết lập kết nối an toàn với broker Kafka bằng SSL/TLS. Điều này yêu cầu thêm các thuộc tính cấu hình SSL/TLS cụ thể vào cấu hình của Producer.

## Mục tiêu

*   Cập nhật lớp `AvroUserProducer` (hoặc cấu hình của nó) để bao gồm các thuộc tính SSL/TLS.
*   Đảm bảo Producer có thể kết nối thành công với cluster Kafka đã bật SSL/TLS.
*   Xác minh rằng các tin nhắn được gửi an toàn qua kênh được mã hóa.

## Danh sách kiểm tra

### Producer

- [ ] Định vị cấu hình Kafka Producer trong `AvroUserProducer.java` (hoặc nguồn cấu hình của nó).
- [ ] Thêm các thuộc tính SSL/TLS sau vào `ProducerConfig` (hoặc `properties` map):
    - `security.protocol`: Đặt thành `SSL`.
    - `ssl.truststore.location`: Đường dẫn đến file truststore của client (được tạo trong Nhiệm vụ 5).
    - `ssl.truststore.password`: Mật khẩu cho truststore của client.
    - `ssl.keystore.location`: Đường dẫn đến file keystore của client (được tạo trong Nhiệm vụ 5).
    - `ssl.keystore.password`: Mật khẩu cho keystore của client.
    - `ssl.key.password`: Mật khẩu cho khóa riêng trong keystore của client.
    - Ghi chú: Các thuộc tính này hướng dẫn producer sử dụng SSL/TLS để giao tiếp và cung cấp thông tin xác thực cần thiết cho TLS lẫn nhau.
- [ ] Kiểm tra kết nối của Producer:
    - [ ] Chạy ứng dụng Avro Producer.
    - [ ] Xác minh rằng nó có thể kết nối với broker Kafka mà không có lỗi SSL/TLS.
    - [ ] Kiểm tra log của broker Kafka để tìm kết nối client thành công qua SSL/TLS.
    - Ghi chú: Xác thực kết nối ban đầu rất quan trọng.

## Tiến độ

### Producer

- [ ] Cấu hình SSL/TLS của Producer đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS`: Yêu cầu các file keystore và truststore của client.
*   `TASK-KAFKA-M4-S2-006-CONFIGURE-BROKER-SSL`: Yêu cầu các broker Kafka đã được cấu hình và chạy với SSL/TLS.
*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Nhiệm vụ này sửa đổi triển khai Avro Producer hiện có.

## Các cân nhắc chính

*   **Đường dẫn file:** Kiểm tra kỹ các đường dẫn tuyệt đối hoặc tương đối đến các file keystore và truststore. Đường dẫn không chính xác là một nguồn lỗi phổ biến.
*   **Mật khẩu:** Đảm bảo mật khẩu khớp với mật khẩu được sử dụng trong quá trình tạo chứng chỉ.
*   **Xác minh tên máy chủ:** Nếu `ssl.endpoint.identification.algorithm` không được đặt rõ ràng thành chuỗi rỗng (không được khuyến nghị cho sản xuất), hãy đảm bảo tên máy chủ được sử dụng trong `bootstrap.servers` khớp với Tên chung (CN) hoặc Tên thay thế chủ thể (SAN) trong chứng chỉ của broker.
*   **Khắc phục sự cố:** Các lỗi phổ biến bao gồm `SSLHandshakeException`, `InvalidPasswordException` hoặc `FileNotFoundException`. Kiểm tra log của ứng dụng client và log của broker để tìm thông báo lỗi chi tiết.

## Ghi chú

*   **Vị trí thuộc tính:** Các thuộc tính này nên được thêm vào đối tượng `Properties` hoặc `Map` được sử dụng để tạo thể hiện `KafkaProducer`.
*   **Cấu hình tập trung:** Trong một ứng dụng thực tế, các cấu hình nhạy cảm này (mật khẩu, đường dẫn) nên được quản lý an toàn, ví dụ: thông qua các biến môi trường, công cụ quản lý cấu hình hoặc dịch vụ quản lý bí mật.

## Thảo luận

Cấu hình Producer cho SSL/TLS đảm bảo rằng dữ liệu được mã hóa từ ứng dụng nguồn đến cluster Kafka. Điều này bổ sung một lớp bảo mật quan trọng, bảo vệ thông tin nhạy cảm khỏi bị chặn và giả mạo trong quá trình truyền. Kết hợp với SSL/TLS phía broker, nó thiết lập một kênh liên lạc an toàn.

## Các bước tiếp theo

Bước tiếp theo sẽ là cấu hình Avro Kafka Consumer tương ứng cho kết nối SSL/TLS để hoàn tất giao tiếp đầu cuối an toàn.

## Trạng thái hiện tại

Cấu hình Avro Producer cho kết nối SSL/TLS đã được lên kế hoạch.