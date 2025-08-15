---
title: Cấu hình Trình xử lý Lỗi với Dead Letter Topic (DLT)
type: task
status: completed
created: 2025-08-15T02:55:11
updated: 2025-08-15T03:09:50
id: TASK-KAFKA-M5-S4-001
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S3-003, TASK-KAFKA-M5-S2-003]
tags: [spring-kafka, error-handling, dlt]
---

# TASK-KAFKA-M5-S4-001: Cấu hình Trình xử lý Lỗi với Dead Letter Topic (DLT)

## Description
Nhiệm vụ này liên quan đến việc cấu hình một cơ chế xử lý lỗi mạnh mẽ cho các consumer Kafka bằng cách sử dụng `DefaultErrorHandler` của Spring Kafka kết hợp với `DeadLetterPublishingRecoverer` và chiến lược `BackOff`. Điều này đảm bảo rằng các tin nhắn không thể xử lý được sẽ được thử lại và cuối cùng được chuyển đến Dead Letter Topic (DLT) để kiểm tra thêm, ngăn chặn consumer bị chặn.

## Objectives
- Cấu hình `DefaultErrorHandler` cho `ConcurrentKafkaListenerContainerFactory`.
- Tích hợp `DeadLetterPublishingRecoverer` để gửi tin nhắn lỗi đến DLT.
- Định nghĩa một chiến lược `BackOff` cho các lần thử lại trước khi xuất bản lên DLT.

## Checklist
- [x] Cấu hình `DefaultErrorHandler` cho `ConcurrentKafkaListenerContainerFactory`.
    - Tệp: `src/main/java/com/example/kafka/config/KafkaConsumerConfig.java`
    - Lớp: `DefaultErrorHandler`
    - Ghi chú: `DefaultErrorHandler` cung cấp khả năng thử lại và tích hợp với `DeadLetterPublishingRecoverer`.
- [x] Tích hợp `DeadLetterPublishingRecoverer` với `DefaultErrorHandler`.
    - Tệp: `src/main/java/com/example/kafka/config/KafkaConsumerConfig.java`
    - Lớp: `DeadLetterPublishingRecoverer`
    - Ghi chú: `DeadLetterPublishingRecoverer` yêu cầu một thể hiện `KafkaTemplate` để xuất bản tin nhắn lỗi đến DLT. `KafkaTemplate` này có thể là cùng một template được sử dụng bởi producer, hoặc một template chuyên dụng nếu yêu cầu các thuộc tính khác nhau để xuất bản lên DLT.
    - Thực hành tốt nhất:
        - Tên topic DLT mặc định là `original-topic.DLT` (ví dụ: nếu topic gốc là `my-topic`, DLT sẽ là `my-topic.DLT`).
        - Cấu hình một chiến lược `BackOff` (ví dụ: `FixedBackOff(interval, maxAttempts)`) cho các lần thử lại trước khi gửi đến DLT. `FixedBackOff` cung cấp độ trễ cố định và số lần thử. `ExponentialBackOff` có thể được sử dụng cho độ trễ tăng dần.
        - **Ví dụ thiết lập `DefaultErrorHandler` (trong phương thức `kafkaListenerContainerFactory()`):**
            ```java
            // Đảm bảo kafkaTemplate được autowired vào KafkaConsumerConfig
            // Ví dụ:
            // @Autowired
            // private KafkaTemplate<String, String> kafkaTemplate;

            DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(1000L, 2) // Thử lại 2 lần với độ trễ 1 giây
            );
            factory.setCommonErrorHandler(errorHandler);
            ```
        - Lỗi thường gặp: Không cấu hình DLT có nghĩa là tin nhắn lỗi sẽ bị mất hoặc được xử lý lại vô tận, làm chặn consumer. Chiến lược `BackOff` không chính xác có thể dẫn đến thử lại quá mức đối với lỗi vĩnh viễn hoặc quá ít lần thử đối với lỗi tạm thời.

## Progress
- Triển khai Xử lý Lỗi: [x]

## Dependencies
- TASK-KAFKA-M5-S3-003: Định nghĩa Bean ConcurrentKafkaListenerContainerFactory
- TASK-KAFKA-M5-S2-003: Định nghĩa Bean KafkaTemplate (cho `DeadLetterPublishingRecoverer`)

## Key Considerations
- **Độ tin cậy:** Cơ chế này rất quan trọng để xây dựng các ứng dụng consumer chịu lỗi, đảm bảo không có tin nhắn nào bị mất do lỗi xử lý.
- **Khả năng quan sát:** DLT cung cấp một nơi tập trung để giám sát và phân tích các tin nhắn lỗi, hỗ trợ gỡ lỗi và xác định các vấn đề lặp lại.
- **Chiến lược thử lại:** Chiến lược `BackOff` cho phép xử lý các lỗi tạm thời một cách duyên dáng bằng cách thử lại tin nhắn một số lần nhất định với độ trễ được xác định.

## Notes
- Đây là một thành phần quan trọng cho các ứng dụng Kafka sẵn sàng cho sản xuất. Nó ngăn một tin nhắn xấu duy nhất làm sập toàn bộ consumer.

## Discussion
Triển khai một chiến lược xử lý lỗi mạnh mẽ với DLT là một khía cạnh cơ bản của việc xây dựng các consumer Kafka đáng tin cậy. Nó cân bằng giữa khả năng phục hồi tức thì với phân tích dài hạn các tin nhắn có vấn đề.

## Next Steps
Với việc xử lý lỗi đã được cấu hình, tập hợp các tác vụ cuối cùng sẽ tập trung vào việc triển khai kế hoạch kiểm thử để xác minh chức năng của producer, consumer và xử lý lỗi.

## Current Status
Hoàn thành.