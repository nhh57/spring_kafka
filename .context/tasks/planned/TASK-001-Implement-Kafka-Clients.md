---
title: "Module 2: Triển khai Producer và Consumer Kafka Cơ bản"
type: task
status: planned
created: 2025-08-11T07:59:42.311Z
updated: 2025-08-11T08:06:33.099Z
id: TASK-001
priority: high
memory_types: [procedural]
dependencies: []
tags: [kafka, java, producer, consumer, module-2]
---

## Mô tả

Nhiệm vụ này bao gồm việc triển khai thực tế của thiết kế kỹ thuật được chỉ định trong `docs/technical_design/kafka_module2_coding_tdd.md`. Mục tiêu là xây dựng các ứng dụng client producer và consumer Kafka cơ bản bằng thư viện Java `kafka-clients` tiêu chuẩn để hiểu kiến trúc và cấu hình cốt lõi của chúng.

## Mục tiêu

- Xây dựng một ứng dụng Java Producer độc lập.
- Cấu hình và kiểm thử các cài đặt producer khác nhau (`acks`, `retries`, `idempotence`).
- Xây dựng một ứng dụng Java Consumer độc lập.
- Triển khai việc commit offset thủ công.
- Quan sát và hiểu cơ chế consumer group rebalancing.

## Checklist (Danh sách kiểm tra)

### **Giai đoạn 1: Thiết lập Dự án & Triển khai Producer**

- [x] **Nhiệm vụ 1.1: Xác minh Môi trường & Dependencies** (Đã hoàn thành)
    - **Vị trí:** Tệp `build.gradle` ở thư mục gốc.
    - **Mục tiêu:** Xác nhận rằng các dependencies cần thiết đã được bao gồm cho tất cả các dự án con.
    - **Checklist:**
        - [ ] Xác minh `org.apache.kafka:kafka-clients:3.6.1` đã có.
        - [ ] Xác minh `org.slf4j:slf4j-api` và `ch.qos.logback:logback-classic` đã có để ghi log.
    - **Ghi chú:** Bước này đảm bảo dự án đã sẵn sàng để phát triển. Các dependencies được kế thừa bởi `producer-service` và `consumer-service`, vì vậy không cần thay đổi trong các tệp `build.gradle` tương ứng của chúng.

- [x] **Nhiệm vụ 1.2: Triển khai Producer Cơ bản (`MessageProducerService`)** (Đã hoàn thành)
    - **Tệp:** `producer-service/src/main/java/com/example/kafka/producer/MessageProducerService.java`
    - **Mục tiêu:** Thiết lập một producer cơ bản với cấu hình tối thiểu để học ban đầu.
    - **Checklist:**
        - [ ] **Constructor:** Trong constructor của `MessageProducerService`, cấu hình đối tượng `Properties`.
        - [ ] **Thuộc tính `bootstrap.servers`:** Đặt thành `"localhost:9092"`.
        - [ ] **Thuộc tính `key.serializer`:** Đặt thành `StringSerializer.class.getName()`.
        - [ ] **Thuộc tính `value.serializer`:** Đặt thành `StringSerializer.class.getName()`.
        - [ ] **Thuộc tính `acks`:** Đặt thành `"1"`. Đây là cấu hình cơ bản của chúng ta. Nó cung cấp sự cân bằng giữa hiệu suất và độ bền, nhưng có thể dẫn đến mất dữ liệu nếu leader bị lỗi trước khi sao chép.
        - [ ] **Loại bỏ Cài đặt Nâng cao:** Đảm bảo `enable.idempotence`, `retries`, và `retry.backoff.ms` được chú thích hoặc loại bỏ cho việc triển khai cơ bản này.
    - **Ghi chú:** Bắt đầu với `acks=1` là một mặc định phổ biến. Nó cho phép chúng ta sau này chứng minh rõ ràng lợi ích của `acks=all` và idempotence cho các ứng dụng quan trọng về dữ liệu.

### **Giai đoạn 2: Cấu hình Producer & Thử nghiệm Độ bền**

