# Điều cần tránh: Bỏ qua việc cấu hình `ConsumerFactory` và `ProducerFactory` chi tiết

## Mô tả
Trong Spring for Apache Kafka, `ConsumerFactory` và `ProducerFactory` là các thành phần cốt lõi chịu trách nhiệm tạo ra các thể hiện của Kafka `Consumer` và `Producer` client. Việc bỏ qua cấu hình chi tiết cho các factory này, chỉ dựa vào các giá trị mặc định hoặc cấu hình tối thiểu, có thể dẫn đến hiệu suất dưới mức tối ưu, hành vi không mong muốn hoặc thiếu khả năng kiểm soát đối với các client Kafka cơ bản.

## Vai trò của `ConsumerFactory` và `ProducerFactory`
*   **`ProducerFactory`:** Chịu trách nhiệm tạo ra các thể hiện của `org.apache.kafka.clients.producer.Producer`. Nó chứa tất cả các cấu hình liên quan đến Producer (ví dụ: `bootstrap.servers`, `key.serializer`, `value.serializer`, `acks`, `retries`, `enable.idempotence`).
*   **`ConsumerFactory`:** Chịu trách nhiệm tạo ra các thể hiện của `org.apache.kafka.clients.consumer.Consumer`. Nó chứa tất cả các cấu hình liên quan đến Consumer (ví dụ: `bootstrap.servers`, `group.id`, `key.deserializer`, `value.deserializer`, `enable.auto.commit`).

Spring Kafka sử dụng các factory này để tạo ra các `KafkaTemplate` (cho Producer) và các `MessageListenerContainer` (cho Consumer) một cách tự động.

## Các vấn đề khi bỏ qua cấu hình chi tiết
1.  **Hiệu suất kém:** Các cấu hình mặc định có thể không phù hợp với khối lượng công việc hoặc yêu cầu hiệu suất cụ thể của ứng dụng của bạn. Ví dụ:
    *   Producer: `linger.ms`, `batch.size`, `compression.type`, `buffer.memory`.
    *   Consumer: `fetch.min.bytes`, `fetch.max.wait.ms`, `max.poll.records`, `max.poll.interval.ms`.
2.  **Hành vi không mong muốn:** Các vấn đề như mất dữ liệu, trùng lặp tin nhắn, hoặc lag consumer cao có thể xuất hiện nếu các cấu hình liên quan đến độ bền và độ tin cậy không được đặt đúng cách. Ví dụ: không cấu hình `acks` hoặc `enable.idempotence` cho Producer, hoặc `enable.auto.commit` cho Consumer.
3.  **Khó khăn trong gỡ lỗi:** Khi vấn đề phát sinh, việc không có cấu hình rõ ràng làm cho việc xác định nguyên nhân gốc rễ trở nên khó khăn hơn.
4.  **Thiếu kiểm soát:** Bạn mất khả năng tinh chỉnh hành vi của Kafka client để phù hợp với các yêu cầu nghiệp vụ hoặc môi trường triển khai.

## Các cấu hình quan trọng cần xem xét
### Cho `ProducerFactory`
*   **`bootstrap.servers`:** Địa chỉ của Kafka brokers.
*   **`key.serializer`, `value.serializer`:** Các lớp tuần tự hóa cho khóa và giá trị.
*   **`acks`:** Mức độ đảm bảo ghi tin nhắn (`0`, `1`, `all`). Luôn dùng `all` cho dữ liệu quan trọng.
*   **`retries`:** Số lần thử lại khi gửi tin nhắn thất bại.
*   **`enable.idempotence`:** Bật tính năng bất biến để đảm bảo chính xác một lần.
*   **`transactional.id`:** Cần thiết nếu sử dụng giao dịch Kafka.
*   **`linger.ms`:** Thời gian chờ tối đa để batch tin nhắn.
*   **`batch.size`:** Kích thước batch tối đa.
*   **`compression.type`:** Loại nén dữ liệu (`gzip`, `snappy`, `lz4`, `zstd`).

### Cho `ConsumerFactory`
*   **`bootstrap.servers`:** Địa chỉ của Kafka brokers.
*   **`group.id`:** ID của consumer group.
*   **`key.deserializer`, `value.deserializer`:** Các lớp giải tuần tự hóa cho khóa và giá trị.
*   **`enable.auto.commit`:** Bật/tắt tự động commit offset. Khuyến nghị `false` cho môi trường sản xuất.
*   **`auto.offset.reset`:** Hành vi khi không có offset đã commit (`earliest`, `latest`).
*   **`isolation.level`:** Mức độ cách ly giao dịch (`read_committed`, `read_uncommitted`).
*   **`max.poll.records`:** Số lượng tin nhắn tối đa trả về trong mỗi lần poll.
*   **`max.poll.interval.ms`:** Thời gian tối đa giữa các lần poll trước khi consumer bị coi là chết.
*   **`session.timeout.ms`:** Thời gian tối đa consumer có thể không gửi heartbeat trước khi bị coi là chết và bị loại khỏi group.

## Khuyến nghị và phương pháp hay nhất
*   **Luôn định nghĩa rõ ràng các `ProducerFactory` và `ConsumerFactory`:** Đừng chỉ dựa vào các cấu hình mặc định.
*   **Tạo Bean riêng cho từng Factory:**
    ```java
    @Configuration
    public class KafkaConfig {

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // Quan trọng!
            configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Quan trọng!
            // Thêm các cấu hình khác
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-app-group");
            configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Quan trọng!
            configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            // Thêm các cấu hình khác
            return new DefaultKafkaConsumerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            factory.setConcurrency(3); // Cấu hình concurrency phù hợp
            // Cấu hình error handler, message converter, v.v.
            return factory;
        }
    }
    ```
*   **Sử dụng `ProducerConfig` và `ConsumerConfig`:** Luôn sử dụng các hằng số từ `org.apache.kafka.clients.producer.ProducerConfig` và `org.apache.kafka.clients.consumer.ConsumerConfig` để tránh lỗi chính tả và tăng tính dễ đọc.
*   **Tài liệu hóa cấu hình:** Ghi lại các cấu hình quan trọng và lý do tại sao chúng được đặt như vậy.
*   **Giám sát hiệu suất:** Theo dõi các chỉ số Producer và Consumer để đảm bảo cấu hình đang hoạt động như mong đợi và điều chỉnh khi cần thiết.