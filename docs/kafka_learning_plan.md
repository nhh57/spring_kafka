# Kế hoạch Giảng dạy Kafka Chuyên sâu

Chào bạn, với 20 năm kinh nghiệm trong lĩnh vực Spring và Kafka, tôi rất sẵn lòng chia sẻ kiến thức chuyên sâu để bạn có thể nắm vững Kafka từ những chi tiết nhỏ nhất. Mục tiêu của chúng ta là không chỉ hiểu "cách làm" mà còn "tại sao làm", "nó giải quyết vấn đề gì", và "khi nào nên sử dụng".

Dưới đây là kế hoạch giảng dạy chi tiết mà tôi đề xuất, được trình bày theo từng module:

## Module 1: Kafka Fundamentals (Nền tảng của Kafka)

### 1.1 What is Kafka? (Kafka là gì?)
- **Mục đích:** Hiểu Kafka là một nền tảng streaming phân tán, khác biệt với các hệ thống hàng đợi tin nhắn truyền thống.
- **Vấn đề giải quyết:** Các hệ thống hàng đợi tin nhắn truyền thống (như RabbitMQ, ActiveMQ) thường gặp khó khăn với thông lượng cao, lưu trữ bền vững và xử lý luồng dữ liệu thời gian thực ở quy mô lớn. Kafka giải quyết những vấn đề này bằng cách coi dữ liệu là một chuỗi các bản ghi không thể thay đổi, có thứ tự (một dạng log).
- **Khi nào nên sử dụng:** Khi bạn cần xử lý khối lượng lớn luồng dữ liệu, yêu cầu khả năng chịu lỗi, tính sẵn sàng cao và khả năng xử lý dữ liệu theo thời gian thực hoặc gần thời gian thực.

### 1.2 Why Kafka? (Tại sao lại là Kafka? Các lợi ích cốt lõi)
- **Mục đích:** Nắm bắt những lợi ích cốt lõi và phân biệt Kafka với các hệ thống nhắn tin khác.
- **Vấn đề giải quyết:**
    - **Khả năng mở rộng (Scalability):** Các hệ thống nguyên khối hoặc hàng đợi truyền thống đạt đến giới hạn; Kafka mở rộng theo chiều ngang bằng cách thêm nhiều broker hơn.
    - **Độ bền & Khả năng chịu lỗi (Durability & Fault Tolerance):** Dữ liệu được lưu trữ trên đĩa và được sao chép trên nhiều broker, đảm bảo không mất dữ liệu ngay cả khi một broker bị lỗi.
    - **Thông lượng cao & Độ trễ thấp (High Throughput & Low Latency):** Được thiết kế để thu nạp và phân phối dữ liệu hiệu suất cao.
    - **Xử lý thời gian thực (Real-time Processing):** Cho phép xử lý dữ liệu ngay lập tức khi nó đến.
    - **Tách rời (Decoupling):** Các producer và consumer hoạt động độc lập, cho phép kiến trúc linh hoạt.
- **Khi nào nên sử dụng:** Khi xây dựng kiến trúc hướng sự kiện, giao tiếp giữa các microservice, tổng hợp nhật ký, phân tích thời gian thực hoặc các đường ống dữ liệu.

### 1.3 When to Use Kafka? (Các trường hợp sử dụng cụ thể)
- **Mục đích:** Xác định các kịch bản thực tế mà Kafka là lựa chọn lý tưởng.
- **Vấn đề giải quyết:** Các vấn đề kinh doanh cụ thể yêu cầu luồng sự kiện.
- **Khi nào nên sử dụng:**
    - **Tổng hợp nhật ký (Log Aggregation):** Tập trung nhật ký từ các dịch vụ khác nhau để phân tích.
    - **Xử lý luồng (Stream Processing):** Phát hiện gian lận thời gian thực, đề xuất cá nhân hóa, xử lý dữ liệu IoT.
    - **Event Sourcing:** Lưu trữ một chuỗi các sự kiện làm nguồn chân lý chính cho trạng thái ứng dụng.
    - **Giao tiếp Microservices:** Giao tiếp không đồng bộ giữa các dịch vụ có tính kết nối lỏng lẻo.
    - **Đường ống dữ liệu (Data Pipelines):** Di chuyển dữ liệu một cách đáng tin cậy và hiệu quả giữa các hệ thống (ví dụ: từ cơ sở dữ liệu đến kho dữ liệu).

