---
title: Cập nhật docs/kafka_coding_plan.md với các liên kết đến ví dụ code và hướng dẫn thiết lập mới
type: task
status: planned
created: 2025-08-12T14:10:33
updated: 2025-08-12T14:25:20
id: TASK-KAFKA-M4-S5-017
priority: low
memory_types: [procedural]
dependencies:
    - TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA
    - TASK-KAFKA-M4-S1-002-CONFIGURE-AVRO-COMPILER
    - TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER
    - TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER
    - TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS
    - TASK-KAFKA-M4-S2-006-CONFIGURE-BROKER-SSL
    - TASK-KAFKA-M4-S2-007-CONFIGURE-PRODUCER-SSL
    - TASK-KAFKA-M4-S2-008-CONFIGURE-CONSUMER-SSL
    - TASK-KAFKA-M4-S3-009-CONFIGURE-BROKER-SASL
    - TASK-KAFKA-M4-S3-010-CREATE-SASL-USERS
    - TASK-KAFKA-M4-S3-011-CONFIGURE-PRODUCER-SASL
    - TASK-KAFKA-M4-S3-012-CONFIGURE-CONSUMER-SASL
    - TASK-KAFKA-M4-S4-013-WRITE-UNIT-TESTS
    - TASK-KAFKA-M4-S4-014-WRITE-INTEGRATION-TESTS-AVRO
    - TASK-KAFKA-M4-S4-015-WRITE-INTEGRATION-TESTS-SECURITY
    - TASK-KAFKA-M4-S4-016-IMPLEMENT-ERROR-SIMULATION-TESTS
tags: [documentation, update]
---

## Mô tả

Nhiệm vụ này liên quan đến việc cập nhật tài liệu kế hoạch code Kafka chính (`docs/kafka_coding_plan.md`) để phản ánh nội dung và các ví dụ mới được phát triển cho Module 4. Điều này đảm bảo rằng tài liệu được cập nhật và cung cấp hướng dẫn rõ ràng cho người dùng để truy cập và sử dụng các ví dụ code và hướng dẫn thiết lập mới được tạo.

## Mục tiêu

*   Thêm các phần mới hoặc cập nhật các phần hiện có trong `docs/kafka_coding_plan.md` cho Module 4.
*   Bao gồm các liên kết đến code ví dụ Avro Producer/Consumer mới được tạo.
*   Bao gồm các liên kết đến bất kỳ hướng dẫn thiết lập mới nào cho cấu hình Schema Registry, SSL/TLS và SASL.
*   Cung cấp hướng dẫn rõ ràng về cách chạy và kiểm thử các ví dụ mới.

## Danh sách kiểm tra

### Tài liệu

- [ ] Mở `docs/kafka_coding_plan.md`.
- [ ] Điều hướng đến phần "Module 4: Advanced Kafka Concepts & Operations - Thực hành".
- [ ] **Cập nhật Phần 4.1 (Serdes với Avro):**
    - [ ] Thêm liên kết đến file schema `User.avsc`.
    - [ ] Thêm liên kết đến các file ví dụ `AvroUserProducer.java` và `AvroUserConsumer.java`.
    - [ ] Cung cấp hướng dẫn về cách thiết lập và chạy Schema Registry (nếu chưa được đề cập).
    - [ ] Giải thích cách biên dịch schema Avro và chạy các ví dụ Producer/Consumer.
- [ ] **Cập nhật Phần 4.2 (Bảo mật Kafka):**
    - [ ] Thêm liên kết đến bất kỳ hướng dẫn thiết lập mới nào để tạo chứng chỉ SSL/TLS và cấu hình broker cho SSL/TLS và SASL.
    - [ ] Thêm liên kết đến các ví dụ code Producer và Consumer đã cập nhật bao gồm cấu hình SSL/TLS và SASL.
    - [ ] Cung cấp hướng dẫn về cách cấu hình và chạy các ví dụ Producer/Consumer an toàn.
- [ ] **Thêm phần phụ "Kiểm thử" cho Module 4:**
    - [ ] Liên kết đến các file unit test (`AvroUserProducerTest.java`, `AvroUserConsumerTest.java`).
    - [ ] Liên kết đến các file integration test (`AvroSerdesIntegrationTest.java`, `KafkaSecurityIntegrationTest.java`, `ErrorSimulationIntegrationTest.java`).
    - [ ] Giải thích ngắn gọn cách chạy các kiểm thử này.
- [ ] Xem lại toàn bộ phần Module 4 để đảm bảo rõ ràng, đầy đủ và chính xác.

## Tiến độ

### Tài liệu

- [ ] Cập nhật `docs/kafka_coding_plan.md` đã được khởi tạo.

## Phụ thuộc

Nhiệm vụ này phụ thuộc vào việc hoàn thành tất cả các nhiệm vụ triển khai code và kiểm thử cho Module 4, vì nó ghi lại những nỗ lực đó.

## Các cân nhắc chính

*   **Rõ ràng:** Đảm bảo hướng dẫn dễ hiểu cho người mới làm quen với dự án.
*   **Hoàn chỉnh:** Tất cả các ví dụ code, file cấu hình và các bước thiết lập có liên quan đều phải được tham chiếu.
*   **Tính nhất quán:** Duy trì phong cách và giọng điệu hiện có của `docs/kafka_coding_plan.md`.
*   **Khả năng truy cập:** Đảm bảo tất cả các file được liên kết đều có thể truy cập và được tham chiếu chính xác.

## Ghi chú

*   Nhiệm vụ này có thể liên quan đến việc tạo các hướng dẫn thiết lập mới (ví dụ: `docs/setup_guides/kafka_ssl_sasl_setup.md`) nếu chi tiết về việc tạo chứng chỉ và cấu hình broker quá rộng để đưa trực tiếp vào `kafka_coding_plan.md`.

## Thảo luận

Duy trì tài liệu cập nhật và toàn diện là rất quan trọng như việc viết mã sạch. Nhiệm vụ này đảm bảo rằng các ví dụ và cấu hình có giá trị được phát triển cho Module 4 dễ dàng được khám phá và sử dụng bởi những người khác. Tài liệu rõ ràng làm giảm đáng kể rào cản gia nhập cho người dùng mới và tạo điều kiện chuyển giao kiến thức trong nhóm.

## Các bước tiếp theo

Nhiệm vụ cuối cùng sẽ là giải quyết bất kỳ "Câu hỏi mở" nào từ Tài liệu thiết kế kỹ thuật cho Module 4.

## Trạng thái hiện tại

Cập nhật `docs/kafka_coding_plan.md` đã được lên kế hoạch.