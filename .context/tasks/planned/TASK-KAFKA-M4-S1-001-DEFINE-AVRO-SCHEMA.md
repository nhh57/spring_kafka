---
title: Định nghĩa Schema Avro cho thực thể Người dùng
type: task
status: planned
created: 2025-08-12T14:05:01
updated: 2025-08-12T14:16:02
id: TASK-KAFKA-M4-S1-001
priority: high
memory_types: [procedural, semantic]
dependencies: []
tags: [kafka, avro, serdes, schema]
---

## Mô tả

Nhiệm vụ này liên quan đến việc định nghĩa một Schema Avro đơn giản cho một thực thể `User` (Người dùng). Schema này sẽ đóng vai trò là mô hình dữ liệu cho các tin nhắn được trao đổi giữa Producer và Consumer của Kafka, thể hiện việc sử dụng Avro để tuần tự hóa và giải tuần tự hóa. Schema sẽ được lưu dưới dạng file `.avsc`.

## Mục tiêu

*   Tạo một file schema Avro mới (`User.avsc`).
*   Định nghĩa cấu trúc của thực thể `User` trong schema, bao gồm các trường phổ biến như ID, tên và email.
*   Đảm bảo schema hợp lệ và tuân thủ đặc tả Avro.

## Danh sách kiểm tra

### Thực thể

- [ ] Tạo file `User.avsc` trong `src/main/avro/`.
    - Ghi chú: File này sẽ chứa định nghĩa schema Avro. Thư mục `src/main/avro/` là một quy ước phổ biến để lưu trữ các file schema Avro trong các dự án Java. Vị trí này sẽ được các plugin Maven/Gradle sử dụng để tạo các lớp Java.

### Cấu hình

- [ ] Định nghĩa schema `User` với các trường:
    - `id`: chuỗi (ví dụ: UUID để đảm bảo tính duy nhất)
    - `name`: chuỗi
    - `email`: chuỗi
    - Ghi chú: Bắt đầu với một schema đơn giản để giảm thiểu độ phức tạp. Các trường phải mô tả và đại diện cho dữ liệu người dùng điển hình. Cân nhắc sử dụng các kiểu `union` cho các trường có thể null nếu cần, nhưng đối với nhiệm vụ ban đầu này, hãy giữ đơn giản với các kiểu không thể null.

## Tiến độ

### Thực thể

- [ ] User.avsc đã được tạo với schema cơ bản.

## Phụ thuộc

Không có. Nhiệm vụ này là một bước cơ bản để triển khai Avro Serdes.

## Các cân nhắc chính

*   **Tại sao là Avro?** Avro là một định dạng tuần tự hóa nhị phân nhỏ gọn, nhanh chóng, tích hợp tốt với Schema Registry. Nó cung cấp khả năng tiến hóa schema mạnh mẽ (tương thích ngược và tiến) rất quan trọng cho việc phát triển các mô hình dữ liệu trong môi trường streaming phân tán.
*   **Tiến hóa Schema:** Việc định nghĩa schema ngay từ đầu giúp thực thi các hợp đồng dữ liệu giữa producer và consumer. Các thay đổi trong tương lai đối với schema phải xem xét các quy tắc tương thích (ví dụ: thêm các trường tùy chọn để tương thích ngược).
*   **Dễ đọc so với Hiệu quả:** Mặc dù JSON dễ đọc, định dạng nhị phân của Avro hiệu quả hơn cho việc truyền tải và lưu trữ qua mạng, làm cho nó phù hợp với các topic Kafka có thông lượng cao.

## Ghi chú

*   **Định nghĩa Schema:** Schema Avro được định nghĩa ở định dạng JSON. Kiểu cấp cao nhất phải là "record".
*   **Quy ước đặt tên:** Avro khuyến nghị sử dụng `namespace` để ngăn chặn xung đột đặt tên và định nghĩa cấu trúc gói cho các lớp Java được tạo.
*   **Cấu trúc ví dụ:**
    ```json
    {
      "type": "record",
      "name": "User",
      "namespace": "com.example.kafka.avro",
      "fields": [
        {"name": "id", "type": "string"},
        {"name": "name", "type": "string"},
        {"name": "email", "type": "string"}
      ]
    }
    ```

## Thảo luận

Sức mạnh của Avro nằm ở cách tiếp cận schema-first. Bằng cách định nghĩa schema một cách rõ ràng, chúng ta có được sự an toàn tại thời điểm biên dịch và kiểm tra tính tương thích tại thời điểm chạy, giảm lỗi có thể phát sinh từ sự không khớp schema trong các hệ thống lỏng lẻo. Trường `namespace` đặc biệt quan trọng vì nó trực tiếp chuyển đổi thành tên gói Java cho các lớp được tạo, đảm bảo tổ chức phù hợp.

## Các bước tiếp theo

Sau khi định nghĩa schema, bước tiếp theo sẽ là cấu hình hệ thống xây dựng (Gradle/Maven) để tự động tạo các lớp Java từ schema Avro này.

## Trạng thái hiện tại

Schema Avro cho thực thể `User` đã được lên kế hoạch.