### 1.4 Core Abstractions: Messages, Topics, Partitions (Các khái niệm cốt lõi: Tin nhắn, Topic, Phân vùng)
- **Mục đích:** Học các khối xây dựng cơ bản của mô hình dữ liệu Kafka.
- **Vấn đề giải quyết:** Cách Kafka tổ chức và phân phối dữ liệu để mở rộng và xử lý song song.
- **Khi nào nên sử dụng:** Rất quan trọng để thiết kế các luồng dữ liệu hiệu quả; hiểu cách dữ liệu được sắp xếp, lưu trữ và tiêu thụ.

### 1.5 Core Abstractions: Brokers, Zookeeper/KRaft (Các khái niệm cốt lõi: Broker, Zookeeper/KRaft)
- **Mục đích:** Hiểu các thành phần phía máy chủ của một cụm Kafka.
- **Vấn đề giải quyết:** Cách các broker Kafka quản lý topic/phân vùng và cách siêu dữ liệu cụm được duy trì. Zookeeper (cũ) và KRaft (hiện đại) giải quyết vấn đề điều phối phân tán cho Kafka.
- **Khi nào nên sử dụng:** Rất quan trọng để thiết lập, vận hành và khắc phục sự cố cụm Kafka. KRaft đơn giản hóa việc triển khai và vận hành bằng cách loại bỏ phụ thuộc Zookeeper.

### Vấn đề thường gặp & Cạm bẫy (Module 1)
- **Vấn đề 1:** Hiểu nhầm Kafka là Message Queue truyền thống.
    - **Chi tiết:** [`docs/common_problems/kafka_not_mq.md`](docs/common_problems/kafka_not_mq.md)
- **Vấn đề 2:** Chọn sai số lượng phân vùng cho Topic.
    - **Chi tiết:** [`docs/common_problems/partition_count.md`](docs/common_problems/partition_count.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 1)
- **Thực hành tốt 1:** Luôn định nghĩa rõ ràng mục đích của từng Topic.
    - **Chi tiết:** [`docs/best_practices/topic_purpose.md`](docs/best_practices/topic_purpose.md)
- **Điều cần tránh 1:** Sử dụng Topic quá "chung chung" cho nhiều loại dữ liệu.
    - **Chi tiết:** [`docs/best_practices/avoid_generic_topics.md`](docs/best_practices/avoid_generic_topics.md)

## Module 2: Kafka Architecture & Components Deep Dive (Đi sâu vào Kiến trúc & Thành phần Kafka)

### 2.1 Kafka Cluster Setup & Components (Brokers, Controller) (Thiết lập cụm Kafka & Các thành phần)
- **Mục đích:** Hiểu cấu trúc vật lý và logic của một cụm Kafka.
- **Vấn đề giải quyết:** Cách nhiều broker hoạt động cùng nhau để tạo thành một hệ thống có tính sẵn sàng cao và khả năng mở rộng. Controller chịu trách nhiệm bầu chọn leader và gán phân vùng.
- **Khi nào nên sử dụng:** Khi triển khai, quản lý hoặc mở rộng môi trường Kafka.

