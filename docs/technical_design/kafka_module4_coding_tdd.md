# Tài liệu Thiết kế Kỹ thuật: Module 4 - Advanced Kafka Concepts & Operations

## 1. Tổng quan

Tài liệu này phác thảo thiết kế kỹ thuật cho Module 4 của kế hoạch giảng dạy code Kafka, tập trung vào các khái niệm nâng cao và hoạt động của Kafka. Module này bao gồm hai phần chính: thực hành Serialization/Deserialization (Serdes) với Avro và thực hành bảo mật Kafka (SSL/TLS, SASL).

Mục tiêu chính là cung cấp các ví dụ code và hướng dẫn chi tiết để người học có thể tự tay cấu hình và trải nghiệm các khía cạnh phức tạp này của Kafka trong môi trường phát triển. Việc sử dụng Avro và Schema Registry sẽ giúp quản lý Schema hiệu quả, trong khi cấu hình bảo mật sẽ trang bị kiến thức về cách bảo vệ cụm Kafka.

## 2. Yêu cầu

### 2.1 Yêu cầu Chức năng

*   Là người học, tôi muốn có thể định nghĩa một Avro Schema và sử dụng nó để tuần tự hóa/giải tuần tự hóa tin nhắn Kafka.
*   Là người học, tôi muốn có thể tích hợp Producer và Consumer với Schema Registry để quản lý Schema tự động.
*   Là người học, tôi muốn có thể cấu hình Producer và Consumer để kết nối an toàn với cụm Kafka sử dụng SSL/TLS.
*   Là người học, tôi muốn có thể cấu hình Producer và Consumer để kết nối an toàn với cụm Kafka sử dụng SASL (ví dụ: SCRAM).

### 2.2 Yêu cầu Phi chức năng

*   **Khả năng mở rộng:** Các ví dụ code cần minh họa cách cấu hình Serdes và bảo mật mà không ảnh hưởng đáng kể đến khả năng mở rộng của ứng dụng Kafka.
*   **Bảo mật:** Các cấu hình bảo mật phải tuân thủ các phương pháp hay nhất hiện hành của Kafka.
*   **Hiệu suất:** Cần xem xét tác động của Serdes và bảo mật đến hiệu suất (throughput, latency) và có các khuyến nghị tối ưu hóa.
*   **Khả năng bảo trì:** Code ví dụ phải rõ ràng, dễ hiểu và dễ bảo trì.
*   **Khả năng tái sử dụng:** Các cấu hình và logic Serdes/bảo mật nên được thiết kế để có thể tái sử dụng trong các dự án thực tế.

## 3. Thiết kế Kỹ thuật

### 3.1. Thay đổi Mô hình Dữ liệu

*   **Avro Schema (`.avsc` file):**
    *   Định nghĩa một hoặc nhiều Avro Schema đơn giản để đại diện cho cấu trúc dữ liệu của tin nhắn Kafka.
    *   Ví dụ: Một schema cho `User` có các trường như `id`, `name`, `email`.
    *   Schema sẽ được sử dụng bởi cả Producer và Consumer.
    *   Cần có một quy trình để biên dịch `.avsc` thành các lớp Java tương ứng.

### 3.2. Thay đổi API

*   **Avro Producer:**
    *   Sử dụng `KafkaProducer` của Apache Kafka.
    *   Cấu hình `key.serializer` và `value.serializer` thành `KafkaAvroSerializer`.
    *   Thêm cấu hình `schema.registry.url` để Producer biết địa chỉ của Schema Registry.
    *   Sử dụng các lớp Java được tạo từ Avro Schema để tạo đối tượng tin nhắn.
*   **Avro Consumer:**
    *   Sử dụng `KafkaConsumer` của Apache Kafka.
    *   Cấu hình `key.deserializer` và `value.deserializer` thành `KafkaAvroDeserializer`.
    *   Thêm cấu hình `schema.registry.url` để Consumer biết địa chỉ của Schema Registry.
    *   Cấu hình `specific.avro.reader` nếu cần đọc các đối tượng Avro cụ thể.
