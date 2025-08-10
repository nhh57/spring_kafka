---
title: Hiểu phân vùng qua CLI
type: task
status: completed
created: 2025-08-10T05:26:55
updated: 2025-08-10T10:21:14
id: TASK-KAFKA-M1-US3-PARTITIONING
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M1-US1-SETUP-ENV, TASK-KAFKA-M1-US2-BASIC-CLI]
tags: [kafka, fundamentals, cli, partitioning, key]
---

# Hiểu phân vùng qua CLI

## Description
Là một người học, tôi muốn có thể gửi tin nhắn với key và quan sát chúng được gửi đến cùng một phân vùng để hiểu cách Kafka duy trì thứ tự và phân phối dữ liệu.

## Objectives
*   Gửi tin nhắn với key bằng console producer.
*   Xác minh rằng các tin nhắn có cùng key được gửi đến cùng một phân vùng.

## Checklist
### Partitioning Understanding
- [x] Viết hướng dẫn cách sử dụng `kafka-console-producer.sh` với tùy chọn `--property parse.key=true --property key.separator=:`.
    - **Notes**: Giải thích rằng `--property parse.key=true` yêu cầu producer phân tích key từ đầu vào, và `--property key.separator=:` chỉ định dấu phân cách giữa key và value (ví dụ: `my_key:my_value`).
    - **Considerations**: Nhấn mạnh rằng việc sử dụng key là cách để đảm bảo các tin nhắn liên quan đến cùng một thực thể (ví dụ: `order_id`, `user_id`) luôn được gửi đến cùng một phân vùng, từ đó duy trì thứ tự xử lý.
    - **Best Practices**: Khuyến khích sử dụng các key có tính phân tán tốt để tránh "hot partitions" (phân vùng bị quá tải).
    - **Common Pitfalls**: Quên cung cấp key hoặc sử dụng sai dấu phân cách, dẫn đến tin nhắn không được phân vùng như mong đợi.
- [x] Viết hướng dẫn cách quan sát và giải thích việc phân phối tin nhắn theo key.
    - **Notes**: Hướng dẫn người học gửi nhiều tin nhắn với cùng một key và quan sát chúng chỉ xuất hiện trên một consumer cụ thể (nếu consumer group có nhiều consumer). Giải thích cơ chế hashing của Kafka: `hash(key) % num_partitions` để xác định phân vùng.
    - **Considerations**: Giải thích rằng ngay cả khi có nhiều consumer trong một group, chỉ một consumer duy nhất sẽ được gán cho một phân vùng tại một thời điểm, đảm bảo thứ tự xử lý trong phân vùng đó.
    - **Best Practices**: Sử dụng `kafka-consumer-groups.sh --describe --group <group_name>` để xem phân vùng nào được gán cho consumer nào và offset hiện tại của từng phân vùng. Điều này giúp xác minh trực quan việc phân phối.
    - **Common Pitfalls**: Không hiểu rằng thứ tự chỉ được đảm bảo trong phạm vi một phân vùng, không phải trên toàn bộ topic nếu có nhiều phân vùng.
- [x] Cung cấp một ví dụ đơn giản để người học tự thử nghiệm với các key khác nhau.
    - **Notes**: Đề xuất một kịch bản đơn giản, ví dụ: quản lý trạng thái người dùng. Người học sẽ gửi tin nhắn với các key như `user1`, `user2`, `user3` và quan sát hành vi phân vùng.
    - **Considerations**: Khuyến khích người học thử gửi nhiều tin nhắn với cùng một key, sau đó thay đổi key và gửi thêm, để thấy sự thay đổi trong phân phối.
    - **Best Practices**: Luôn nhắc nhở người học về tầm quan trọng của việc chọn key phù hợp với use case nghiệp vụ.
    - **Common Pitfalls**: Sử dụng key không đa dạng, dẫn đến phân phối không đều và có thể tạo ra hot partitions trong môi trường thực tế.

## Progress
*   **Partitioning Understanding**: [x] Đã hoàn thành tất cả các tasks.

## Dependencies
*   TASK-KAFKA-M1-US1-SETUP-ENV (Môi trường Kafka cục bộ đã được thiết lập và chạy).
*   TASK-KAFKA-M1-US2-BASIC-CLI (Đã quen thuộc với các lệnh CLI cơ bản).

## Key Considerations
*   **Thứ tự tin nhắn**: Key đảm bảo thứ tự tin nhắn trong một phân vùng.
*   **Phân phối dữ liệu**: Key giúp phân phối dữ liệu một cách nhất quán, quan trọng cho các ứng dụng cần xử lý dữ liệu liên quan cùng nhau.

## Notes
*   Giải thích vai trò của hashing trong việc xác định phân vùng dựa trên key.
*   Nhấn mạnh rằng thứ tự chỉ được đảm bảo trong phạm vi một phân vùng.

## Discussion
Hiểu cách Kafka sử dụng key để phân vùng là rất quan trọng để thiết kế các ứng dụng Kafka hiệu quả, đặc biệt khi cần đảm bảo thứ tự xử lý hoặc phân phối tải.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có hiểu biết sâu sắc hơn về cách Kafka quản lý phân vùng và thứ tự tin nhắn.

## Current Status
[ ] Chuẩn bị hướng dẫn và ví dụ code cho việc hiểu phân vùng bằng CLI.