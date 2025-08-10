---
title: Thiết lập môi trường Kafka cục bộ
type: task
status: completed
created: 2025-08-10T05:26:25
updated: 2025-08-10T10:19:40
id: TASK-KAFKA-M1-US1-SETUP-ENV
priority: high
memory_types: [procedural]
dependencies: []
tags: [kafka, fundamentals, setup, docker, docker-compose]
---

# Thiết lập môi trường Kafka cục bộ

## Description
Là một người học, tôi muốn có thể khởi chạy một cụm Kafka đơn giản (hoặc multi-broker) trên máy cục bộ bằng Docker Compose để có môi trường thực hành.

## Objectives
*   Khởi chạy thành công Zookeeper và Kafka Broker bằng Docker Compose.
*   Xác minh các container đang chạy và không có lỗi.

## Checklist
### Setup
- [x] Tạo file `docker-compose.yml` với cấu hình Zookeeper và Kafka Broker.
    - **Notes**: Cung cấp mẫu `docker-compose.yml` bao gồm các dịch vụ `zookeeper` và `kafka`. Đảm bảo các biến môi trường như `KAFKA_ADVERTISED_LISTENERS` được cấu hình đúng để Kafka broker có thể được truy cập từ bên ngoài container.
    - **Considerations**: Quyết định sử dụng Kafka phiên bản nào (ví dụ: `confluentinc/cp-kafka:7.0.1` hoặc `bitnami/kafka:latest`). Cân nhắc việc sử dụng multi-broker cho các module sau để minh họa replication.
    - **Best Practices**: Luôn sử dụng phiên bản Docker images cụ thể (không dùng `latest`) để đảm bảo tính ổn định và tái tạo. Đặt tên dịch vụ rõ ràng (`kafka-broker-1`, `zookeeper-1`).
    - **Common Pitfalls**: Lỗi port conflict (ví dụ: 9092, 2181 đã được sử dụng), sai cấu hình network dẫn đến broker không advertise đúng địa chỉ, lỗi `KAFKA_CFG_ZOOKEEPER_CONNECT` trỏ sai Zookeeper.
- [x] Viết hướng dẫn chi tiết cách cài đặt Docker và Docker Compose (nếu người học chưa có).
    - **Notes**: Hướng dẫn chi tiết đã được cung cấp tại [`docs/setup_guides/docker_compose_install.md`](docs/setup_guides/docker_compose_install.md). Người học cần làm theo hướng dẫn này trước khi tiếp tục.
    - **Considerations**: Đảm bảo người học đã kiểm tra các yêu cầu hệ thống và hoàn tất cài đặt Docker Desktop (Windows/macOS) hoặc Docker Engine/Compose Plugin (Linux).
    - **Best Practices**: Khuyến khích người học xác minh cài đặt bằng cách chạy `docker --version` và `docker compose version`.
    - **Common Pitfalls**: Bỏ qua các bước cài đặt hoặc không kiểm tra xác minh, dẫn đến lỗi khi chạy Docker Compose.
- [x] Viết hướng dẫn cách sử dụng `docker-compose up -d` và `docker ps` để kiểm tra trạng thái container.
    - **Notes**: Hướng dẫn người học chạy lệnh `docker-compose up -d` trong thư mục chứa `docker-compose.yml`. Giải thích rằng `-d` là "detached mode" (chạy ngầm). Sau đó, hướng dẫn sử dụng `docker ps` để xem các container đang chạy.
    - **Considerations**: Nhấn mạnh việc kiểm tra cột `STATUS` để đảm bảo container đang ở trạng thái `Up (healthy)` hoặc `Up`.
    - **Best Practices**: Luôn sử dụng `docker-compose down` trước khi `docker-compose up -d` để đảm bảo không có container cũ nào còn sót lại và môi trường sạch sẽ.
    - **Common Pitfalls**: Container không khởi động được (trạng thái `Exited`) do lỗi trong `docker-compose.yml` hoặc tài nguyên hệ thống không đủ.
- [x] Viết hướng dẫn cách kiểm tra log của các container Kafka/Zookeeper.
    - **Notes**: Hướng dẫn sử dụng `docker-compose logs -f <service_name>` (ví dụ: `docker-compose logs -f kafka`) để xem log của một dịch vụ cụ thể. Giải thích ý nghĩa của `-f` (follow log). Hướng dẫn tìm kiếm các từ khóa như "started", "listening", "error", "exception" để xác định trạng thái khởi động và các vấn đề tiềm ẩn.
    - **Considerations**: Giải thích rằng log là công cụ quan trọng nhất để gỡ lỗi khi container không khởi động hoặc hoạt động không như mong đợi.
    - **Best Practices**: Khuyến khích người học thường xuyên kiểm tra log khi gặp vấn đề.
    - **Common Pitfalls**: Không phân biệt được các loại log (info, warn, error) và bỏ qua các cảnh báo quan trọng.

## Progress
*   **Setup**: [x] Đã hoàn thành tất cả các tasks.

## Dependencies
*   Docker cài đặt và hoạt động.
*   Docker Compose cài đặt và hoạt động.

## Key Considerations
*   **Tại sao Docker?**: Docker cung cấp môi trường cô lập, dễ tái tạo, tránh xung đột phụ thuộc trên hệ thống host. Nó đơn giản hóa quá trình thiết lập Kafka vốn phức tạp.
*   **Docker Compose**: Giúp quản lý nhiều dịch vụ (Zookeeper, Kafka) trong một file cấu hình duy nhất, dễ dàng khởi động/dừng toàn bộ stack.
*   **Hạn chế**: Hiệu suất có thể không bằng cài đặt native trên Linux, nhưng đủ cho mục đích học tập và phát triển.

## Notes
*   Đảm bảo rằng tài nguyên hệ thống (RAM, CPU) đủ cho Docker và các container Kafka.
*   Sử dụng mạng bridge mặc định của Docker Compose để các container có thể giao tiếp với nhau.

## Discussion
Việc thiết lập môi trường là bước đầu tiên quan trọng để có thể thực hành các kiến thức về Kafka. Docker Compose là công cụ lý tưởng cho việc này vì nó giúp trừu tượng hóa sự phức tạp của việc cài đặt và cấu hình thủ công Kafka.

## Next Steps
Sau khi hoàn thành User Story này, người học sẽ có một cụm Kafka đang chạy và sẵn sàng để tương tác.

## Current Status
[ ] Chuẩn bị môi trường Docker Compose cho Kafka và hướng dẫn thiết lập.