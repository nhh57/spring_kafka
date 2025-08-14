---
title: Tạo người dùng SASL/SCRAM trên các broker Kafka
type: task
status: planned
created: 2025-08-12T14:08:02
updated: 2025-08-12T14:20:29
id: TASK-KAFKA-M4-S3-010
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL]
tags: [kafka, security, sasl, scram, user-management]
---

## Mô tả

Nhiệm vụ này tập trung vào việc tạo và quản lý người dùng SASL/SCRAM trực tiếp trên các broker Kafka. Những người dùng này sẽ được các ứng dụng client (Producer và Consumer) sử dụng để xác thực với cluster Kafka. Điều này thường được thực hiện bằng công cụ dòng lệnh `kafka-configs.sh`.

## Mục tiêu

*   Tạo một người dùng chuyên dụng cho các ứng dụng Producer.
*   Tạo một người dùng chuyên dụng cho các ứng dụng Consumer.
*   Đảm bảo người dùng được thêm chính xác vào cấu hình bảo mật của broker Kafka (ví dụ: file người dùng JAAS được cấu hình trong Nhiệm vụ 9).
*   Hiểu các công cụ dòng lệnh để quản lý người dùng.

## Danh sách kiểm tra

### Bảo mật

- [ ] **Tru cập máy Broker Kafka:**
    - [ ] Đảm bảo bạn có quyền truy cập dòng lệnh vào một trong các máy broker Kafka.
    - Ghi chú: Công cụ `kafka-configs.sh` cần được chạy trên một máy có thể kết nối với cluster Kafka và có quyền truy cập vào cài đặt Kafka.
- [ ] **Tạo người dùng Producer:**
    - [ ] Sử dụng `kafka-configs.sh` để tạo người dùng SCRAM cho ứng dụng Producer, ví dụ: `producer_user`.
    - Lệnh ví dụ:
        ```bash
        kafka-configs.sh --bootstrap-server localhost:9092 --alter --add-config 'SCRAM-SHA-512=[password=your-producer-password]' --entity-type users --entity-name producer_user
        ```
    - Ghi chú: Thay thế `localhost:9092` bằng địa chỉ và cổng của broker của bạn (listener đã bật SASL). Chọn mật khẩu mạnh.
- [ ] **Tạo người dùng Consumer:**
    - [ ] Sử dụng `kafka-configs.sh` để tạo người dùng SCRAM cho ứng dụng Consumer, ví dụ: `consumer_user`.
    - Lệnh ví dụ:
        ```bash
        kafka-configs.sh --bootstrap-server localhost:9092 --alter --add-config 'SCRAM-SHA-512=[password=your-consumer-password]' --entity-type users --entity-name consumer_user
        ```
    - Ghi chú: Chọn mật khẩu mạnh.
- [ ] **Xác minh việc tạo người dùng:**
    - [ ] Sử dụng `kafka-configs.sh` để mô tả người dùng đã tạo và xác minh sự tồn tại của họ.
    - Lệnh ví dụ:
        ```bash
        kafka-configs.sh --bootstrap-server localhost:9092 --describe --entity-type users --entity-name producer_user
        kafka-configs.sh --bootstrap-server localhost:9092 --describe --entity-type users --entity-name consumer_user
        ```
    - Ghi chú: Điều này xác nhận rằng người dùng đã được thêm thành công vào kho người dùng nội bộ của Kafka.

## Tiến độ

### Bảo mật

- [ ] Việc tạo người dùng SASL/SCRAM đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL`: Nhiệm vụ này phụ thuộc vào việc các broker Kafka đã được cấu hình để hỗ trợ xác thực SASL/SCRAM.

## Các cân nhắc chính

*   **Bảo mật mật khẩu:** Không bao giờ mã hóa cứng mật khẩu trong các script hoặc file cấu hình trong sản xuất. Sử dụng hệ thống quản lý bí mật an toàn (ví dụ: HashiCorp Vault, AWS Secrets Manager) để lưu trữ và truy xuất thông tin xác thực.
*   **Vai trò người dùng:** Trong một kịch bản thực tế, bạn sẽ tạo các người dùng khác nhau với các vai trò cụ thể (ví dụ: `admin`, `monitoring`, `producer_app_A`, `consumer_app_B`) và cấp cho họ các ACL phù hợp.
*   **Tự động hóa:** Tự động hóa việc tạo và quản lý người dùng trong môi trường sản xuất như một phần của pipeline cơ sở hạ tầng dưới dạng mã (IaC) hoặc CI/CD của bạn.
*   **Cấu hình JAAS:** Người dùng được tạo qua `kafka-configs.sh` được lưu trữ nội bộ bởi Kafka. Nếu bạn đang sử dụng cấu hình JAAS dựa trên file cho broker (như được đề xuất trong Nhiệm vụ 9), hãy đảm bảo người dùng bạn tạo ở đây cũng được phản ánh ở đó hoặc cấu hình JAAS của broker được thiết lập để đọc từ kho người dùng nội bộ của Kafka.

## Ghi chú

*   **`kafka-configs.sh`:** Công cụ này mạnh mẽ và có thể được sử dụng để quản lý các cấu hình Kafka khác nhau, bao gồm thông tin xác thực người dùng.
*   **`--bootstrap-server`:** Đảm bảo điều này trỏ đến một broker có listener đã bật SASL.

## Thảo luận

Việc tạo người dùng SASL/SCRAM chuyên dụng là một bước quan trọng trong việc triển khai kiểm soát truy cập chi tiết trong Kafka. Bằng cách cung cấp thông tin xác thực duy nhất cho mỗi ứng dụng hoặc dịch vụ, bạn có thể theo dõi các hoạt động của chúng và thực thi quyền hiệu quả hơn bằng cách sử dụng ACL. Điều này nâng cao tư thế bảo mật tổng thể của cluster Kafka bằng cách đảm bảo rằng chỉ các thực thể được xác thực mới có thể tương tác với nó.

## Các bước tiếp theo

Khi người dùng SASL/SCRAM được tạo, bước tiếp theo sẽ là cấu hình các ứng dụng client (Producer và Consumer) để sử dụng thông tin xác thực này để xác thực.

## Trạng thái hiện tại

Việc tạo người dùng SASL/SCRAM trên các broker Kafka đã được lên kế hoạch.