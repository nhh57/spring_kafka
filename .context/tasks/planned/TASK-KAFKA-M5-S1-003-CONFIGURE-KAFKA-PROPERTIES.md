---
title: Cấu hình thuộc tính Kafka trong application.yml
type: task
status: completed
created: 2025-08-15T02:53:38
updated: 2025-08-15T03:06:45
id: TASK-KAFKA-M5-S1-003
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S1-001, TASK-KAFKA-M5-S1-002]
tags: [spring-boot, kafka, configuration]
---

# TASK-KAFKA-M5-S1-003: Cấu hình thuộc tính Kafka trong application.yml

## Description
Nhiệm vụ này liên quan đến việc thiết lập các thuộc tính kết nối Kafka cơ bản trong tệp `application.yml` của dự án Spring Boot. Các thuộc tính này rất quan trọng để ứng dụng kết nối và tương tác với cụm Kafka.

## Objectives
- Cấu hình `bootstrap-servers` để trỏ đến các Kafka broker.
- Định nghĩa `group-id` mặc định cho các consumer trong ứng dụng.

## Checklist
- [x] Cấu hình `bootstrap-servers` trong `application.yml`.
    - Tệp: `src/main/resources/application.yml`
    - Nội dung:
        ```yaml
        spring:
          kafka:
            bootstrap-servers: localhost:9092 # Thay thế bằng địa chỉ Kafka broker thực tế
            consumer:
              group-id: spring-kafka-group
            producer:
              # Các thuộc tính Producer sẽ được thêm vào trong các tác vụ sau
        ```
    - Ghi chú: `bootstrap-servers` chỉ định kết nối đến Kafka broker.
- [x] Cấu hình `group-id` trong `application.yml` cho các consumer.
    - Tệp: `src/main/resources/application.yml`
    - Ghi chú: `group-id` rất cần thiết cho việc quản lý nhóm consumer, cho phép nhiều phiên bản consumer chia sẻ các phân vùng.
    - Lỗi thường gặp: Địa chỉ `bootstrap-servers` không chính xác (ví dụ: sai cổng, tên máy chủ) sẽ ngăn kết nối.

## Progress
- Thiết lập Dự án: [x]

## Dependencies
- TASK-KAFKA-M5-S1-001: Tạo Dự án Spring Boot cho Module Kafka 5
- TASK-KAFKA-M5-S1-002: Thêm Dependency Spring Kafka

## Key Considerations
- **`bootstrap-servers`:** Đây là danh sách ban đầu các cặp host/port để thiết lập kết nối với cụm Kafka. Ngay cả khi chỉ định một, client sẽ tự động khám phá các broker khác trong cụm.
- **`group-id`:** Các consumer trong cùng một `group-id` sẽ chia sẻ các phân vùng của một topic, đảm bảo rằng mỗi tin nhắn chỉ được xử lý bởi một consumer trong nhóm đó. Các nhóm khác nhau có thể tiêu thụ cùng một topic một cách độc lập.

## Notes
- Đây là các cấu hình cơ bản. Nếu không có chúng, các ứng dụng Spring Kafka không thể kết nối hoặc tương tác với cụm Kafka.
- Đối với thiết lập cục bộ, `localhost:9092` là phổ biến, nhưng trong môi trường sản xuất, đây sẽ là danh sách các địa chỉ broker thực tế.

## Discussion
Cấu hình ban đầu đúng đắn đặt nền tảng cho tất cả các tương tác Kafka tiếp theo. Đây là một bước quan trọng đảm bảo khả năng kết nối và hành vi nhóm consumer đúng đắn.

## Next Steps
Với việc thiết lập dự án cơ bản đã hoàn tất, các tác vụ tiếp theo sẽ tập trung vào việc triển khai các chức năng Kafka Producer và Consumer.

## Current Status
Hoàn thành.