### 2.2 Zookeeper vs. KRaft (Evolution of Metadata Management) (Zookeeper so với KRaft: Sự tiến hóa của quản lý siêu dữ liệu)
- **Mục đích:** Hiểu sự thay đổi trong cơ chế điều phối nội bộ của Kafka.
- **Vấn đề giải quyết:** KRaft (Kafka Raft) loại bỏ sự phụ thuộc bên ngoài vào Zookeeper, đơn giản hóa kiến trúc, triển khai và chi phí vận hành của Kafka. Nó cũng cải thiện khả năng mở rộng và thời gian khôi phục.
- **Khi nào nên sử dụng:** Đối với các triển khai Kafka mới, KRaft là lựa chọn được khuyến nghị. Đối với các triển khai hiện có, việc hiểu lộ trình di chuyển là quan trọng.

### 2.3 Producers (Sending Messages) (Producer: Gửi tin nhắn)
- **Mục đích:** Học cách các ứng dụng gửi dữ liệu đến các topic Kafka.
- **Vấn đề giải quyết:** Thu nạp dữ liệu một cách đáng tin cậy và hiệu quả vào Kafka.
- **Khi nào nên sử dụng:** Bất kỳ ứng dụng nào tạo dữ liệu để được xử lý bởi các hệ thống khác hoặc lưu trữ trong Kafka.

### 2.4 Producer Configuration & Guarantees (Acks, Retries, Idempotence, Transactions) (Cấu hình Producer & Đảm bảo: Acks, Thử lại, Tính bất biến, Giao dịch)
- **Mục đích:** Nắm vững các cài đặt kiểm soát độ bền dữ liệu và đảm bảo phân phối.
- **Vấn đề giải quyết:** Đảm bảo tin nhắn được gửi chính xác một lần, ít nhất một lần hoặc nhiều nhất một lần, và xử lý lỗi mạng hoặc sự cố broker một cách duyên dáng.
- **Khi nào nên sử dụng:** Rất quan trọng để xây dựng các đường ống dữ liệu mạnh mẽ nơi tính toàn vẹn dữ liệu là tối quan trọng.

### 2.5 Consumers (Receiving Messages) (Consumer: Nhận tin nhắn)
- **Mục đích:** Học cách các ứng dụng đọc dữ liệu từ các topic Kafka.
- **Vấn đề giải quyết:** Tiêu thụ dữ liệu từ Kafka một cách hiệu quả và có khả năng mở rộng.
- **Khi nào nên sử dụng:** Bất kỳ ứng dụng nào cần xử lý luồng dữ liệu từ Kafka.

### 2.6 Consumer Groups & Offsets (Nhóm Consumer & Offset)
- **Mục đích:** Hiểu cách nhiều consumer có thể hoạt động cùng nhau để tiêu thụ một topic song song và cách Kafka theo dõi tiến độ của chúng.
- **Vấn đề giải quyết:** Cho phép tiêu thụ có khả năng mở rộng và đảm bảo rằng mỗi tin nhắn chỉ được xử lý bởi một consumer trong một nhóm, đồng thời cho phép các nhóm khác nhau tiêu thụ độc lập. Offset theo dõi vị trí đọc.
- **Khi nào nên sử dụng:** Để xây dựng các ứng dụng phân tán cần xử lý dữ liệu đồng thời và duy trì trạng thái của chúng.

### 2.7 Delivery Semantics (At-most-once, At-least-once, Exactly-once) (Ngữ nghĩa phân phối: Nhiều nhất một lần, Ít nhất một lần, Chính xác một lần)
- **Mục đích:** Hiểu các mức độ đảm bảo phân phối tin nhắn khác nhau mà Kafka cung cấp.
- **Vấn đề giải quyết:** Lựa chọn đảm bảo phù hợp dựa trên yêu cầu ứng dụng (ví dụ: giao dịch tài chính yêu cầu chính xác một lần, nhật ký có thể chấp nhận ít nhất một lần).
- **Khi nào nên sử dụng:** Khi thiết kế các đường ống dữ liệu quan trọng nơi việc mất hoặc trùng lặp dữ liệu phải được quản lý cẩn thận.