- [x] **Nhiệm vụ 2.1: Thử nghiệm với Cấu hình `acks`** (Đã hoàn thành)
    - **Tệp:** `producer-service/src/main/java/com/example/kafka/producer/MessageProducerService.java`
    - **Mục tiêu:** Hiểu sự đánh đổi giữa hiệu suất và độ bền cho mỗi cài đặt `acks`.
    - **Checklist:**
        - [ ] **Đặt `acks=0`:** Sửa đổi `props.put(ProducerConfig.ACKS_CONFIG, "0");`. Chạy producer.
            - **Quan sát:** Producer gửi tin nhắn mà không cần chờ bất kỳ xác nhận nào từ broker. Đây là "fire and forget" - thông lượng cao nhất, nhưng rủi ro mất dữ liệu cao nhất.
        - [ ] **Đặt `acks=1`:** Hoàn nguyên về `props.put(ProducerConfig.ACKS_CONFIG, "1");`. Chạy producer.
            - **Quan sát:** Producer chờ xác nhận chỉ từ leader của phân vùng. Hiệu suất tốt, nhưng dữ liệu có thể bị mất nếu leader gặp sự cố trước khi các follower sao chép dữ liệu.
        - [ ] **Đặt `acks=all`:** Sửa đổi `props.put(ProducerConfig.ACKS_CONFIG, "all");`. Chạy producer.
            - **Quan sát:** Producer chờ xác nhận từ leader và tất cả các replica đồng bộ. Điều này cung cấp đảm bảo độ bền cao nhất nhưng có độ trễ cao hơn.
    - **Ghi chú:** Đây là một thử nghiệm thực hành. Mục tiêu là quan sát log và "cảm nhận" sự khác biệt về độ trễ, củng cố các khái niệm lý thuyết.

- [x] **Nhiệmệm vụ 2.2: Kiểm tra Idempotent Producer** (Đã hoàn thành)
    - **Tệp:** `producer-service/src/main/java/com/example/kafka/producer/MessageProducerService.java`
    - **Mục tiêu:** Ngăn chặn việc trùng lặp tin nhắn trong quá trình thử lại bằng cách bật idempotent producer.
    - **Checklist:**
        - [ ] **Bật Idempotence:** Thêm `props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");`.
    - **Ghi chú:**
        - **Tại sao:** Khi `enable.idempotence` là `true`, Kafka đảm bảo rằng việc thử lại sẽ không dẫn đến các tin nhắn trùng lặp trong log. Nó hoạt động bằng cách gán một ID Producer (PID) duy nhất và một số thứ tự cho mỗi tin nhắn.
        - **Thực hành tốt nhất:** Đây là một cài đặt quan trọng cho ngữ nghĩa "exactly-once" (EOS) và nên được bật cho hầu hết các trường hợp sử dụng trong sản xuất để ngăn chặn việc trùng lặp dữ liệu. Nó ngầm đặt `acks` thành `all` và cấu hình một giá trị `retries` an toàn.

### **Giai đoạn 3: Triển khai Consumer**

- [x] **Nhiệm vụ 3.1: Triển khai Consumer Cơ bản (`MessageConsumerService`)** (Đã hoàn thành)
    - **Tệp:** `consumer-service/src/main/java/com/example/kafka/consumer/MessageConsumerService.java`
    - **Mục tiêu:** Tạo một consumer cơ bản tự động commit offset.
    - **Checklist:**
        - [ ] **Constructor:** Trong constructor của `MessageConsumerService`, cấu hình đối tượng `Properties`.
        - [ ] **Thuộc tính `bootstrap.servers`:** Đặt thành `"localhost:9092"`.
        - [ ] **Thuộc tính `group.id`:** Đặt một tên mô tả, ví dụ: `"learning-group"`.
        - [ ] **Thuộc tính `key.deserializer`:** Đặt thành `StringDeserializer.class.getName()`.
        - [ ] **Thuộc tính `value.deserializer`:** Đặt thành `StringDeserializer.class.getName()`.
        - [ ] **Thuộc tính `auto.offset.reset`:** Đặt thành `"earliest"` để đảm bảo consumer đọc từ đầu topic trong lần chạy đầu tiên.
        - [ ] **Thuộc tính `enable.auto.commit`:** Đặt thành `"true"`.
        - [ ] **Vòng lặp Polling:** Trong phương thức `run()`, đảm bảo lệnh gọi `consumer.commitSync()` hoặc `commitAsync()` được loại bỏ hoặc chú thích.
    - **Ghi chú:** Tự động commit tiện lợi nhưng có rủi ro. Một consumer có thể commit một offset trước khi tin nhắn được xử lý hoàn toàn. Nếu consumer gặp sự cố sau khi commit nhưng trước khi xử lý xong, tin nhắn sẽ bị mất mãi mãi. Đây là lý do tại sao commit thủ công được ưu tiên trong môi trường sản xuất.

