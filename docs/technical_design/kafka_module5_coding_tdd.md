# Tài liệu Thiết kế Kỹ thuật: Module 5 - Spring for Apache Kafka

## 1. Tổng quan

Tài liệu này phác thảo thiết kế kỹ thuật cho Module 5 của kế hoạch giảng dạy code Kafka, tập trung vào việc tích hợp Apache Kafka với Spring Boot thông qua Spring for Apache Kafka. Module này sẽ bao gồm các phần chính: thiết lập dự án Spring Boot, sử dụng `KafkaTemplate` để gửi tin nhắn, sử dụng `@KafkaListener` để tiêu thụ tin nhắn và triển khai các chiến lược xử lý lỗi hiệu quả.

Mục tiêu chính là cung cấp các ví dụ code và hướng dẫn chi tiết để người học có thể xây dựng các ứng dụng dựa trên Spring Boot tương tác với Kafka một cách dễ dàng và hiệu quả, đồng thời hiểu rõ các phương pháp hay nhất trong việc xử lý tin nhắn và lỗi.

## 2. Yêu cầu

### 2.1 Yêu cầu Chức năng

*   Là người học, tôi muốn có thể tạo một dự án Spring Boot mới và thêm các dependency cần thiết để tích hợp Spring Kafka.
*   Là người học, tôi muốn có thể cấu hình Producer trong Spring Boot để gửi tin nhắn đến các topic Kafka.
*   Là người học, tôi muốn có thể cấu hình Consumer trong Spring Boot để tiêu thụ tin nhắn từ các topic Kafka.
*   Là người học, tôi muốn có thể triển khai cơ chế xử lý lỗi cho các tin nhắn không thể xử lý được trong Consumer, bao gồm việc sử dụng Dead Letter Topic (DLT).

### 2.2 Yêu cầu Phi chức năng

*   **Dễ sử dụng:** Các ví dụ code phải đơn giản, dễ hiểu và minh họa rõ ràng cách sử dụng Spring Kafka.
*   **Hiệu suất:** Cấu hình mặc định và khuyến nghị nên được tối ưu hóa cho hiệu suất cơ bản, đồng thời đưa ra các lưu ý về tối ưu hóa nâng cao.
*   **Khả năng bảo trì:** Code ví dụ phải tuân thủ các quy ước lập trình Spring Boot và Kafka để dễ dàng bảo trì và mở rộng.
*   **Độ tin cậy:** Các cơ chế xử lý lỗi phải đảm bảo tin nhắn không bị mất và hệ thống có khả năng phục hồi.

## 3. Thiết kế Kỹ thuật

### 3.1. Thay đổi Mô hình Dữ liệu

*   Không có thay đổi mô hình dữ liệu cụ thể ở đây, vì module này tập trung vào cách thức tích hợp và xử lý tin nhắn, không phải cấu trúc dữ liệu của tin nhắn. Tin nhắn sẽ là chuỗi đơn giản hoặc các đối tượng POJO.

### 3.2. Thay đổi API

*   **Spring Boot Application:**
    *   Sử dụng `@SpringBootApplication` để khởi tạo ứng dụng.
    *   Cấu hình Kafka properties trong `application.yml` hoặc `application.properties`.
*   **Kafka Producer (Spring Kafka):**
    *   Sử dụng `KafkaTemplate` để gửi tin nhắn.
    *   `KafkaTemplate` được cấu hình thông qua `ProducerFactory`.
    *   Ví dụ: `kafkaTemplate.send("my-topic", "key", "value");`
*   **Kafka Consumer (Spring Kafka):**
    *   Sử dụng `@KafkaListener` annotation trên các phương thức để tiêu thụ tin nhắn.
    *   `@KafkaListener` được cấu hình thông qua `ConcurrentKafkaListenerContainerFactory`.
    *   Ví dụ: `@KafkaListener(topics = "my-topic", groupId = "my-group")`
*   **Xử lý lỗi:**
    *   Sử dụng `DefaultErrorHandler` với `DeadLetterPublishingRecoverer` và `FixedBackOff`/`ExponentialBackOff` để xử lý tin nhắn lỗi.
    *   Cấu hình này sẽ được áp dụng cho `ConcurrentKafkaListenerContainerFactory`.

### 3.3. Luồng Logic

*   **Thiết lập dự án:**
    1.  Tạo dự án Spring Boot mới (sử dụng Spring Initializr).
    2.  Thêm `spring-kafka` dependency.
    3.  Cấu hình `bootstrap-servers` và `group-id` trong `application.yml`.
*   **Gửi tin nhắn:**
    1.  `@Autowired` `KafkaTemplate` vào một service hoặc controller.
    2.  Gọi phương thức `send()` của `KafkaTemplate` để gửi tin nhắn.
    3.  Có thể lắng nghe kết quả gửi bằng `ListenableFuture`.
