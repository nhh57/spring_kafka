---
title: Quản lý Consumer Group và Offset thủ công
type: task
status: planned
created: 2025-08-10T12:26:54
updated: 2025-08-10T12:26:54
id: TASK-KAFKA-M2-US4-OFFSET-MANAGEMENT
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M2-US3-BASIC-CONSUMER]
tags: [kafka, consumer, offset, commit, consumer-group, rebalance]
---

# Quản lý Consumer Group và Offset thủ công

## Description
Là một người học, tôi muốn có thể quản lý offset thủ công trong Consumer để kiểm soát chính xác việc commit tiến độ và hiểu cơ chế rebalance.

## Objectives
*   Cấu hình Consumer để tắt `enable.auto.commit`.
*   Sử dụng `consumer.commitSync()` và `consumer.commitAsync()` để commit offset.
*   Mô phỏng consumer crash và quan sát hành vi phục hồi từ offset đã commit.
*   Chạy nhiều Consumer trong cùng một group để quan sát phân vùng được gán và cơ chế rebalance.

## Checklist
### Offset Management
- [x] Viết code để cấu hình Consumer với `enable.auto.commit=false`.
    - **File Location**: `src/main/java/com/example/kafka/OffsetManagementConsumer.java` (trong phương thức `main` hoặc một phương thức riêng).
    - **Notes**: Hướng dẫn thêm cấu hình `props.put("enable.auto.commit", "false");` vào `Properties` object trước khi khởi tạo `KafkaConsumer`. Giải thích tại sao việc tắt auto-commit lại quan trọng trong môi trường sản xuất để tránh mất dữ liệu hoặc trùng lặp.
    ```java
    props.put("enable.auto.commit", "false");
    ```
    - **Considerations**: Nhấn mạnh rằng điều này đặt trách nhiệm commit offset lên vai lập trình viên. Thảo luận về sự đánh đổi giữa sự tiện lợi của auto-commit và khả năng kiểm soát chính xác của commit thủ công.
    - **Best Practices**: Luôn tắt auto-commit cho các ứng dụng quan trọng, nơi tính toàn vẹn dữ liệu là tối quan trọng.
    - **Common Pitfalls**: Quên commit offset sau khi xử lý, dẫn đến việc đọc lại các tin nhắn đã xử lý khi consumer khởi động lại.
- [x] Viết code để sử dụng `consumer.commitSync()`.
    - **File Location**: `src/main/java/com/example/kafka/OffsetManagementConsumer.java` (trong vòng lặp poll).
    - **Notes**: Hướng dẫn sử dụng `consumer.commitSync()` sau khi xử lý một batch tin nhắn. Giải thích `commitSync()` là phương thức đồng bộ, chặn luồng của consumer cho đến khi offset được commit thành công hoặc gặp lỗi. Nó đảm bảo offset được lưu trữ bền vững trước khi consumer tiếp tục xử lý các tin nhắn mới.
    ```java
    // ... xử lý tin nhắn ...
    try {
        consumer.commitSync();
    } catch (KafkaException e) {
        log.error("Error committing offset synchronously: {}", e.getMessage());
        // Xử lý lỗi commit (ví dụ: ghi log, thoát ứng dụng)
    }
    ```
    - **Considerations**: Thích hợp khi cần đảm bảo offset đã được lưu trước khi tiếp tục xử lý, ví dụ: trước khi ghi dữ liệu đã xử lý vào database.
    - **Best Practices**: Sử dụng trong khối `try-catch` để xử lý `KafkaException` có thể xảy ra trong quá trình commit.
    - **Common Pitfalls**: Gây tắc nghẽn nếu commit quá thường xuyên (ảnh hưởng đến thông lượng) hoặc nếu có vấn đề mạng/broker gây trễ trong quá trình commit.
