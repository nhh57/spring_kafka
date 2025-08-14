---
title: Viết integration test cho các kết nối Kafka an toàn (SSL/TLS, SASL)
type: task
status: planned
created: 2025-08-12T14:09:47
updated: 2025-08-12T14:23:07
id: TASK-KAFKA-M4-S4-015
priority: high
memory_types: [procedural]
dependencies:
    - TASK-KAFKA-M4-S2-007-CONFIGURE-PRODUCER-SSL
    - TASK-KAFKA-M4-S2-008-CONFIGURE-CONSUMER-SSL
    - TASK-KAFKA-M4-S3-011-CONFIGURE-PRODUCER-SASL
    - TASK-KAFKA-M4-S3-012-CONFIGURE-CONSUMER-SASL
tags: [kafka, security, ssl, tls, sasl, scram, testing, integration-test, testcontainers]
---

## Mô tả

Nhiệm vụ này tập trung vào việc viết các integration test toàn diện để xác minh các kết nối an toàn đến Kafka bằng cả SSL/TLS và SASL/SCRAM. Các kiểm thử này sẽ đảm bảo rằng các ứng dụng Producer và Consumer có thể thiết lập thành công các kênh liên lạc an toàn và được xác thực với cluster Kafka. Testcontainers sẽ được sử dụng để tạo môi trường cô lập và có thể tái tạo với một thể hiện Kafka được bảo mật.

## Mục tiêu

*   Thiết lập môi trường kiểm thử bao gồm một broker Kafka được cấu hình cho SSL/TLS và SASL/SCRAM.
*   Triển khai các integration test để gửi tin nhắn bằng Producer được cấu hình cho các kết nối an toàn.
*   Triển khai các integration test để tiêu thụ tin nhắn bằng Consumer được cấu hình cho các kết nối an toàn.
*   Xác minh rằng tin nhắn được trao đổi thành công khi bảo mật được bật.
*   Kiểm thử các cấu hình bảo mật khác nhau (ví dụ: chỉ SSL, chỉ SASL, SASL_SSL).

## Danh sách kiểm tra

### Kiểm thử

- [ ] Tạo một lớp kiểm thử mới, ví dụ: `src/test/java/com/example/kafka/security/KafkaSecurityIntegrationTest.java`.
    - Ghi chú: Lớp này sẽ chứa các integration test tập trung vào bảo mật.
- [ ] **Thiết lập môi trường kiểm thử Kafka an toàn:**
    - [ ] Sử dụng Testcontainers để khởi tạo một broker Kafka với SSL/TLS và SASL/SCRAM được bật. Điều này có thể liên quan đến:
        - Gắn các file `server.properties` tùy chỉnh cho container Kafka.
        - Gắn các chứng chỉ SSL/TLS, keystore và truststore.
        - Cấu hình JAAS để xác thực SASL/SCRAM.
    - Ghi chú: Đây là phần phức tạp nhất của thiết lập. Đảm bảo hình ảnh Testcontainer Kafka hỗ trợ tất cả các tính năng bảo mật cần thiết.
- [ ] **Cấu hình Producer và Consumer kiểm thử:**
    - [ ] Khởi tạo `AvroUserProducer` và `AvroUserConsumer` (hoặc Kafka Producer/Consumer chung để đơn giản) trong kiểm thử với các cấu hình trỏ đến thể hiện Kafka đã được bảo mật của Testcontainer.
    - [ ] Bao gồm các thuộc tính `security.protocol`, `ssl.*` và `sasl.*` cần thiết cho mỗi client.
    - Ghi chú: Cấu hình client phải phản ánh những gì đã được thiết lập trong các nhiệm vụ trước (Nhiệm vụ 7, 8, 11, 12).
- [ ] **Triển khai các trường hợp kiểm thử cho SSL/TLS:**
    - [ ] Kiểm thử Producer gửi tin nhắn qua SSL/TLS.
    - [ ] Kiểm thử Consumer nhận tin nhắn qua SSL/TLS.
    - [ ] Xác minh việc trao đổi tin nhắn thành công.
    - Ghi chú: Đảm bảo keystore/truststore và mật khẩu của client là chính xác.