### **Giai đoạn 4: Quản lý Offset Thủ công & Consumer Groups**

- [x] **Nhiệm vụ 4.1: Triển khai Commit Offset Thủ công** (Đã hoàn thành)
    - **Tệp:** `consumer-service/src/main/java/com/example/kafka/consumer/MessageConsumerService.java`
    - **Mục tiêu:** Giành toàn quyền kiểm soát các đảm bảo xử lý tin nhắn bằng cách quản lý offset thủ công.
    - **Checklist:**
        - [ ] **Tắt Tự động Commit:** Thay đổi `enable.auto.commit` thành `"false"`.
        - [ ] **Triển khai `commitSync`:** Sau vòng lặp `for` xử lý các bản ghi, thêm một kiểm tra `if (!records.isEmpty())` và bên trong nó, gọi `consumer.commitSync()`.
    - **Ghi chú:**
        - **Tại sao:** Commit thủ công đảm bảo rằng chúng ta chỉ xác nhận các tin nhắn sau khi chúng đã được xử lý thành công (ví dụ: lưu vào cơ sở dữ liệu, gửi đến một dịch vụ khác). Đây là nền tảng của xử lý "at-least-once".
        - **`commitSync` vs. `commitAsync`:** `commitSync` là blocking và sẽ thử lại cho đến khi commit thành công hoặc gặp lỗi không thể thử lại. Nó đơn giản hơn để sử dụng nhưng có thể ảnh hưởng đến thông lượng. `commitAsync` là non-blocking và sử dụng callback, cung cấp hiệu suất tốt hơn nhưng đòi hỏi xử lý lỗi phức tạp hơn.

- [ ] **Nhiệm vụ 4.2: Kiểm tra Consumer Group Rebalancing** (Sẵn sàng để người dùng thử nghiệm)
    - **Mục tiêu:** Quan sát cách Kafka tự động phân phối các phân vùng giữa nhiều consumer trong cùng một nhóm.
    - **Checklist:**
        - [ ] Chạy một instance của `ConsumerApp`. Quan sát log gán phân vùng.
        - [ ] Chạy một instance thứ hai của `ConsumerApp` trong một terminal mới, sử dụng cùng `group.id`.
        - [ ] Quan sát log của cả hai instance. Bạn sẽ thấy một quá trình rebalance xảy ra, với các phân vùng được phân phối lại giữa hai consumer.
    - **Ghi chú:** Điều này chứng tỏ khả năng mở rộng theo chiều ngang của Kafka cho các consumer. Số lượng consumer hoạt động tối đa trong một nhóm bị giới hạn bởi số lượng phân vùng trong topic.

## Các Vấn đề Cần Lưu ý

- **Tập trung vào Client API:** Việc triển khai phải sử dụng API `org.apache.kafka.clients` thô, không phải các abstraction của Spring Kafka, như đã chỉ định trong TDD.
- **Quản lý Cấu hình:** Cấu hình nên được quản lý thông qua các đối tượng `java.util.Properties` hoặc `java.util.Map`.
- **Xử lý Lỗi:** Nên bao gồm xử lý lỗi cơ bản (ví dụ: ghi log ngoại lệ), nhưng logic retry/DLQ phức tạp nằm ngoài phạm vi của module này.

## Thảo luận

Kế hoạch này ánh xạ trực tiếp đến các yêu cầu chức năng của TDD. Bằng cách xây dựng các client này từ đầu, người học sẽ có được sự hiểu biết cơ bản về cách các thành phần cốt lõi của Kafka tương tác, điều này rất cần thiết trước khi chuyển sang các abstraction cấp cao hơn như Spring Kafka. Các thử nghiệm được thiết kế để cung cấp phản hồi hữu hình về cách các cấu hình khác nhau ảnh hưởng đến độ tin cậy và hành vi.

## Tình trạng Hiện tại

- **`2025-08-11T08:06:33.099Z`**: Kế hoạch nhiệm vụ được cập nhật với các bước chi tiết, cụ thể cho việc triển khai và học tập. Chờ thực hiện.