*   **Security Configuration (Producer/Consumer):**
    *   **SSL/TLS:**
        *   Cấu hình `security.protocol=SSL`.
        *   Cấu hình `ssl.truststore.location`, `ssl.truststore.password` để tin cậy các broker.
        *   Cấu hình `ssl.keystore.location`, `ssl.keystore.password`, `ssl.key.password` để xác thực client (mutual TLS).
    *   **SASL (SCRAM-SHA-512):**
        *   Cấu hình `security.protocol=SASL_SSL` (nếu dùng SSL cùng SASL) hoặc `SASL_PLAINTEXT`.
        *   Cấu hình `sasl.mechanism=SCRAM-SHA-512`.
        *   Cấu hình `sasl.jaas.config` với thông tin username/password.

### 3.3. Luồng Logic

*   **Serdes với Avro:**
    1.  Định nghĩa Avro Schema trong file `.avsc`.
    2.  Sử dụng plugin Maven/Gradle Avro để tạo các lớp Java tương ứng.
    3.  **Producer:** Tạo đối tượng từ lớp Avro đã tạo, gửi đến Kafka. `KafkaAvroSerializer` sẽ tự động đăng ký/kiểm tra Schema với Schema Registry và tuần tự hóa đối tượng thành định dạng Avro nhị phân.
    4.  **Consumer:** Đọc tin nhắn từ Kafka. `KafkaAvroDeserializer` sẽ sử dụng ID Schema trong tin nhắn để lấy Schema từ Schema Registry và giải tuần tự hóa dữ liệu nhị phân thành đối tượng Java Avro.
*   **Bảo mật Kafka:**
    1.  **Thiết lập môi trường Kafka:** Cần một cụm Kafka được cấu hình để yêu cầu SSL/TLS và/hoặc SASL.
    2.  **Producer/Consumer:**
        *   Khi khởi tạo `KafkaProducer` hoặc `KafkaConsumer`, các thuộc tính bảo mật (`security.protocol`, `ssl.*`, `sasl.*`) sẽ được truyền vào.
        *   Client sẽ tự động thiết lập kết nối bảo mật với broker theo cấu hình.

### 3.4. Phụ thuộc

*   **Apache Avro:** `org.apache.avro:avro`
*   **Confluent Kafka Avro Serializer/Deserializer:** `io.confluent:kafka-avro-serializer`
*   **Confluent Schema Registry Client:** `io.confluent:kafka-schema-registry-client`
*   **Apache Kafka Clients:** `org.apache.kafka:kafka-clients`
*   **Gradle/Maven Avro Plugin:** Để tự động tạo các lớp Java từ `.avsc`.

### 3.5. Cân nhắc Bảo mật

*   **SSL/TLS:**
    *   Sử dụng chứng chỉ đáng tin cậy.
    *   Bảo vệ `truststore` và `keystore` bằng mật khẩu mạnh.
    *   Đảm bảo `ssl.endpoint.identification.algorithm=HTTPS` được đặt để ngăn chặn tấn công man-in-the-middle.
*   **SASL:**
    *   Sử dụng cơ chế mạnh như SCRAM-SHA-512 thay vì PLAIN.
    *   Bảo vệ thông tin đăng nhập (username/password) trong `sasl.jaas.config` (ví dụ: sử dụng biến môi trường hoặc công cụ quản lý bí mật).
    *   Kết hợp SASL với SSL/TLS (`SASL_SSL`) để mã hóa dữ liệu khi truyền tải.
*   **ACLs:** Mặc dù không phải là một phần của ví dụ code trực tiếp, cần nhấn mạnh tầm quan trọng của việc cấu hình ACLs ở cấp broker để ủy quyền chi tiết cho các người dùng/ứng dụng đã xác thực.

### 3.6. Cân nhắc Hiệu suất