### 2.8 Replication (Leader/Follower, ISRs) (Sao chép: Leader/Follower, ISRs)
- **Mục đích:** Hiểu cách Kafka đảm bảo độ bền dữ liệu và khả năng chịu lỗi.
- **Vấn đề giải quyết:** Ngăn chặn mất dữ liệu và duy trì tính sẵn sàng cao bằng cách sao chép dữ liệu phân vùng trên nhiều broker.
- **Khi nào nên sử dụng:** Rất cần thiết để cấu hình một cụm Kafka cấp sản xuất.

### Vấn đề thường gặp & Cạm bẫy (Module 2)
- **Vấn đề 1:** Cấu hình Producer không đúng dẫn đến mất dữ liệu hoặc trùng lặp.
    - **Chi tiết:** [`docs/common_problems/producer_config_issues.md`](docs/common_problems/producer_config_issues.md)
- **Vấn đề 2:** Hiểu sai về Consumer Group và Offset Commit.
    - **Chi tiết:** [`docs/common_problems/consumer_group_offset.md`](docs/common_problems/consumer_group_offset.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 2)
- **Thực hành tốt 1:** Luôn sử dụng `acks=all` và `min.insync.replicas` cho dữ liệu quan trọng.
    - **Chi tiết:** [`docs/best_practices/producer_acks.md`](docs/best_practices/producer_acks.md)
- **Điều cần tránh 1:** Tắt chế độ tự động commit offset nếu không hiểu rõ.
    - **Chi tiết:** [`docs/best_practices/avoid_auto_commit.md`](docs/best_practices/avoid_auto_commit.md)

## Module 3: Kafka APIs Deep Dive (Đi sâu vào các API Kafka)

### 3.1 Producer API (Java Client) (API Producer: Client Java)
- **Mục đích:** Trải nghiệm thực tế với việc gửi tin nhắn theo chương trình.
- **Vấn đề giải quyết:** Tương tác trực tiếp với Kafka từ mã ứng dụng.
- **Khi nào nên sử dụng:** Khi viết các ứng dụng tùy chỉnh để tạo dữ liệu cho Kafka.

### 3.2 Consumer API (Java Client) (API Consumer: Client Java)
- **Mục đích:** Trải nghiệm thực tế với việc tiêu thụ tin nhắn theo chương trình.
- **Vấn đề giải quyết:** Tương tác trực tiếp với Kafka từ mã ứng dụng để đọc dữ liệu.
- **Khi nào nên sử dụng:** Khi viết các ứng dụng tùy chỉnh để tiêu thụ dữ liệu từ Kafka.

### 3.3 Kafka Streams: Building Stream Processing Applications (Kafka Streams: Xây dựng ứng dụng xử lý luồng)
- **Mục đích:** Học cách xây dựng các ứng dụng xử lý luồng mạnh mẽ, có khả năng mở rộng và chịu lỗi.
- **Vấn đề giải quyết:** Chuyển đổi, tổng hợp và phân tích dữ liệu thời gian thực trực tiếp trong Kafka, mà không cần một cụm xử lý riêng biệt (như Spark Streaming hoặc Flink).
- **Khi nào nên sử dụng:** Để xử lý dữ liệu trong luồng, phân tích thời gian thực, microservice hướng sự kiện hoặc xây dựng các materialized view.

### 3.4 Kafka Connect: Integrating with External Systems (Kafka Connect: Tích hợp với các hệ thống bên ngoài)
- **Mục đích:** Hiểu cách dễ dàng di chuyển dữ liệu giữa Kafka và các hệ thống dữ liệu khác.
- **Vấn đề giải quyết:** Đơn giản hóa việc tích hợp dữ liệu bằng cách cung cấp một khuôn khổ để xây dựng và chạy các connector có thể tái sử dụng (ví dụ: kết nối với cơ sở dữ liệu, S3, Elasticsearch).
- **Khi nào nên sử dụng:** Để xây dựng các đường ống dữ liệu mạnh mẽ và có khả năng mở rộng cho ETL (Extract, Transform, Load) hoặc CDC (Change Data Capture).