*   **Tiêu thụ tin nhắn:**
    1.  Tạo một class hoặc component và đánh dấu nó là `@Component` hoặc `@Service`.
    2.  Tạo một phương thức trong class đó và đánh dấu nó bằng `@KafkaListener`.
    3.  Trong phương thức listener, xử lý tin nhắn nhận được.
*   **Xử lý lỗi:**
    1.  Định nghĩa một `ConcurrentKafkaListenerContainerFactory` bean.
    2.  Cấu hình `DefaultErrorHandler` với `DeadLetterPublishingRecoverer` và một chiến lược `BackOff` (ví dụ: `FixedBackOff` cho số lần thử lại).
    3.  Khi một ngoại lệ xảy ra trong listener, `DefaultErrorHandler` sẽ thử lại tin nhắn theo chiến lược `BackOff`. Nếu vẫn thất bại, `DeadLetterPublishingRecoverer` sẽ gửi tin nhắn đó đến một Dead Letter Topic (DLT).

### 3.4. Phụ thuộc

*   `org.springframework.kafka:spring-kafka`
*   `org.springframework.boot:spring-boot-starter-web` (tùy chọn, nếu xây dựng API REST)
*   `org.springframework.boot:spring-boot-starter-test` (cho kiểm thử)

### 3.5. Cân nhắc Bảo mật

*   Module này tập trung vào tích hợp Spring Kafka cơ bản. Các cấu hình bảo mật (SSL/TLS, SASL) đã được đề cập trong Module 4 và sẽ được áp dụng thông qua các thuộc tính cấu hình Kafka trong `application.yml` của Spring Boot.
*   Cần lưu ý bảo vệ thông tin nhạy cảm như mật khẩu hoặc keystore/truststore trong môi trường sản xuất (sử dụng Spring Cloud Config, Vault, hoặc biến môi trường).

### 3.6. Cân nhắc Hiệu suất

*   **`concurrency`:** Cấu hình `concurrency` của `ConcurrentKafkaListenerContainerFactory` để tối ưu hóa việc xử lý song song, phù hợp với số lượng phân vùng.
*   **Batch processing:** Sử dụng `@KafkaListener(containerFactory = "batchFactory")` để xử lý tin nhắn theo lô, có thể cải thiện thông lượng.
*   **`max.poll.records`:** Điều chỉnh số lượng bản ghi tối đa mà consumer kéo về trong mỗi lần poll.
*   **Auto-commit vs. Manual commit:** Khuyến nghị tắt `enable.auto.commit` và sử dụng commit thủ công (`Acknowledgment.acknowledge()`) để kiểm soát chính xác thời điểm commit offset, đảm bảo độ tin cậy.

## 4. Kế hoạch Kiểm tra

*   **Kiểm tra đơn vị:**
    *   Kiểm tra logic của các service gửi tin nhắn (sử dụng `KafkaTemplate` mock).
    *   Kiểm tra logic của các service xử lý tin nhắn (sử dụng `@KafkaListener` mock hoặc gọi trực tiếp phương thức).
*   **Kiểm tra tích hợp:**
    *   Sử dụng `EmbeddedKafkaBroker` từ Spring Kafka Test để thiết lập một Kafka broker nhúng trong môi trường test.
    *   Viết các test case end-to-end để gửi tin nhắn bằng `KafkaTemplate` và xác minh rằng `@KafkaListener` nhận và xử lý chúng đúng cách.
    *   Kiểm tra cơ chế xử lý lỗi bằng cách mô phỏng các ngoại lệ trong listener và xác minh tin nhắn được gửi đến DLT.

## 5. Câu hỏi Mở

*   Cần hướng dẫn cụ thể hơn về cách thiết lập môi trường Kafka cục bộ với Docker Compose để chạy các ví dụ Spring Kafka.
*   Có nên bao gồm ví dụ về Kafka Transactions trong Spring Kafka?

## 6. Các Phương án Đã Xem xét

*   **Không sử dụng Spring Kafka, chỉ sử dụng Kafka client API:**
    *   **Ưu điểm:** Kiểm soát hoàn toàn các cấu hình và luồng xử lý.
    *   **Nhược điểm:** Tăng đáng kể lượng code boilerplate để quản lý lifecycle của Producer/Consumer, xử lý thread, deserialization, và xử lý lỗi.
    *   **Lý do từ chối:** Spring Kafka cung cấp một mức độ trừu tượng cao hơn và tích hợp chặt chẽ với Spring ecosystem, giúp tăng năng suất phát triển và giảm thiểu lỗi.