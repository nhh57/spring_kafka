---
title: Triển khai mô phỏng lỗi trong integration test
type: task
status: planned
created: 2025-08-12T14:10:10
updated: 2025-08-12T14:24:22
id: TASK-KAFKA-M4-S4-016
priority: medium
memory_types: [procedural]
dependencies:
    - TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER
    - TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER
    - TASK-KAFKA-M4-S4-014-WRITE-INTEGRATION-TESTS-AVRO
    - TASK-KAFKA-M4-S4-015-WRITE-INTEGRATION-TESTS-SECURITY
tags: [kafka, testing, error-handling, integration-test, testcontainers]
---

## Mô tả

Nhiệm vụ này tập trung vào việc triển khai các integration test mô phỏng các điều kiện lỗi khác nhau trong hệ sinh thái Kafka, đặc biệt liên quan đến Avro Serdes và cấu hình bảo mật. Mục tiêu là xác minh rằng các ứng dụng Producer và Consumer xử lý các lỗi này một cách duyên dáng, bằng cách phục hồi, ghi log hoặc thất bại một cách có thể dự đoán được. Điều này mang lại sự tin cậy vào tính mạnh mẽ của hệ thống trước các kịch bản không mong muốn.

## Mục tiêu

*   Mô phỏng lỗi không tương thích schema trong quá trình tuần tự hóa/giải tuần tự hóa Avro.
*   Mô phỏng lỗi liên quan đến bảo mật (ví dụ: thông tin xác thực không hợp lệ, chứng chỉ hết hạn).
*   Xác minh rằng các ứng dụng ghi log các thông báo lỗi phù hợp.
*   Xác minh rằng các ứng dụng thể hiện hành vi mong đợi (ví dụ: cơ chế thử lại, tắt máy duyên dáng hoặc từ chối tin nhắn).

## Danh sách kiểm tra

### Kiểm thử

- [ ] Mở rộng các lớp integration test hiện có (ví dụ: `AvroSerdesIntegrationTest`, `KafkaSecurityIntegrationTest`) hoặc tạo các lớp mới để mô phỏng lỗi.
    - Ghi chú: Việc tái sử dụng các thiết lập Testcontainer hiện có sẽ hiệu quả.
- [ ] **Mô phỏng không tương thích Schema:**
    - [ ] Giới thiệu một thay đổi schema không tương thích ngược (ví dụ: xóa một trường bắt buộc, thay đổi kiểu dữ liệu của một trường hiện có) cho Producer, và cố gắng tiêu thụ bằng Consumer cũ.
    - [ ] Khẳng định rằng Consumer không thể giải tuần tự hóa tin nhắn và ghi log lỗi liên quan đến việc không khớp schema.
    - Ghi chú: Điều này kiểm thử khả năng của `KafkaAvroDeserializer` trong việc phát hiện các vấn đề về schema.
- [ ] **Mô phỏng lỗi bảo mật (Thông tin xác thực không hợp lệ):**
    - [ ] Cấu hình Producer/Consumer với tên người dùng/mật khẩu SASL không chính xác.
    - [ ] Khẳng định rằng nỗ lực kết nối thất bại với lỗi xác thực.
    - Ghi chú: Điều này xác minh rằng cấu hình SASL từ chối chính xác truy cập trái phép.
- [ ] **Mô phỏng lỗi bảo mật (Chứng chỉ hết hạn/không hợp lệ):**
    - [ ] (Nếu khả thi với Testcontainers) Cấu hình broker Kafka hoặc client với chứng chỉ SSL/TLS hết hạn hoặc không đáng tin cậy.
    - [ ] Khẳng định rằng nỗ lực kết nối thất bại với `SSLHandshakeException` hoặc lỗi xác thực chứng chỉ tương tự.
    - Ghi chú: Điều này đảm bảo rằng cấu hình SSL/TLS xác thực chính xác các chứng chỉ.
- [ ] **Mô phỏng không khả dụng của Broker (Tùy chọn):**
    - [ ] Trong quá trình kiểm thử, tạm thời dừng Testcontainer broker Kafka và quan sát hành vi của Producer/Consumer (ví dụ: thử lại, ghi log lỗi).
    - Ghi chú: Điều này kiểm thử khả năng phục hồi của các ứng dụng client đối với sự cố của broker.

## Tiến độ

### Kiểm thử

- [ ] Cấu trúc kiểm thử mô phỏng lỗi đã được tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-003-IMPLEMENT-AVRO-PRODUCER`: Triển khai Producer.
*   `TASK-KAFKA-M4-S1-004-IMPLEMENT-AVRO-CONSUMER`: Triển khai Consumer.
*   `TASK-KAFKA-M4-S4-014-WRITE-INTEGRATION-TESTS-AVRO`: Cung cấp thiết lập kiểm thử tích hợp Avro Serdes.
*   `TASK-KAFKA-M4-S4-015-WRITE-INTEGRATION-TESTS-SECURITY`: Cung cấp thiết lập kiểm thử tích hợp Kafka đã được bảo mật.

## Các cân nhắc chính

*   **Kiểm soát môi trường kiểm thử:** Testcontainers rất quan trọng để mô phỏng các điều kiện lỗi này một cách có kiểm soát và cô lập.
*   **Ghi log:** Đảm bảo ghi log toàn diện được bật trong mã ứng dụng và môi trường kiểm thử để nắm bắt chi tiết lỗi.
*   **Khẳng định:** Các khẳng định nên tập trung vào loại lỗi mong đợi, nội dung thông báo lỗi và hành vi tổng thể của ứng dụng (ví dụ: nó có bị crash, thử lại hay xử lý lỗi một cách duyên dáng không?).

## Ghi chú

*   **Kiểm thử tiến hóa Schema:** Mặc dù nhiệm vụ này tập trung vào các lỗi không tương thích, một kịch bản nâng cao hơn sẽ liên quan đến việc kiểm thử khả năng tương thích ngược và tiến với Schema Registry.
*   **Kỹ thuật hỗn loạn (Chaos Engineering):** Kiểm thử mô phỏng lỗi là một hình thức cơ bản của kỹ thuật hỗn loạn, giúp xác định các điểm yếu trong hệ thống.

## Thảo luận

Việc triển khai các kiểm thử mô phỏng lỗi là một biện pháp chủ động để nâng cao khả năng phục hồi của các ứng dụng Kafka. Bằng cách cố ý gây ra lỗi, chúng ta có thể xác thực tính mạnh mẽ của cấu hình Serdes và bảo mật, đảm bảo rằng hệ thống hoạt động có thể dự đoán được và phục hồi duyên dáng từ các điều kiện lỗi phổ biến và không phổ biến. Điều này củng cố độ tin cậy tổng thể của pipeline dữ liệu Kafka.

## Các bước tiếp theo

Với tất cả các nhiệm vụ kiểm thử đã được phác thảo, các nhiệm vụ cuối cùng sẽ liên quan đến tài liệu và dọn dẹp.

## Trạng thái hiện tại

Triển khai mô phỏng lỗi trong integration test đã được lên kế hoạch.