---
title: Cấu hình các broker Kafka để bật SASL/SCRAM
type: task
status: planned
created: 2025-08-12T14:07:41
updated: 2025-08-12T14:19:58
id: TASK-KAFKA-M4-S3-009
priority: high
memory_types: [procedural]
dependencies: []
tags: [kafka, security, sasl, scram, broker, configuration]
---

## Mô tả

Nhiệm vụ này tập trung vào việc cấu hình các broker Kafka để bật SASL (Simple Authentication and Security Layer) với cơ chế SCRAM (Salted Challenge Response Authentication Mechanism). Điều này cung cấp xác thực mạnh mẽ cho các client kết nối với cluster Kafka. Nó liên quan đến việc cập nhật file `server.properties` và có thể thiết lập cấu hình JAAS (Java Authentication and Authorization Service) cho các broker.

## Mục tiêu

*   Sửa đổi file `server.properties` cho mỗi broker Kafka để bật SASL/SCRAM.
*   Cấu hình các listener của broker để chấp nhận kết nối SASL_SSL hoặc SASL_PLAINTEXT.
*   Định nghĩa cấu hình JAAS cho các broker Kafka để xác thực chống lại một kho người dùng (ví dụ: kho người dùng dựa trên file cho phát triển, hoặc một hệ thống mạnh mẽ hơn cho sản xuất).
*   Chỉ định cơ chế SCRAM (ví dụ: SCRAM-SHA-512).

## Danh sách kiểm tra

### Cấu hình

- [ ] Đối với mỗi broker Kafka, định vị file `server.properties` của nó.
    - Ghi chú: Thường được tìm thấy trong thư mục cài đặt Kafka dưới `config/`.
- [ ] **Cấu hình Listeners:**
    - [ ] Cập nhật `listeners` để bao gồm giao thức SASL, ví dụ: `listeners=PLAINTEXT://localhost:9092,SASL_SSL://localhost:9094`.
    - [ ] Cập nhật `advertised.listeners` nếu chạy trong Docker hoặc môi trường phân tán.
    - Ghi chú: Rất khuyến nghị kết hợp SASL với SSL/TLS (`SASL_SSL`) để mã hóa khi truyền tải.
- [ ] **Cấu hình cơ chế SASL:**
    - [ ] Đặt `sasl.enabled.mechanisms` thành `SCRAM-SHA-512`.
    - [ ] Đặt `sasl.mechanism.inter.broker.protocol` thành `SCRAM-SHA-512` (để xác thực giữa các broker).
    - Ghi chú: SCRAM-SHA-512 là một cơ chế mạnh, được sử dụng rộng rãi.
- [ ] **Cấu hình JAAS cho Kafka Broker:**
    - [ ] Tạo một file cấu hình JAAS (ví dụ: `kafka_server_jaas.conf`).
    - [ ] Thêm một mục `KafkaServer` vào file này, chỉ định `ScramLoginModule` và một kho người dùng dựa trên file (ví dụ: `org.apache.kafka.common.security.scram.ScramLoginModule required username=admin password=admin;`).
    - [ ] Tham chiếu file JAAS này trong `server.properties` hoặc là một đối số JVM (`-Djava.security.auth.login.config=/path/to/kafka_server_jaas.conf`).
    - Ghi chú: Đối với phát triển, một kho người dùng dựa trên file đơn giản là đủ. Đối với sản xuất, tích hợp với LDAP, Kerberos hoặc một hệ thống quản lý người dùng an toàn khác.
- [ ] **Cấu hình bảo mật Zookeeper/KRaft (nếu áp dụng):**
    - [ ] Nếu sử dụng Zookeeper, đảm bảo Zookeeper cũng được bảo mật, vì các broker Kafka tương tác với nó.
    - [ ] Nếu sử dụng chế độ KRaft, đảm bảo các bộ điều khiển KRaft được bảo mật.
    - Ghi chú: Điều này nằm ngoài phạm vi trực tiếp của nhiệm vụ này nhưng là một cân nhắc bảo mật quan trọng cho toàn bộ cluster.
- [ ] **Khởi động lại (các) broker Kafka:**
    - [ ] Khởi động lại mỗi broker Kafka sau khi áp dụng các thay đổi cấu hình.
    - Ghi chú: Các thay đổi cấu hình yêu cầu khởi động lại để có hiệu lực.

## Tiến độ

### Cấu hình

- [ ] Cấu hình SASL/SCRAM của Broker đã được khởi tạo.

## Phụ thuộc

Không có, mặc dù nó thường bổ sung cho cấu hình SSL/TLS.

## Các cân nhắc chính

*   **Kết hợp giao thức bảo mật:** Luôn kết hợp SASL với SSL/TLS (`SASL_SSL`) trong sản xuất để đảm bảo cả xác thực và mã hóa. `SASL_PLAINTEXT` chỉ nên được sử dụng để kiểm thử trong các môi trường biệt lập.
*   **Quản lý người dùng:** Cấu hình JAAS là chìa khóa để Kafka xác thực người dùng. Đối với sản xuất, tránh mật khẩu plaintext và kho người dùng dựa trên file.
*   **ACLs:** Trong khi SASL xác thực người dùng, ACLs (Access Control Lists) là cần thiết để ủy quyền cho những gì người dùng đã xác thực được phép làm (ví dụ: đọc/ghi vào các topic cụ thể). Điều này sẽ được đề cập trong một nhiệm vụ sau.
*   **Bảo mật Zookeeper/KRaft:** Đừng bỏ qua việc bảo mật kho siêu dữ liệu (Zookeeper hoặc KRaft) nếu nó nằm bên ngoài broker.

## Ghi chú

*   **Ví dụ `kafka_server_jaas.conf` (cho người dùng dựa trên file):**
    ```
    KafkaServer {
        org.apache.kafka.common.security.scram.ScramLoginModule required
        username="admin" password="admin-password"
        username="producer_user" password="producer-password"
        username="consumer_user" password="consumer-password";
    };
    ```
*   **`KAFKA_OPTS`:** Khi chạy Kafka từ dòng lệnh, hãy sử dụng `export KAFKA_OPTS="-Djava.security.auth.login.config=/path/to/kafka_server_jaas.conf"` trước khi khởi động broker.

## Thảo luận

Việc bật SASL/SCRAM trên các broker Kafka cung cấp một cơ chế xác thực mạnh mẽ, đảm bảo rằng chỉ các client được ủy quyền mới có thể kết nối với cluster. Đây là một lớp phòng thủ quan trọng chống lại truy cập trái phép. Tính linh hoạt của JAAS cho phép tích hợp với các hệ thống xác thực doanh nghiệp khác nhau, làm cho nó phù hợp với các kịch bản triển khai đa dạng.

## Các bước tiếp theo

Khi các broker Kafka được cấu hình cho SASL/SCRAM, bước tiếp theo sẽ là tạo các người dùng SASL/SCRAM thực tế trong Kafka.

## Trạng thái hiện tại

Cấu hình các broker Kafka cho SASL/SCRAM đã được lên kế hoạch.