- [ ] **Triển khai các trường hợp kiểm thử cho SASL/SCRAM:**
    - [ ] Kiểm thử Producer gửi tin nhắn với xác thực SASL/SCRAM.
    - [ ] Kiểm thử Consumer nhận tin nhắn với xác thực SASL/SCRAM.
    - [ ] Xác minh việc trao đổi tin nhắn thành công với người dùng đã xác thực.
    - Ghi chú: Đảm bảo tên người dùng/mật khẩu chính xác trong `sasl.jaas.config`.
- [ ] **Triển khai các trường hợp kiểm thử cho SASL_SSL (Bảo mật kết hợp):**
    - [ ] Kiểm thử Producer gửi tin nhắn qua `SASL_SSL`.
    - [ ] Kiểm thử Consumer nhận tin nhắn qua `SASL_SSL`.
    - [ ] Xác minh giao tiếp đầu cuối.
    - Ghi chú: Đây là thiết lập sản xuất được khuyến nghị.
- [ ] **Triển khai các trường hợp kiểm thử tiêu cực (Tùy chọn nhưng được khuyến nghị):**
    - [ ] Cố gắng kết nối với thông tin xác thực không hợp lệ (sai mật khẩu, người dùng không tồn tại) và khẳng định rằng xác thực thất bại.
    - [ ] Cố gắng kết nối mà không có chứng chỉ SSL/TLS cần thiết và khẳng định rằng kết nối thất bại.
    - Ghi chú: Các kiểm thử tiêu cực đảm bảo rằng các cơ chế bảo mật hoạt động như mong đợi và từ chối truy cập trái phép.

## Tiến độ

### Kiểm thử

- [ ] Cấu trúc integration test bảo mật đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S2-007-CONFIGURE-PRODUCER-SSL`: Cấu hình SSL của Producer.
*   `TASK-KAFKA-M4-S2-008-CONFIGURE-CONSUMER-SSL`: Cấu hình SSL của Consumer.
*   `TASK-KAFKA-M4-S3-011-CONFIGURE-PRODUCER-SASL`: Cấu hình SASL của Producer.
*   `TASK-KAFKA-M4-S3-012-CONFIGURE-CONSUMER-SASL`: Cấu hình SASL của Consumer.

## Các cân nhắc chính

*   **Testcontainers cho Kafka an toàn:** Đây sẽ là một thiết lập Testcontainers phức tạp hơn. Tìm kiếm các ví dụ hiện có hoặc hình ảnh cơ sở tạo điều kiện triển khai Kafka an toàn. Bạn có thể cần xây dựng một hình ảnh Docker tùy chỉnh cho Kafka với bảo mật được cấu hình trước hoặc sử dụng các tính năng Testcontainers nâng cao để gắn file và đặt biến môi trường.
*   **Quản lý chứng chỉ trong kiểm thử:** Để kiểm thử, bạn có thể sử dụng các chứng chỉ tương tự được tạo trong Nhiệm vụ 5, đảm bảo chúng có thể truy cập được trong môi trường kiểm thử (ví dụ: bằng cách sao chép chúng vào Testcontainer).
*   **Ghi log:** Bật ghi log chi tiết cho các client và broker Kafka trong quá trình kiểm thử để hỗ trợ gỡ lỗi các vấn đề liên quan đến bảo mật.

## Ghi chú

*   **Awaitility:** Sử dụng Awaitility cho các khẳng định không đồng bộ, đặc biệt khi chờ tin nhắn được tiêu thụ.
*   **Quy tắc/Tiện ích mở rộng JUnit:** Tận dụng các tiện ích mở rộng JUnit 5 để quản lý vòng đời Testcontainers.

## Thảo luận

Kiểm thử các kết nối Kafka an toàn là tối quan trọng để đảm bảo rằng các biện pháp bảo mật đã triển khai hiệu quả và không gây ra các tác dụng phụ không mong muốn. Các integration test này cung cấp sự tin cậy rằng cluster Kafka được bảo vệ chống lại truy cập trái phép và dữ liệu đang truyền được an toàn. Nhiệm vụ này đại diện cho một bước xác thực quan trọng đối với các khía cạnh bảo mật của việc triển khai Kafka.

## Các bước tiếp theo

Sau khi các integration test bảo mật này hoàn tất, nhiệm vụ kiểm thử cuối cùng sẽ liên quan đến việc triển khai mô phỏng lỗi.

## Trạng thái hiện tại

Viết integration test cho các kết nối Kafka an toàn đang được lên kế hoạch.