---
title: Cấu hình Avro Consumer cho kết nối SSL/TLS
type: task
status: planned
created: 2025-08-12T14:07:22
updated: 2025-08-12T14:19:29
id: TASK-KAFKA-M4-S2-008
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS, TASK-KAFKA-M4-S2-006-CONFIGURE-BROKER-SSL, TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER]
tags: [kafka, security, ssl, tls, consumer, configuration]
---

## Mô tả

Nhiệm vụ này liên quan đến việc sửa đổi Avro Kafka Consumer (được triển khai trong Nhiệm vụ 4) để thiết lập kết nối an toàn với broker Kafka bằng SSL/TLS. Điều này yêu cầu thêm các thuộc tính cấu hình SSL/TLS cụ thể vào cấu hình của Consumer. Điều này hoàn tất kênh liên lạc an toàn đầu cuối bằng SSL/TLS.

## Mục tiêu

*   Cập nhật lớp `AvroUserConsumer` (hoặc cấu hình của nó) để bao gồm các thuộc tính SSL/TLS.
*   Đảm bảo Consumer có thể kết nối thành công với cluster Kafka đã bật SSL/TLS.
*   Xác minh rằng các tin nhắn được nhận an toàn qua kênh được mã hóa.

## Danh sách kiểm tra

### Consumer

- [ ] Định vị cấu hình Kafka Consumer trong `AvroUserConsumer.java` (hoặc nguồn cấu hình của nó).
- [ ] Thêm các thuộc tính SSL/TLS sau vào `ConsumerConfig` (hoặc `properties` map):
    - `security.protocol`: Đặt thành `SSL`.
    - `ssl.truststore.location`: Đường dẫn đến file truststore của client (được tạo trong Nhiệm vụ 5).
    - `ssl.truststore.password`: Mật khẩu cho truststore của client.
    - `ssl.keystore.location`: Đường dẫn đến file keystore của client (được tạo trong Nhiệm vụ 5).
    - `ssl.keystore.password`: Mật khẩu cho keystore của client.
    - `ssl.key.password`: Mật khẩu cho khóa riêng trong keystore của client.
    - Ghi chú: Các thuộc tính này hướng dẫn consumer sử dụng SSL/TLS để giao tiếp và cung cấp thông tin xác thực cần thiết cho TLS lẫn nhau.
- [ ] Kiểm tra kết nối của Consumer:
    - [ ] Chạy ứng dụng Avro Consumer.
    - [ ] Xác minh rằng nó có thể kết nối với broker Kafka mà không có lỗi SSL/TLS.
    - [ ] Kiểm tra log của broker Kafka để tìm kết nối client thành công qua SSL/TLS.
    - [ ] Xác minh rằng các tin nhắn được gửi bởi SSL/TLS Producer (Nhiệm vụ 7) được nhận và xử lý chính xác bởi Consumer này.
    - Ghi chú: Kiểm thử toàn diện là điều cần thiết để xác nhận kênh liên lạc an toàn.

## Tiến độ

### Consumer

- [ ] Cấu hình SSL/TLS của Consumer đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS`: Yêu cầu các file keystore và truststore của client.
*   `TASK-KAFKA-M4-S2-006-CONFIGURE-BROKER-SSL`: Yêu cầu các broker Kafka đã được cấu hình và chạy với SSL/TLS.
*   `TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER`: Nhiệm vụ này sửa đổi triển khai Avro Consumer hiện có.

## Các cân nhắc chính

*   **Tính nhất quán:** Đảm bảo các tham số cấu hình SSL/TLS (đường dẫn, mật khẩu) nhất quán giữa Producer (Nhiệm vụ 7) và Consumer.
*   **Xử lý lỗi:** Chú ý đến bất kỳ `SSLHandshakeException` hoặc các lỗi liên quan đến bảo mật khác trong quá trình kết nối. Những lỗi này thường chỉ ra cấu hình sai của chứng chỉ hoặc truststore.
*   **Tường lửa:** Đảm bảo tường lửa mạng cho phép giao tiếp trên cổng SSL/TLS (ví dụ: 9093) giữa client và broker.

## Ghi chú

*   **Vị trí thuộc tính:** Các thuộc tính này nên được thêm vào đối tượng `Properties` hoặc `Map` được sử dụng để tạo thể hiện `KafkaConsumer`.
*   **Cấu hình tập trung:** Giống như Producer, quản lý cấu hình nhạy cảm một cách an toàn.

## Thảo luận

Cấu hình Consumer cho SSL/TLS là bước cuối cùng trong việc thiết lập một kênh liên lạc được mã hóa hoàn toàn cho các tin nhắn Kafka. Điều này đảm bảo rằng dữ liệu được bảo vệ không chỉ khi gửi mà còn khi tiêu thụ, duy trì tính bảo mật và toàn vẹn trong toàn bộ pipeline dữ liệu. Với cả hai đầu được bảo mật, hệ thống đạt được mức độ bảo mật lớp vận chuyển mạnh mẽ.

## Các bước tiếp theo

Với cấu hình SSL/TLS hoàn tất cho cả Producer và Consumer, tập hợp các nhiệm vụ tiếp theo sẽ tập trung vào bảo mật Kafka bằng SASL (SCRAM).

## Trạng thái hiện tại

Cấu hình Avro Consumer cho kết nối SSL/TLS đã được lên kế hoạch.