*   **Avro Serdes:**
    *   Avro tạo ra các tin nhắn nhị phân nhỏ gọn hơn so với JSON, giúp giảm băng thông mạng và dung lượng lưu trữ Kafka.
    *   Quá trình tuần tự hóa/giải tuần tự hóa có thể nhanh hơn so với JSON/XML.
    *   Overhead từ việc tương tác với Schema Registry (đăng ký/truy xuất Schema) thường không đáng kể sau lần đầu tiên.
*   **Bảo mật (SSL/TLS, SASL):**
    *   Mã hóa/giải mã SSL/TLS thêm một chút overhead CPU và độ trễ vào quá trình truyền tin nhắn.
    *   Việc sử dụng SASL (đặc biệt là SCRAM) cũng có thể thêm một lượng nhỏ overhead tính toán.
    *   Tác động đến hiệu suất thường chấp nhận được đối với hầu hết các ứng dụng và cần được cân bằng với yêu cầu bảo mật.
    *   Cân nhắc sử dụng các CPU hỗ trợ tăng tốc mã hóa phần cứng.

## 4. Kế hoạch Kiểm tra

*   **Kiểm tra đơn vị:**
    *   Kiểm tra logic tạo và sử dụng các đối tượng Avro (ví dụ: kiểm tra việc khởi tạo, truy cập trường).
    *   Kiểm tra logic cấu hình Producer/Consumer (ví dụ: đảm bảo các thuộc tính được đặt đúng).
*   **Kiểm tra tích hợp:**
    *   Sử dụng Kafka cục bộ (Docker Compose) và Schema Registry để chạy các ví dụ Avro Producer/Consumer.
    *   Xác minh rằng tin nhắn được gửi và nhận thành công, và nội dung khớp với mong đợi.
    *   Sử dụng Kafka cục bộ được bảo mật (SSL/TLS, SASL) để kiểm tra kết nối an toàn từ Producer/Consumer.
    *   Mô phỏng các trường hợp lỗi (ví dụ: Schema không tương thích, chứng chỉ hết hạn) để xác minh hành vi xử lý lỗi.

## 5. Câu hỏi Mở

*   Cần có hướng dẫn chi tiết hơn về cách tạo và quản lý chứng chỉ SSL/TLS cho cụm Kafka và client.
*   Cần làm rõ cách cấu hình Kafka broker để bật SSL/TLS và SASL.
*   Có nên bao gồm ví dụ về Schema Evolution (tương thích ngược/tiến) và cách Schema Registry xử lý chúng?

## 6. Các Phương án Đã Xem xét

*   **Sử dụng JSON Schema thay vì Avro:**
    *   **Ưu điểm:** Dễ đọc, thân thiện với con người hơn.
    *   **Nhược điểm:** Kích thước tin nhắn lớn hơn, hiệu suất Serdes thường kém hơn, ít hỗ trợ tích hợp sẵn cho Schema Evolution so với Avro trong hệ sinh thái Kafka.
    *   **Lý do từ chối:** Avro là lựa chọn ưu việt hơn cho dữ liệu có cấu trúc trong Kafka do hiệu suất và khả năng quản lý Schema vượt trội.
*   **Chỉ sử dụng SSL/TLS hoặc chỉ SASL thay vì cả hai:**
    *   **Ưu điểm:** Cấu hình đơn giản hơn.
    *   **Nhược điểm:**
        *   Chỉ SSL/TLS: Cung cấp mã hóa và xác thực server, nhưng không có xác thực client mạnh mẽ (nếu không dùng mutual TLS) và ủy quyền dựa trên người dùng.
        *   Chỉ SASL: Cung cấp xác thực và ủy quyền, nhưng không mã hóa dữ liệu khi truyền tải (nếu dùng PLAINTEXT).
    *   **Lý do từ chối:** Để đạt được mức độ bảo mật cao nhất (mã hóa + xác thực mạnh mẽ), việc kết hợp cả SSL/TLS và SASL (SASL_SSL) là phương pháp hay nhất.