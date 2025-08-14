---
title: Giải quyết "Câu hỏi mở" từ TDD
type: task
status: planned
created: 2025-08-12T14:10:58
updated: 2025-08-12T14:25:56
id: TASK-KAFKA-M4-S5-018
priority: low
memory_types: [procedural, semantic]
dependencies: []
tags: [documentation, clarification, security, schema-evolution]
---

## Mô tả

Nhiệm vụ này giải quyết phần "Câu hỏi mở" được xác định trong Tài liệu thiết kế kỹ thuật cho Module 4 (`docs/technical_design/kafka_module4_coding_tdd.md`). Mục tiêu là cung cấp thêm làm rõ, hướng dẫn hoặc các ví dụ bổ sung để giải quyết các điểm mở này, nâng cao tính đầy đủ và tiện ích của tài liệu và ví dụ code của Module 4.

## Mục tiêu

*   Cung cấp hướng dẫn chi tiết về việc tạo và quản lý chứng chỉ SSL/TLS cho Kafka.
*   Làm rõ cách cấu hình các broker Kafka để bật SSL/TLS và SASL.
*   Cân nhắc bao gồm một ví dụ về Tiến hóa Schema và cách Schema Registry xử lý nó.

## Danh sách kiểm tra

### Tài liệu

- [ ] **Hướng dẫn quản lý chứng chỉ SSL/TLS:**
    - [ ] Tạo một hướng dẫn thiết lập chuyên dụng (ví dụ: `docs/setup_guides/kafka_ssl_tls_setup.md`) mô tả chi tiết:
        - Các bước để tạo chứng chỉ CA, broker và client bằng `keytool` hoặc OpenSSL.
        - Các phương pháp hay nhất để quản lý keystore và truststore.
        - Ghi chú: Hướng dẫn này phải đủ toàn diện để người dùng có thể thiết lập một cluster Kafka an toàn từ đầu.
- [ ] **Làm rõ cấu hình Kafka Broker cho SSL/TLS và SASL:**
    - [ ] Cập nhật `docs/setup_guides/kafka_ssl_tls_setup.md` hoặc tạo một phần mới trong `docs/kafka_coding_plan.md` (nếu ngắn gọn) phác thảo rõ ràng các cấu hình `server.properties` cần thiết cho SSL/TLS và SASL.
    - Ghi chú: Điều này sẽ bao gồm các thuộc tính `listeners`, `ssl.*`, `sasl.*` và cấu hình JAAS.
- [ ] **Cân nhắc ví dụ về Tiến hóa Schema:**
    - [ ] Đánh giá tính khả thi và phạm vi của việc thêm một ví dụ đơn giản thể hiện tiến hóa schema (ví dụ: thêm một trường tùy chọn vào `User.avsc`).
    - [ ] Nếu khả thi, triển khai thay đổi schema, cập nhật Avro Producer để gửi tin nhắn với schema mới, và xác minh rằng Consumer cũ vẫn có thể đọc chúng (tương thích ngược).
    - [ ] Tài liệu hóa ví dụ này và ý nghĩa của nó trong `docs/kafka_coding_plan.md` hoặc một tài liệu chuyên dụng mới.
    - Ghi chú: Đây là một chủ đề nâng cao hơn nhưng rất liên quan đến việc sử dụng Avro trong thế giới thực.

## Tiến độ

### Tài liệu

- [ ] Giải quyết các câu hỏi mở đã được khởi tạo.

## Phụ thuộc

Không có trực tiếp, nhưng nhiệm vụ này xây dựng dựa trên việc triển khai bảo mật và Avro Serdes.

## Các cân nhắc chính

*   **Trải nghiệm người dùng:** Mục tiêu chính là làm cho trải nghiệm học tập mượt mà và toàn diện hơn cho người dùng.
*   **Phạm vi mở rộng:** Khi giải quyết các câu hỏi mở, hãy lưu ý không đưa quá nhiều sự phức tạp hoặc các tính năng mới có thể mở rộng đáng kể phạm vi của Module 4. Ưu tiên sự rõ ràng và hiểu biết cơ bản.
*   **Tham chiếu chéo:** Đảm bảo rằng mọi tài liệu mới được tạo đều được liên kết chính xác từ `docs/kafka_coding_plan.md` và TDD.

## Ghi chú

*   Ví dụ về Tiến hóa Schema có thể yêu cầu cập nhật schema Avro, tạo lại các lớp và thể hiện cách `KafkaAvroDeserializer` xử lý các thay đổi dựa trên các quy tắc tương thích.

## Thảo luận

Giải quyết các câu hỏi mở là một bước quan trọng trong việc tinh chỉnh tài liệu giáo dục. Nó đảm bảo rằng các điểm mơ hồ phổ biến hoặc các lĩnh vực cần giải thích sâu hơn được đề cập, cuối cùng dẫn đến một nguồn tài nguyên học tập hoàn chỉnh và có giá trị hơn. Quyết định bao gồm Tiến hóa Schema sẽ phụ thuộc vào việc cân bằng tầm quan trọng của nó với độ phức tạp tổng thể của module.

## Các bước tiếp theo

Với nhiệm vụ này, tất cả các nhiệm vụ tài liệu và dọn dẹp đã được lên kế hoạch sẽ được phác thảo. Sau khi hoàn thành, toàn bộ quá trình phân tách sẽ hoàn tất.

## Trạng thái hiện tại

Giải quyết "Câu hỏi mở" từ TDD đã được lên kế hoạch.