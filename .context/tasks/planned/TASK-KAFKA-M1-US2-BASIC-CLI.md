---
title: Tương tác Kafka cơ bản bằng CLI
type: task
status: completed
created: 2025-08-10T05:26:41
updated: 2025-08-10T10:20:29
id: TASK-KAFKA-M1-US2-BASIC-CLI
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M1-US1-SETUP-ENV]
tags: [kafka, fundamentals, cli, producer, consumer, topic]
---

# Tương tác Kafka cơ bản bằng CLI

## Description
Là một người học, tôi muốn có thể sử dụng các công cụ Kafka CLI cơ bản để tương tác với cụm Kafka, bao gồm tạo topic, gửi và nhận tin nhắn.

## Objectives
*   Tạo thành công một topic Kafka bằng CLI.
*   Gửi tin nhắn từ console producer và nhận được chúng ở console consumer.

## Checklist
### CLI Interaction
- [x] Viết hướng dẫn cách truy cập Kafka CLI bên trong container Docker.
    - **Notes**: Hướng dẫn sử dụng `docker exec -it <tên_hoặc_ID_container_kafka> bash` hoặc `docker-compose exec kafka bash` để vào shell của container Kafka. Giải thích tại sao cần vào container để sử dụng các script CLI.
    - **Considerations**: Đảm bảo người học đã khởi động container Kafka thành công. Cung cấp lệnh `docker ps` để tìm tên/ID container.
    - **Best Practices**: Luôn sử dụng tên dịch vụ trong `docker-compose.yml` (ví dụ: `kafka`) thay vì ID container để dễ quản lý.
    - **Common Pitfalls**: Container Kafka chưa chạy, sai tên/ID container, không có `bash` trong container (thay bằng `sh` nếu cần).
- [x] Cung cấp các lệnh mẫu để tạo topic.
    - **Notes**: Ví dụ: `kafka-topics.sh --create --topic my-first-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1`. Giải thích chi tiết từng tham số: `--create` (tạo topic), `--topic` (tên topic), `--bootstrap-server` (địa chỉ broker), `--partitions` (số phân vùng), `--replication-factor` (số bản sao).
    - **Considerations**: Giải thích mối quan hệ giữa số phân vùng và khả năng mở rộng của consumer group. Giải thích ý nghĩa của replication factor đối với độ bền dữ liệu.
    - **Best Practices**: Luôn chỉ định rõ ràng `partitions` và `replication-factor` thay vì dùng giá trị mặc định. Kiểm tra topic đã tạo bằng `kafka-topics.sh --list` và `kafka-topics.sh --describe`.
    - **Common Pitfalls**: Topic đã tồn tại, sai địa chỉ bootstrap server, không đủ broker cho replication factor.
- [x] Cung cấp các lệnh mẫu để gửi tin nhắn bằng console producer.
    - **Notes**: Ví dụ: `kafka-console-producer.sh --topic my-first-topic --bootstrap-server localhost:9092`. Hướng dẫn người học gõ tin nhắn và nhấn Enter để gửi. Giải thích rằng mỗi dòng là một tin nhắn.
    - **Considerations**: Giải thích Producer là ứng dụng tạo và gửi dữ liệu.
    - **Best Practices**: Bắt đầu với các tin nhắn đơn giản, không có key hoặc header để tập trung vào chức năng cơ bản.
    - **Common Pitfalls**: Producer không thể kết nối đến broker, tin nhắn không xuất hiện do consumer chưa chạy hoặc sai topic.
- [x] Cung cấp các lệnh mẫu để tiêu thụ tin nhắn bằng console consumer.
    - **Notes**: Ví dụ: `kafka-console-consumer.sh --topic my-first-topic --bootstrap-server localhost:9092 --from-beginning`. Giải thích các tham số: `--from-beginning` (đọc từ đầu topic), `--group` (chỉ định consumer group).
    - **Considerations**: Giải thích Consumer là ứng dụng đọc dữ liệu. Giới thiệu khái niệm Consumer Group và vai trò của nó trong việc xử lý song song.
    - **Best Practices**: Luôn sử dụng `--from-beginning` khi muốn đọc tất cả tin nhắn từ topic, đặc biệt khi mới bắt đầu. Sử dụng `--group` để minh họa hành vi của consumer group.
    - **Common Pitfalls**: Quên `--from-beginning` (chỉ đọc tin nhắn mới), consumer group không nhận được phân vùng (do consumer khác đang giữ), sai địa chỉ bootstrap server.

## Progress
*   **CLI Interaction**: [x] Đã hoàn thành tất cả các tasks.

## Dependencies
*   TASK-KAFKA-M1-US1-SETUP-ENV (Môi trường Kafka cục bộ đã được thiết lập và chạy).

## Key Considerations
*   **Tầm quan trọng của CLI**: CLI là cách cơ bản nhất để tương tác với Kafka, giúp hiểu rõ các khái niệm mà không bị che khuất bởi các abstraction của client library.
*   **Mô hình Publish/Subscribe**: Các lệnh producer/consumer minh họa rõ ràng mô hình này.

## Notes
*   Đảm bảo `bootstrap-server` trỏ đến địa chỉ và port của Kafka Broker trong Docker Compose.
*   Giải thích sự khác biệt giữa producer và consumer.

## Discussion
Phần này giúp người học có cái nhìn trực quan về cách dữ liệu chảy qua Kafka từ producer đến consumer, sử dụng các công cụ dòng lệnh quen thuộc.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có khả năng tương tác cơ bản với cụm Kafka và sẵn sàng khám phá các khái niệm nâng cao hơn.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho tương tác CLI cơ bản.