### Vấn đề thường gặp & Cạm bẫy (Module 3)
- **Vấn đề 1:** Hiệu suất kém khi sử dụng Kafka Streams (ví dụ: State Store không tối ưu).
    - **Chi tiết:** [`docs/common_problems/kafka_streams_performance.md`](docs/common_problems/kafka_streams_performance.md)
- **Vấn đề 2:** Xử lý lỗi trong Kafka Connect không hiệu quả.
    - **Chi tiết:** [`docs/common_problems/kafka_connect_error_handling.md`](docs/common_problems/kafka_connect_error_handling.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 3)
- **Thực hành tốt 1:** Sử dụng Kafka Streams cho các tác vụ xử lý luồng nhẹ, Kafka Connect cho tích hợp dữ liệu.
    - **Chi tiết:** [`docs/best_practices/streams_connect_usage.md`](docs/best_practices/streams_connect_usage.md)
- **Điều cần tránh 1:** Viết Producer/Consumer client thủ công khi có thể sử dụng Kafka Connect.
    - **Chi tiết:** [`docs/best_practices/avoid_manual_clients.md`](docs/best_practices/avoid_manual_clients.md)

## Module 4: Advanced Kafka Concepts & Operations (Các khái niệm nâng cao & Vận hành Kafka)

### 4.1 Serialization/Deserialization (Serdes) (Tuần tự hóa/Giải tuần tự hóa)
- **Mục đích:** Hiểu cách dữ liệu được chuyển đổi thành byte để truyền và ngược lại.
- **Vấn đề giải quyết:** Đảm bảo khả năng tương thích dữ liệu giữa các producer và consumer. Thảo luận về Avro, Protobuf, JSON và các serializer tùy chỉnh.
- **Khi nào nên sử dụng:** Cơ bản cho bất kỳ ứng dụng Kafka nào; việc chọn Serde phù hợp ảnh hưởng đến hiệu suất, sự phát triển schema và khả năng tương tác.

### 4.2 Custom Partitions (Phân vùng tùy chỉnh)
- **Mục đích:** Học cách kiểm soát việc phân phối tin nhắn trên các phân vùng.
- **Vấn đề giải quyết:** Đảm bảo các tin nhắn có cùng khóa đi đến cùng một phân vùng hoặc phân phối tin nhắn đồng đều để xử lý song song tốt hơn.
- **Khi nào nên sử dụng:** Khi các yêu cầu đặt hàng hoặc xử lý cụ thể quyết định cách tin nhắn nên được định tuyến trong một topic.

### 4.3 Security in Kafka (Authentication, Authorization, Encryption) (Bảo mật trong Kafka: Xác thực, Ủy quyền, Mã hóa)
- **Mục đích:** Bảo mật các cụm và dữ liệu Kafka.
- **Vấn đề giải quyết:** Bảo vệ dữ liệu nhạy cảm, kiểm soát quyền truy cập và đảm bảo giao tiếp an toàn trong cụm và với các client.
- **Khi nào nên sử dụng:** Bắt buộc đối với bất kỳ môi trường sản xuất nào. Bao gồm SSL/TLS để mã hóa, SASL để xác thực (PLAIN, GSSAPI/Kerberos, SCRAM) và ACL để ủy quyền.

### 4.4 Monitoring & Alerting (Giám sát & Cảnh báo)
- **Mục đích:** Quan sát tình trạng và hiệu suất của cụm Kafka.
- **Vấn đề giải quyết:** Xác định các tắc nghẽn, lỗi và sự cố hiệu suất trong thời gian thực.
- **Khi nào nên sử dụng:** Rất cần thiết cho sự ổn định hoạt động. Bao gồm các chỉ số JMX, tích hợp Prometheus/Grafana và các công cụ giám sát dành riêng cho Kafka.

