---
title: Cấu hình Producer và đảm bảo phân phối
type: task
status: planned
created: 2025-08-10T12:26:16
updated: 2025-08-10T12:26:16
id: TASK-KAFKA-M2-US2-PRODUCER-CONFIG
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M2-US1-BASIC-PRODUCER]
tags: [kafka, producer, acks, retries, idempotence, durability, fault-tolerance]
---

# Cấu hình Producer và đảm bảo phân phối

## Description
Là một người học, tôi muốn có thể cấu hình Producer với các tùy chọn `acks`, `retries`, và `enable.idempotence` để hiểu ảnh hưởng của chúng đến độ bền và hiệu suất.

## Objectives
*   Cấu hình Producer để gửi tin nhắn với các giá trị `acks` khác nhau.
*   Bật/tắt `retries` và `enable.idempotence` và quan sát hành vi.
*   Hiểu cách các cấu hình này ảnh hưởng đến ngữ nghĩa phân phối (at-most-once, at-least-once, exactly-once).

## Checklist
### Producer Configuration
- [x] Viết code để cấu hình Producer với `acks=0`.
    - **File Location**: `src/main/java/com/example/kafka/ProducerConfigExample.java` (trong phương thức `main` hoặc một phương thức riêng).
    - **Notes**: Hướng dẫn thêm cấu hình `props.put("acks", "0");` vào `Properties` object trước khi khởi tạo `KafkaProducer`.
    ```java
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", StringSerializer.class.getName());
    props.put("acks", "0"); // Cấu hình acks=0
    KafkaProducer<String, String> producer = new KafkaProducer<>(props);
    ```
    - **Considerations**: Nhấn mạnh rằng đây là cấu hình cho thông lượng cao nhất nhưng rủi ro mất dữ liệu cao nhất. Thích hợp cho các trường hợp mà việc mất một vài tin nhắn là chấp nhận được (ví dụ: log không quan trọng).
    - **Best Practices**: Chỉ sử dụng cho dữ liệu không quan trọng.
    - **Common Pitfalls**: Mất tin nhắn nếu broker gặp sự cố ngay sau khi nhận, hoặc nếu network bị gián đoạn. Không có cách nào để biết liệu tin nhắn đã được broker nhận thành công hay chưa.
- [x] Viết code để cấu hình Producer với `acks=1`.
    - **File Location**: `src/main/java/com/example/kafka/ProducerConfigExample.java`
    - **Notes**: Thay đổi cấu hình `props.put("acks", "1");`. Giải thích `acks=1` có nghĩa là Producer chờ xác nhận từ leader broker rằng tin nhắn đã được ghi vào log của leader.
    ```java
    props.put("acks", "1"); // Cấu hình acks=1
    ```
    - **Considerations**: Cấu hình này cung cấp sự cân bằng giữa thông lượng và độ bền. Có thể mất dữ liệu nếu leader crash trước khi các follower kịp sao chép tin nhắn.
    - **Best Practices**: Phù hợp cho nhiều trường hợp sử dụng khi mất một lượng nhỏ dữ liệu là chấp nhận được trong các tình huống lỗi hiếm gặp.
    - **Common Pitfalls**: Mất dữ liệu nếu leader bị lỗi ngay sau khi ghi tin nhắn nhưng trước khi các follower sao chép kịp.
- [x] Viết code để cấu hình Producer với `acks=all` (hoặc `-1`).
    - **File Location**: `src/main/java/com/example/kafka/ProducerConfigExample.java`
    - **Notes**: Thay đổi cấu hình `props.put("acks", "all");`. Giải thích `acks=all` có nghĩa là Producer chờ xác nhận từ leader VÀ tất cả các In-Sync Replicas (ISR) rằng tin nhắn đã được ghi thành công vào log của chúng.
    ```java
    props.put("acks", "all"); // Cấu hình acks=all
    ```
    - **Considerations**: Đây là cấu hình cho độ bền dữ liệu cao nhất nhưng độ trễ cao nhất và thông lượng thấp nhất. Nó đảm bảo rằng tin nhắn sẽ không bị mất chừng nào số lượng replica in-sync còn lại lớn hơn hoặc bằng `min.insync.replicas`.
    - **Best Practices**: Bắt buộc cho các dữ liệu quan trọng hoặc giao dịch tài chính. Cần kết hợp với `min.insync.replicas` trên broker/topic để đảm bảo số lượng bản sao tối thiểu.
    - **Common Pitfalls**: Giảm hiệu suất đáng kể, có thể bị chặn nếu không đủ ISR (do broker bị lỗi hoặc network kém), dẫn đến `NotEnoughReplicasException`.