- [x] Viết code để sử dụng `consumer.commitAsync()`.
    - **File Location**: `src/main/java/com/example/kafka/OffsetManagementConsumer.java` (trong vòng lặp poll).
    - **Notes**: Hướng dẫn sử dụng `consumer.commitAsync()` với một `OffsetCommitCallback`. Giải thích `commitAsync()` là phương thức bất đồng bộ, không chặn luồng của consumer. Nó cho phép consumer tiếp tục poll tin nhắn mới trong khi quá trình commit diễn ra ngầm.
    ```java
    // ... xử lý tin nhắn ...
    consumer.commitAsync((offsets, exception) -> {
        if (exception != null) {
            log.error("Error committing offset asynchronously: {}", exception.getMessage());
            // Xử lý lỗi commit không đồng bộ
        } else {
            log.info("Offset committed successfully: {}", offsets);
        }
    });
    ```
    - **Considerations**: Cung cấp thông lượng cao hơn `commitSync()` vì nó không chặn. Tuy nhiên, việc xử lý lỗi trở nên phức tạp hơn.
    - **Best Practices**: Luôn cung cấp một callback để xử lý lỗi commit không đồng bộ (ví dụ: ghi log lỗi, hoặc có thể thử lại logic commit nếu cần).
    - **Common Pitfalls**: Khó khăn trong việc xử lý lỗi nếu không có callback, có thể commit offset cũ nếu không quản lý cẩn thận (ví dụ: gửi một batch tin nhắn mới trước khi nhận được xác nhận commit cho batch cũ).
- [x] Mô phỏng consumer crash và quan sát hành vi phục hồi từ offset đã commit.
    - **File Location**: Hướng dẫn thực hành trên terminal (không yêu cầu thay đổi code).
    - **Notes**: Hướng dẫn người học gửi một vài tin nhắn, sau đó tắt consumer một cách đột ngột (ví dụ: `Ctrl+C` trong terminal nơi consumer đang chạy). Sau đó, khởi động lại consumer và quan sát nó đọc lại từ offset cuối cùng đã commit (không phải từ đầu topic).
    - **Considerations**: Nhấn mạnh tầm quan trọng của việc commit offset đúng thời điểm (sau khi tin nhắn đã được xử lý thành công và bền vững) để tránh mất dữ liệu hoặc trùng lặp khi consumer khởi động lại.
    - **Best Practices**: Thử nghiệm các kịch bản crash khác nhau (ví dụ: crash trước khi commit, crash sau khi commit) để hiểu rõ hơn về ngữ nghĩa phân phối.
    - **Common Pitfalls**: Hiểu nhầm rằng việc crash sẽ tự động xử lý lại tin nhắn từ đầu phân vùng (nếu đã commit).
- [x] Chạy nhiều Consumer trong cùng một group để quan sát phân vùng được gán và cơ chế rebalance.
    - **File Location**: Hướng dẫn thực hành trên terminal (không yêu cầu thay đổi code).
    - **Notes**: Khởi chạy nhiều instance của consumer (trên các terminal hoặc máy khác nhau) cùng một `group.id`. Quan sát các log rebalance (thường có thông báo "Rebalance started", "Partition assigned") và xem phân vùng nào được gán cho mỗi consumer.
    - **Considerations**: Giải thích rằng khi một consumer mới tham gia hoặc một consumer hiện có rời khỏi group (ví dụ: bị crash, bị tắt), một quá trình rebalance xảy ra để phân phối lại các phân vùng giữa các consumer còn lại.
    - **Best Practices**: Giám sát log để theo dõi quá trình rebalance. Đảm bảo số lượng consumer không vượt quá số lượng phân vùng để tránh consumer nhàn rỗi.
    - **Common Pitfalls**: Rebalance storm nếu consumer thường xuyên tham gia/rời khỏi group hoặc `session.timeout.ms` (thời gian tối đa mà consumer có thể không gửi heartbeat trước khi bị coi là chết) quá thấp.

## Progress
*   **Offset Management**: [ ]

## Dependencies
*   TASK-KAFKA-M2-US3-BASIC-CONSUMER (Đã có thể xây dựng Kafka Consumer cơ bản).

## Key Considerations
*   **Exactly-once Semantics**: Quản lý offset thủ công là bước quan trọng để đạt được ngữ nghĩa "exactly-once" khi kết hợp với các cơ chế khác.
*   **Consumer Group Rebalance**: Hiểu rõ rebalance là cần thiết để thiết kế các ứng dụng consumer có tính sẵn sàng cao.

## Notes
*   Giải thích vai trò của topic `__consumer_offsets` trong việc lưu trữ offset.
*   Nhấn mạnh sự khác biệt giữa `commitSync` và `commitAsync` về mặt hiệu suất và độ tin cậy.

## Discussion
Phần này đi sâu vào cách quản lý tiến độ tiêu thụ tin nhắn và cách các consumer hoạt động cùng nhau trong một nhóm, là kiến thức nền tảng cho việc xây dựng các ứng dụng Kafka đáng tin cậy và có khả năng mở rộng.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có hiểu biết vững chắc về quản lý offset và consumer group.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho quản lý offset và consumer group.