### 4.5 Performance Tuning (Producers, Consumers, Brokers) (Điều chỉnh hiệu suất: Producer, Consumer, Broker)
- **Mục đích:** Tối ưu hóa Kafka cho các khối lượng công việc cụ thể.
- **Vấn đề giải quyết:** Tối đa hóa thông lượng và giảm thiểu độ trễ.
- **Khi nào nên sử dụng:** Khi các yêu cầu hiệu suất là rất quan trọng. Bao gồm batching, kích thước bộ đệm, nén và các tham số cấu hình khác.

### 4.6 Schema Registry & Schema Evolution (Schema Registry & Sự phát triển Schema)
- **Mục đích:** Quản lý và thực thi các schema cho tin nhắn Kafka.
- **Vấn đề giải quyết:** Đảm bảo khả năng tương thích và sự phát triển của dữ liệu khi các ứng dụng thay đổi, ngăn chặn các thay đổi gây phá vỡ giữa các producer và consumer.
- **Khi nào nên sử dụng:** Rất khuyến nghị cho các triển khai Kafka quy mô lớn, cấp doanh nghiệp để duy trì chất lượng dữ liệu và khả năng tương tác.

### Vấn đề thường gặp & Cạm bẫy (Module 4)
- **Vấn đề 1:** Quản lý Schema không hiệu quả dẫn đến lỗi tương thích dữ liệu.
    - **Chi tiết:** [`docs/common_problems/schema_management.md`](docs/common_problems/schema_management.md)
- **Vấn đề 2:** Cấu hình bảo mật không đúng gây ra lỗ hổng.
    - **Chi tiết:** [`docs/common_problems/security_misconfiguration.md`](docs/common_problems/security_misconfiguration.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 4)
- **Thực hành tốt 1:** Luôn sử dụng Schema Registry cho dữ liệu có cấu trúc.
    - **Chi tiết:** [`docs/best_practices/use_schema_registry.md`](docs/best_practices/use_schema_registry.md)
- **Điều cần tránh 1:** Bỏ qua việc giám sát và cảnh báo cho cụm Kafka.
    - **Chi tiết:** [`docs/best_practices/monitor_kafka_cluster.md`](docs/best_practices/monitor_kafka_cluster.md)

## Module 5: Spring for Apache Kafka (Spring cho Apache Kafka)

### 5.1 Introduction to Spring Kafka (Giới thiệu về Spring Kafka)
- **Mục đích:** Hiểu cách Spring đơn giản hóa việc tích hợp Kafka trong các ứng dụng Java.
- **Vấn đề giải quyết:** Giảm mã boilerplate và tận dụng tính năng injection phụ thuộc của Spring và quy ước hơn cấu hình cho các client Kafka.
- **Khi nào nên sử dụng:** Khi phát triển các ứng dụng Kafka trong hệ sinh thái Spring.

### 5.2 KafkaTemplate (Sending Messages) (KafkaTemplate: Gửi tin nhắn)
- **Mục đích:** Học thành phần chính để gửi tin nhắn trong Spring Kafka.
- **Vấn đề giải quyết:** Cung cấp một lớp trừu tượng cấp cao để tạo tin nhắn, xử lý kết quả gửi và xử lý lỗi.
- **Khi nào nên sử dụng:** Đối với tất cả các hoạt động tạo tin nhắn trong các ứng dụng Spring.

### 5.3 @KafkaListener (Consuming Messages) (@KafkaListener: Tiêu thụ tin nhắn)
- **Mục đích:** Học cách tiếp cận dựa trên annotation để tiêu thụ tin nhắn.
- **Vấn đề giải quyết:** Đơn giản hóa việc tiêu thụ tin nhắn bằng cách tự động xử lý quản lý nhóm consumer, cam kết offset và giải tuần tự hóa.
- **Khi nào nên sử dụng:** Đối với tất cả các hoạt động tiêu thụ tin nhắn trong các ứng dụng Spring.

