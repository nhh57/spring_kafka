# Tài liệu Thiết kế Kỹ thuật: Module 3 - Kafka APIs Deep Dive - Thực hành

## 1. Tổng quan

Tài liệu này phác thảo kế hoạch kỹ thuật cho Module 3 của chương trình giảng dạy Kafka thực hành, tập trung vào việc đi sâu vào Kafka APIs. Module này sẽ bao gồm các thực hành về Kafka Streams để xây dựng các ứng dụng xử lý luồng đơn giản và Kafka Connect để sử dụng các Connector có sẵn cho việc di chuyển dữ liệu.

## 2. Yêu cầu

### 2.1 Yêu cầu Chức năng

*   **Kafka Streams:**
    *   Xây dựng một ứng dụng xử lý luồng đơn giản sử dụng Kafka Streams API.
    *   Thực hiện các phép biến đổi cơ bản như `filter`, `map`, `groupByKey`, `count` trên một luồng dữ liệu.
    *   Ghi kết quả của quá trình xử lý luồng ra một topic Kafka mới.
*   **Kafka Connect:**
    *   Sử dụng `FileStreamSourceConnector` để đọc dữ liệu từ một file và ghi vào một topic Kafka.
    *   Sử dụng `FileStreamSinkConnector` để đọc dữ liệu từ một topic Kafka và ghi ra một file.
    *   Giới thiệu và minh họa việc sử dụng Single Message Transforms (SMTs).

### 2.2 Yêu cầu Phi chức năng

*   **Hiệu suất:** Các ví dụ code cần minh họa hiệu suất chấp nhận được cho các tác vụ xử lý luồng và di chuyển dữ liệu cơ bản.
*   **Khả năng mở rộng:** Các khái niệm về khả năng mở rộng của Kafka Streams và Kafka Connect sẽ được đề cập thông qua thiết kế các ví dụ.
*   **Khả năng bảo trì:** Code ví dụ cần rõ ràng, dễ hiểu và tuân thủ các quy ước code Java tiêu chuẩn.

## 3. Thiết kế Kỹ thuật

### 3.1. Thay đổi Mô hình Dữ liệu

*   **Kafka Streams:** Không có thay đổi mô hình dữ liệu cứng nhắc. Các ví dụ sẽ thao tác với dữ liệu dạng `String` hoặc các kiểu dữ liệu đơn giản khác. Có thể giới thiệu khái niệm `Serdes` (Serializer/Deserializer) cho các kiểu dữ liệu phức tạp hơn.
*   **Kafka Connect:** Dữ liệu sẽ được đọc từ và ghi vào các file văn bản, sau đó được đưa vào/ra Kafka dưới dạng `String` hoặc `byte[]`.

### 3.2. Thay đổi API

*   **Kafka Streams:**
    *   Sử dụng `StreamsBuilder` để định nghĩa topology xử lý luồng.
    *   Sử dụng `KStream` để biểu diễn luồng dữ liệu.
    *   Các phương thức API chính: `filter()`, `map()`, `groupByKey()`, `count()`, `to()`.
*   **Kafka Connect:**
    *   Sử dụng Kafka Connect REST API hoặc các công cụ CLI để cấu hình và quản lý Connectors.
    *   Các Connector cụ thể: `FileStreamSourceConnector`, `FileStreamSinkConnector`.
    *   Cấu hình SMTs thông qua thuộc tính Connector.

### 3.3. Thay đổi UI

Không có thay đổi UI cụ thể nào trong module này. Việc giám sát có thể thông qua Kafka UI (nếu được thiết lập) để xem các topic và tin nhắn.

### 3.4. Luồng Logic

*   **Kafka Streams:**
    1.  Khởi tạo `StreamsBuilder` và định nghĩa Kafka Streams topology.
    2.  Tạo `KStream` từ một topic đầu vào.
    3.  Áp dụng chuỗi các phép biến đổi (`filter`, `map`, `groupByKey`, `count`).
    4.  Ghi kết quả vào một topic đầu ra.
    5.  Khởi động Kafka Streams application.
