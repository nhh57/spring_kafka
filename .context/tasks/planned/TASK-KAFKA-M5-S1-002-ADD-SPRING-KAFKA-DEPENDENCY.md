---
title: Thêm Dependency Spring Kafka
type: task
status: completed
created: 2025-08-15T02:53:24
updated: 2025-08-15T03:06:20
id: TASK-KAFKA-M5-S1-002
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M5-S1-001]
tags: [spring-boot, kafka, dependency]
---

# TASK-KAFKA-M5-S1-002: Thêm Dependency Spring Kafka

## Description
Nhiệm vụ này liên quan đến việc thêm dependency `spring-kafka` vào dự án Spring Boot mới tạo. Dependency này rất cần thiết để tích hợp các ứng dụng Spring với Apache Kafka.

## Objectives
- Thêm thành công `spring-kafka` vào tệp build của dự án.
- Đảm bảo dự án có thể biên dịch với các chức năng của Spring Kafka.

## Checklist
- [x] Thêm dependency `spring-kafka` vào `build.gradle` (đối với Gradle) hoặc `pom.xml` (đối với Maven).
    - Tệp: `build.gradle` (hoặc `pom.xml`)
    - Ghi chú: Đảm bảo phiên bản `spring-kafka` được thêm vào khớp với phiên bản Spring Boot.
    - Thực hành tốt nhất: Sử dụng quản lý dependency được cung cấp bởi POM cha của Spring Boot hoặc `platform()` của Gradle để có các phiên bản nhất quán.

## Progress
- Thiết lập Dự án: [x]

## Dependencies
- TASK-KAFKA-M5-S1-001: Tạo Dự án Spring Boot cho Module Kafka 5

## Key Considerations
- **Tại sao `spring-kafka`?** Thư viện này cung cấp một lớp trừu tượng cấp cao trên client Kafka gốc, giúp dễ dàng xây dựng các ứng dụng dựa trên Kafka với Spring.
- **Khả năng tương thích phiên bản:** Điều quan trọng là sử dụng phiên bản `spring-kafka` tương thích với phiên bản Spring Boot của bạn để tránh các vấn đề về thời gian chạy. Quản lý dependency của Spring Boot thường tự động xử lý việc này khi sử dụng các starter.

## Notes
- Đây là một thao tác thêm dependency đơn giản. Điểm cần lưu ý chính là đảm bảo khả năng tương thích phiên bản.

## Discussion
Bước này trực tiếp kích hoạt các tính năng của Spring Kafka. Nếu không có nó, dự án sẽ không nhận diện bất kỳ annotation hoặc lớp nào liên quan đến Kafka.

## Next Steps
Sau khi thêm dependency này, bước tiếp theo sẽ là cấu hình các thuộc tính Kafka cơ bản trong `application.yml`.

## Current Status
Hoàn thành.