### 5.4 Spring Kafka Configuration (ProducerFactory, ConsumerFactory, ContainerFactory) (Cấu hình Spring Kafka)
- **Mục đích:** Hiểu các thành phần cấu hình cốt lõi của Spring Kafka.
- **Vấn đề giải quyết:** Tùy chỉnh các thuộc tính client Kafka, tính đồng thời và hành vi của listener.
- **Khi nào nên sử dụng:** Khi thiết lập các ứng dụng Spring Kafka và tinh chỉnh hành vi của chúng.

### 5.5 Error Handling in Spring Kafka (Error Handlers, Dead Letter Topics) (Xử lý lỗi trong Spring Kafka)
- **Mục đích:** Triển khai các chiến lược xử lý lỗi mạnh mẽ cho việc xử lý tin nhắn.
- **Vấn đề giải quyết:** Xử lý ngoại lệ một cách duyên dáng trong quá trình tiêu thụ, ngăn chặn mất tin nhắn và chuyển hướng các tin nhắn có vấn đề đến các hàng đợi thư chết.
- **Khi nào nên sử dụng:** Rất cần thiết cho các consumer Kafka sẵn sàng cho sản xuất.

### 5.6 Transactions in Spring Kafka (Giao dịch trong Spring Kafka)
- **Mục đích:** Triển khai xử lý chính xác một lần từ đầu đến cuối trong Spring Kafka.
- **Vấn đề giải quyết:** Đảm bảo tính nguyên tử của các hoạt động trên việc tạo và tiêu thụ tin nhắn, và tương tác với các hệ thống bên ngoài.
- **Khi nào nên sử dụng:** Đối với các quy trình kinh doanh quan trọng yêu cầu ngữ nghĩa chính xác một lần.

### 5.7 Spring Kafka Streams Integration (Tích hợp Spring Kafka Streams)
- **Mục đích:** Tích hợp các ứng dụng Kafka Streams trong hệ sinh thái Spring.
- **Vấn đề giải quyết:** Tận dụng tính năng tự động cấu hình và injection phụ thuộc của Spring Boot cho các ứng dụng Kafka Streams.
- **Khi nào nên sử dụng:** Khi xây dựng các ứng dụng Kafka Streams trong môi trường Spring.

### Vấn đề thường gặp & Cạm bẫy (Module 5)
- **Vấn đề 1:** Xử lý lỗi không đúng trong `@KafkaListener` dẫn đến tin nhắn bị kẹt.
    - **Chi tiết:** [`docs/common_problems/spring_kafka_error_handling.md`](docs/common_problems/spring_kafka_error_handling.md)
- **Vấn đề 2:** Cấu hình `concurrency` không phù hợp trong Spring Kafka.
    - **Chi tiết:** [`docs/common_problems/spring_kafka_concurrency.md`](docs/common_problems/spring_kafka_concurrency.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 5)
- **Thực hành tốt 1:** Sử dụng `DeadLetterPublishingRecoverer` cho các tin nhắn lỗi.
    - **Chi tiết:** [`docs/best_practices/spring_kafka_dlq.md`](docs/best_practices/spring_kafka_dlq.md)
- **Điều cần tránh 1:** Bỏ qua việc cấu hình `ConsumerFactory` và `ProducerFactory` chi tiết.
    - **Chi tiết:** [`docs/best_practices/spring_kafka_factory_config.md`](docs/best_practices/spring_kafka_factory_config.md)

## Module 6: Real-world Applications & Best Practices (Ứng dụng thực tế & Các phương pháp hay nhất)

### 6.1 Event Sourcing with Kafka (Event Sourcing với Kafka)
- **Mục đích:** Hiểu cách Kafka có thể được sử dụng làm kho sự kiện.
- **Vấn đề giải quyết:** Xây dựng các hệ thống có khả năng mở rộng cao và có thể kiểm tra được, nơi tất cả các thay đổi trạng thái được ghi lại dưới dạng một chuỗi các sự kiện.
- **Khi nào nên sử dụng:** Đối với các hệ thống yêu cầu nhật ký kiểm toán mạnh mẽ, dễ dàng phát lại lịch sử và các mô hình đọc linh hoạt.