*   **Kafka Connect:**
    1.  Cấu hình `FileStreamSourceConnector` để đọc từ `source.txt` và publish vào `input-topic`.
    2.  Cấu hình `FileStreamSinkConnector` để đọc từ `output-topic` và ghi vào `sink.txt`.
    3.  (Tùy chọn) Cấu hình SMTs trên các Connector để biến đổi tin nhắn trong quá trình di chuyển.

### 3.5. Caching Strategy

*   **Kafka Streams:** `State Stores` (thường là RocksDB) được sử dụng nội bộ bởi Kafka Streams cho các thao tác có trạng thái như `groupByKey` và `count`. Các ví dụ sẽ minh họa cách State Stores hoạt động. Không có chiến lược caching tùy chỉnh bên ngoài.
*   **Kafka Connect:** Không có chiến lược caching cụ thể.

### 3.6. Concurrency Handling

*   **Kafka Streams:** Concurrency được xử lý tự động bởi Kafka Streams thông qua việc phân bổ các task xử lý cho các instance ứng dụng và các thread. Mỗi task xử lý một tập hợp các phân vùng.
*   **Kafka Connect:** Concurrency được quản lý bởi Kafka Connect framework, nơi các Connector task được phân phối trên các worker.

### 3.7. Phụ thuộc

*   **Kafka Streams:**
    *   `org.apache.kafka:kafka-streams` (dependency Gradle/Maven)
*   **Kafka Connect:**
    *   Không có dependency code cụ thể, nhưng yêu cầu cài đặt và cấu hình Kafka Connect runtime.

### 3.8. Cân nhắc Bảo mật

*   Module này tập trung vào chức năng API cơ bản. Cân nhắc bảo mật (SSL/TLS, SASL) sẽ được đề cập trong Module 4.
*   Đối với Kafka Connect, cần đảm bảo các file nguồn/đích có quyền truy cập phù hợp.

### 3.9. Cân nhắc Hiệu suất

*   **Kafka Streams:**
    *   Hiệu suất của các phép biến đổi và việc sử dụng State Stores sẽ được minh họa.
    *   Thảo luận về cách các phép biến đổi có thể ảnh hưởng đến hiệu suất và cách tối ưu hóa (ví dụ: chọn đúng key cho `groupByKey`).
*   **Kafka Connect:**
    *   Hiệu suất di chuyển dữ liệu sẽ phụ thuộc vào cấu hình Connector và tài nguyên hệ thống.
    *   Thảo luận về lợi ích hiệu suất của Kafka Connect so với việc tự viết client thủ công.

## 4. Kế hoạch Kiểm tra

*   **Kiểm tra đơn vị (Unit Test):**
    *   Đối với Kafka Streams: Kiểm tra logic xử lý luồng riêng biệt, sử dụng `TopologyTestDriver` để mô phỏng dữ liệu đầu vào và kiểm tra đầu ra.
*   **Kiểm tra tích hợp (Integration Test):**
    *   Đối với Kafka Streams: Chạy ứng dụng Streams trong môi trường test (sử dụng Embedded Kafka hoặc Testcontainers) để xác minh toàn bộ luồng.
    *   Đối với Kafka Connect: Kiểm tra bằng cách chạy các Connector và xác minh dữ liệu được di chuyển chính xác giữa file và Kafka.

## 5. Câu hỏi Mở

*   Làm thế nào để tích hợp các ví dụ Kafka Streams và Kafka Connect vào cấu trúc dự án Gradle hiện có (multi-project build)? Có nên tạo sub-project mới cho mỗi phần này không, hay chỉ là các ví dụ code độc lập?

## 6. Các Phương án Đã Xem xét

*   **Không bao gồm Kafka Connect trong Module này:** Ban đầu có thể xem xét tách Kafka Connect thành một module riêng biệt do sự khác biệt về môi trường thiết lập (yêu cầu cài đặt Connect runtime). Tuy nhiên, vì cả hai đều là các API chính để tương tác với Kafka, việc gộp chung chúng vào "Kafka APIs Deep Dive" sẽ cung cấp cái nhìn toàn diện hơn về các công cụ chính trong hệ sinh thái Kafka.