- [x] Viết code để cấu hình Producer với `retries` và `retry.backoff.ms`.
    - **File Location**: `src/main/java/com/example/kafka/ProducerConfigExample.java`
    - **Notes**: Thêm cấu hình `props.put("retries", 3);` và `props.put("retry.backoff.ms", 1000);`. Giải thích `retries` là số lần Producer sẽ thử lại khi gặp lỗi tạm thời (ví dụ: `LeaderNotAvailableException`, lỗi mạng, hoặc `NotEnoughReplicasException`). `retry.backoff.ms` là thời gian chờ giữa các lần thử lại.
    ```java
    props.put("retries", 3); // Thử lại 3 lần
    props.put("retry.backoff.ms", 1000); // Chờ 1 giây giữa các lần thử lại
    ```
    - **Considerations**: `retries` giúp tăng khả năng chịu lỗi và độ tin cậy của Producer. Tuy nhiên, nếu không được cấu hình cẩn thận (đặc biệt là khi `enable.idempotence` không bật), `retries` có thể dẫn đến trùng lặp tin nhắn.
    - **Best Practices**: Luôn bật `retries` cho các ứng dụng production. Đặt giá trị `retries` đủ lớn để xử lý các lỗi tạm thời nhưng không quá lớn để gây trễ.
    - **Common Pitfalls**: Trùng lặp tin nhắn nếu `enable.idempotence` không bật. Thử lại quá nhanh có thể làm trầm trọng thêm vấn đề nếu broker đang bị quá tải.
- [x] Viết code để cấu hình Producer với `enable.idempotence=true`.
    - **File Location**: `src/main/java/com/example/kafka/ProducerConfigExample.java`
    - **Notes**: Thêm cấu hình `props.put("enable.idempotence", "true");`. Giải thích `enable.idempotence=true` đảm bảo tin nhắn được ghi chính xác một lần (exactly-once delivery) trong phạm vi session của Producer, ngay cả khi có lỗi mạng hoặc thử lại. Kafka Broker sẽ sử dụng một ID Producer duy nhất và số thứ tự để phát hiện và loại bỏ các tin nhắn trùng lặp.
    ```java
    props.put("enable.idempotence", "true"); // Bật tính năng bất biến
    ```
    - **Considerations**: Yêu cầu `acks=all` và `retries > 0`. Nếu không, Kafka sẽ báo lỗi cấu hình.
    - **Best Practices**: Rất khuyến nghị cho các trường hợp cần đảm bảo chính xác một lần. Giúp đơn giản hóa việc xử lý trùng lặp ở phía Consumer.
    - **Common Pitfalls**: Ảnh hưởng nhẹ đến thông lượng (do overhead của việc theo dõi trạng thái idempotence). Không cung cấp đảm bảo `exactly-once` đầu cuối nếu các Consumer không được thiết kế `idempotent`.

## Progress
*   **Producer Configuration**: [ ]

## Dependencies
*   TASK-KAFKA-M2-US1-BASIC-PRODUCER (Đã có thể xây dựng Kafka Producer cơ bản).
*   Java Development Kit (JDK) 21 đã được cài đặt.
*   Gradle đã được cài đặt.

## Key Considerations
*   **Ngữ nghĩa phân phối**: Hiểu rõ sự khác biệt giữa at-most-once, at-least-once, và exactly-once và cách các cấu hình Producer hỗ trợ chúng.
*   **Đánh đổi hiệu suất và độ bền**: Các cấu hình bảo đảm phân phối cao hơn thường đi kèm với chi phí hiệu suất.

## Notes
*   Giải thích cách mô phỏng lỗi broker để kiểm tra các cấu hình này (ví dụ: tắt/khởi động lại container Kafka).
*   Nhấn mạnh tầm quan trọng của việc giám sát các chỉ số Producer (`request-latency-avg`, `record-error-rate`) để đánh giá hiệu suất và hành vi.
*   **Sử dụng SLF4J**: Khuyến nghị sử dụng SLF4J để ghi log thay vì `System.out.println` để có khả năng cấu hình logging linh hoạt hơn. Thêm khai báo `private static final Logger log = LoggerFactory.getLogger(ProducerConfigExample.class);` vào class và sử dụng `log.info()`, `log.error()`.

## Discussion
Việc cấu hình Producer đúng cách là yếu tố then chốt để xây dựng các ứng dụng Kafka đáng tin cậy và hiệu quả, đảm bảo dữ liệu không bị mất hoặc trùng lặp.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có khả năng cấu hình Kafka Producer để đáp ứng các yêu cầu về độ bền dữ liệu và ngữ nghĩa phân phối.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho cấu hình Producer.