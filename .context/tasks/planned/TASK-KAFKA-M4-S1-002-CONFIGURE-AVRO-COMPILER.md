---
title: Cấu hình Gradle/Maven để biên dịch Schema Avro thành các lớp Java
type: task
status: planned
created: 2025-08-12T14:05:21
updated: 2025-08-14T06:47:29
id: TASK-KAFKA-M4-S1-002
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA]
tags: [kafka, avro, gradle, maven, build-tool]
---

## Mô tả

Nhiệm vụ này tập trung vào việc tích hợp quá trình biên dịch schema Avro vào quy trình xây dựng của dự án. Tùy thuộc vào việc dự án sử dụng Gradle hay Maven, plugin phù hợp sẽ được cấu hình để tự động tạo các file nguồn Java từ schema `.avsc` đã định nghĩa trong nhiệm vụ trước. Các lớp Java được tạo này sẽ đại diện cho các mô hình dữ liệu Avro và sẽ được sử dụng bởi Kafka Producer và Consumer.

## Mục tiêu

*   Xác định công cụ xây dựng được sử dụng (Gradle hoặc Maven).
*   Thêm plugin Avro cần thiết vào file cấu hình xây dựng (`build.gradle` cho Gradle hoặc `pom.xml` cho Maven).
*   Cấu hình plugin để:
    *   Xác định vị trí các file schema `.avsc` (ví dụ: trong `src/main/avro/`).
    *   Chỉ định thư mục đầu ra cho các lớp Java được tạo.
    *   Đảm bảo các lớp được tạo được bao gồm trong đường dẫn nguồn của dự án và được biên dịch.

## Danh sách kiểm tra

### Cấu hình

- [x] Xác định xem dự án sử dụng Gradle hay Maven. (Đã hoàn thành)
    - Ghi chú: Kiểm tra `build.gradle` hoặc `pom.xml` trong thư mục gốc của dự án.
- [x] **Nếu là Gradle:** (Đã hoàn thành)
    - [x] Thêm plugin `id 'com.github.davidmc24.gradle-avro'` vào `build.gradle` (hoặc plugin Avro tương đương). (Đã hoàn thành)
    - [x] Cấu hình `avro` source set để bao gồm `src/main/avro`. (Đã hoàn thành)
    - [x] Đảm bảo các nguồn được tạo được thêm vào source set chính. (Đã hoàn thành)
    - Ghi chú: `gradle-avro-plugin` là một lựa chọn phổ biến cho Gradle. Nó đơn giản hóa quá trình tích hợp Avro vào quá trình xây dựng.
- [ ] **Nếu là Maven:** (Không áp dụng)
    - [ ] Thêm `avro-maven-plugin` vào `pom.xml` trong phần `build`.
    - [ ] Cấu hình plugin để chỉ định `sourceDirectory` (ví dụ: `src/main/avro`) và `outputDirectory` (ví dụ: `target/generated-sources/avro`).
    - [ ] Đảm bảo `build-helper-maven-plugin` được sử dụng để thêm thư mục nguồn được tạo vào các thư mục gốc biên dịch của dự án.
    - Ghi chú: `avro-maven-plugin` từ Apache Avro là tiêu chuẩn cho các dự án Maven. `build-helper-maven-plugin` thường cần thiết để đảm bảo Maven nhận các nguồn được tạo.

### Xây dựng

- [x] Chạy lệnh xây dựng (`./gradlew build` hoặc `mvn clean install`) để xác minh rằng các lớp Java được tạo từ `User.avsc` và được biên dịch thành công. (Đã hoàn thành)
    - Ghi chú: Kiểm tra thư mục đầu ra đã chỉ định để tìm file `User.java` được tạo.

## Tiến độ

### Cấu hình

- [ ] Công cụ xây dựng đã được xác định.

## Phụ thuộc

*   `TASK-KAFKA-M4-S1-001-DEFINE-AVRO-SCHEMA`: Nhiệm vụ này phụ thuộc vào việc file schema `User.avsc` đã được định nghĩa.

## Các cân nhắc chính

*   **Tự động hóa xây dựng:** Tự động hóa việc biên dịch schema là rất quan trọng để tăng năng suất của nhà phát triển và đảm bảo tính nhất quán. Biên dịch thủ công dễ gây lỗi và tốn thời gian.
*   **Kiểm soát nguồn:** Các lớp Java được tạo thường KHÔNG nên được commit vào kiểm soát nguồn. Chúng nên được tạo như một phần của quy trình xây dựng. Đảm bảo file `.gitignore` của bạn được cấu hình để loại trừ thư mục nguồn được tạo.
*   **Tích hợp IDE:** Đảm bảo IDE (ví dụ: IntelliJ, VS Code với các tiện ích mở rộng Java) nhận diện thư mục nguồn được tạo là một thư mục gốc nguồn để bật tính năng tự động hoàn thành và điều hướng phù hợp.

## Ghi chú

*   **Lựa chọn Plugin:** Plugin cụ thể và cấu hình của nó có thể thay đổi một chút tùy thuộc vào thiết lập xây dựng chính xác của dự án và phiên bản Avro. Luôn tham khảo tài liệu chính thức của plugin đã chọn.
*   **Gói được tạo:** `namespace` được định nghĩa trong file `.avsc` sẽ xác định gói Java của các lớp được tạo.

## Thảo luận

Nhiệm vụ này là cầu nối giữa định nghĩa schema và triển khai mã. Bằng cách tự động hóa việc tạo các lớp Java từ schema Avro, chúng ta đảm bảo rằng các mô hình dữ liệu của chúng ta luôn đồng bộ với các định nghĩa schema, giảm thiểu các lỗi tiềm ẩn tại thời điểm chạy và đơn giản hóa việc phát triển. Cấu hình phù hợp của bước này là rất cần thiết cho một quy trình làm việc phát triển suôn sẻ khi sử dụng Avro.

## Các bước tiếp theo

Sau khi các lớp Avro được tạo thành công, bước tiếp theo sẽ là triển khai các dịch vụ Kafka Producer và Consumer sẽ sử dụng các lớp được tạo này để tuần tự hóa và giải tuần tự hóa.

## Trạng thái hiện tại

Cấu hình trình biên dịch schema Avro cho công cụ xây dựng đã được lên kế hoạch.