### 6.2 Change Data Capture (CDC) with Kafka Connect (Thay đổi dữ liệu: CDC với Kafka Connect)
- **Mục đích:** Học cách ghi lại các thay đổi cơ sở dữ liệu trong thời gian thực bằng Kafka.
- **Vấn đề giải quyết:** Sao chép dữ liệu từ cơ sở dữ liệu sang các hệ thống khác (kho dữ liệu, chỉ mục tìm kiếm) với độ trễ thấp.
- **Khi nào nên sử dụng:** Để đồng bộ hóa dữ liệu, phân tích thời gian thực hoặc điền các materialized view.

### 6.3 Microservices Communication Patterns with Kafka (Các mẫu giao tiếp Microservices với Kafka)
- **Mục đích:** Khám phá các mẫu phổ biến để giao tiếp giữa các dịch vụ bằng Kafka.
- **Vấn đề giải quyết:** Tách rời các microservice, cho phép giao tiếp không đồng bộ và xây dựng kiến trúc hướng sự kiện.
- **Khi nào nên sử dụng:** Khi thiết kế các hệ thống microservice phân tán.

### 6.4 Testing Kafka Applications (Unit, Integration, End-to-End) (Kiểm thử ứng dụng Kafka)
- **Mục đích:** Học các chiến lược hiệu quả để kiểm thử các producer, consumer và stream Kafka.
- **Vấn đề giải quyết:** Đảm bảo tính đúng đắn và độ tin cậy của các ứng dụng dựa trên Kafka.
- **Khi nào nên sử dụng:** Trong suốt vòng đời phát triển để xây dựng niềm tin vào ứng dụng.

### 6.5 Deployment Considerations (Docker, Kubernetes) (Các cân nhắc triển khai)
- **Mục đích:** Hiểu cách triển khai và quản lý Kafka và các ứng dụng Kafka trong môi trường container.
- **Vấn đề giải quyết:** Tự động hóa việc triển khai, mở rộng và quản lý cơ sở hạ tầng Kafka.
- **Khi nào nên sử dụng:** Đối với các triển khai đám mây hiện đại.

### 6.6 Advanced Use Cases & Future Trends (Các trường hợp sử dụng nâng cao & Xu hướng tương lai)
- **Mục đích:** Khám phá các ứng dụng tiên tiến và hệ sinh thái Kafka đang phát triển.
- **Vấn đề giải quyết:** Luôn cập nhật các tính năng và mẫu Kafka mới nhất.
- **Khi nào nên sử dụng:** Để học hỏi và đổi mới liên tục.

### Vấn đề thường gặp & Cạm bẫy (Module 6)
- **Vấn đề 1:** Thiết kế Event Sourcing quá phức tạp.
    - **Chi tiết:** [`docs/common_problems/event_sourcing_complexity.md`](docs/common_problems/event_sourcing_complexity.md)
- **Vấn đề 2:** Bỏ qua việc kiểm thử đầu cuối cho các luồng dữ liệu Kafka.
    - **Chi tiết:** [`docs/common_problems/e2e_testing_neglect.md`](docs/common_problems/e2e_testing_neglect.md)

### Các phương pháp hay nhất & Điều cần tránh (Module 6)
- **Thực hành tốt 1:** Bắt đầu với các kịch bản Event Sourcing đơn giản.
    - **Chi tiết:** [`docs/best_practices/simple_event_sourcing.md`](docs/best_practices/simple_event_sourcing.md)
- **Điều cần tránh 1:** Triển khai Kafka mà không có chiến lược DevOps rõ ràng.
    - **Chi tiết:** [`docs/best_practices/no_devops_strategy.md`](docs/best_practices/no_devops_strategy.md)