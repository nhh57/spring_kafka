---
title: Triển khai Module 3: Kafka APIs Deep Dive - Thực hành
status: planned
created: 2025-08-11T15:22:24
updated: 2025-08-11T15:25:06
id: TASK-XXX
priority: medium
memory_types: [procedural]
dependencies: []
tags: [kafka, streams, connect, java, api]
---

# Triển khai Module 3: Kafka APIs Deep Dive - Thực hành

## Description
Triển khai các ví dụ code và hướng dẫn thực hành cho Module 3 của kế hoạch giảng dạy Kafka, tập trung vào Kafka Streams và Kafka Connect APIs. Mục tiêu là giúp người học nắm vững cách xây dựng ứng dụng xử lý luồng đơn giản và sử dụng các Connector để di chuyển dữ liệu.

## Objectives
*   Hoàn thành triển khai ví dụ Kafka Streams bao gồm `filter`, `map`, `groupByKey`, `count`.
*   Hoàn thành triển khai ví dụ Kafka Connect sử dụng `FileStreamSourceConnector` và `FileStreamSinkConnector`.
*   Tài liệu hóa cách cấu hình và chạy các ví dụ này.

## Checklist

**Kafka Streams - Implementation:**

- [ ] Nhiệm vụ 1: Tạo một sub-project Gradle mới cho Kafka Streams (`kafka-streams-example`).
    - [ ] 1.1: Cập nhật `settings.gradle` để bao gồm `include 'kafka-streams-example'`.
    - [ ] 1.2: Tạo thư mục `kafka-streams-example/src/main/java/com/example/kafka/streams` và `kafka-streams-example/src/test/java/com/example/kafka/streams`.
    - [ ] 1.3: Tạo file `kafka-streams-example/build.gradle` với các dependency cần thiết:
        ```gradle
        plugins {
            id 'application'
        }

        group = 'com.example.kafka'
        version = '1.0.0'

        java {
            sourceCompatibility = '21'
        }

        repositories {
            mavenCentral()
        }

        dependencies {
            implementation 'org.apache.kafka:kafka-streams:3.6.1'
            implementation 'org.slf4j:slf4j-api:2.0.9'
            implementation 'ch.qos.logback:logback-classic:1.4.11'
            implementation 'ch.qos.logback:logback-core:1.4.11'

            testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.1'
            testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.10.1'
            testImplementation 'org.apache.kafka:kafka-streams-test-utils:3.6.1'
        }

        application {
            mainClass = 'com.example.kafka.streams.SimpleStreamProcessor' // Main class for running directly
        }

        tasks.named('test') {
            useJUnitPlatform()
        }
        ```
    - [ ] 1.4: Ghi chú: Đảm bảo project mới độc lập và không ảnh hưởng đến các project hiện có.

- [ ] Nhiệm vụ 2: Triển khai ứng dụng Kafka Streams đơn giản.
    - [ ] 2.1: Tạo lớp `SimpleStreamProcessor.java` tại `kafka-streams-example/src/main/java/com/example/kafka/streams/SimpleStreamProcessor.java`.
    - [ ] 2.2: Viết code để cấu hình Kafka Streams properties (bootstrap servers, application ID, Serdes).
    - [ ] 2.3: Khởi tạo `StreamsBuilder`.
    - [ ] 2.4: Tạo `KStream` từ một topic đầu vào (ví dụ: "input-topic").
        ```java
        KStream<String, String> sourceStream = builder.stream("input-topic", Consumed.with(Serdes.String(), Serdes.String()));
        ```
    - [ ] 2.5: Áp dụng phép biến đổi `filter()`: Giữ lại các tin nhắn có giá trị chứa "important".
        ```java
        KStream<String, String> filteredStream = sourceStream.filter((key, value) -> value.contains("important"));
        ```
    - [ ] 2.6: Áp dụng phép biến đổi `map()`: Chuyển đổi giá trị tin nhắn thành chữ hoa.
        ```java
        KStream<String, String> mappedStream = filteredStream.mapValues(value -> value.toUpperCase());
        ```
    - [ ] 2.7: Áp dụng phép biến đổi `groupByKey()` và `count()`: Đếm số lượng tin nhắn theo key.
        ```java
        KTable<String, Long> countedStream = mappedStream.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                                                    .count(Materialized.as("counts-store"));
        ```
    - [ ] 2.8: Ghi kết quả `KTable` (từ `count`) ra một topic đầu ra (ví dụ: "output-topic-counts").
        ```java
        countedStream.toStream().to("output-topic-counts", Produced.with(Serdes.String(), Serdes.Long()));
        ```
    - [ ] 2.9: Xây dựng và khởi động Kafka Streams application.
    - [ ] 2.10: Ghi chú: Tập trung vào việc minh họa các API cơ bản, giữ logic xử lý đơn giản.

- [ ] Nhiệm vụ 3: Viết unit test cho Kafka Streams với `TopologyTestDriver`.
    - [ ] 3.1: Tạo lớp `SimpleStreamProcessorTest.java` tại `kafka-streams-example/src/test/java/com/example/kafka/streams/SimpleStreamProcessorTest.java`.
    - [ ] 3.2: Khởi tạo `TopologyTestDriver` với cấu hình Streams và Topology từ `SimpleStreamProcessor`.
    - [ ] 3.3: Tạo `TestInputTopic` và `TestOutputTopic` để gửi và nhận tin nhắn trong test.
    - [ ] 3.4: Viết test case cho `filter()`: Gửi tin nhắn có và không có "important", kiểm tra đầu ra.
    - [ ] 3.5: Viết test case cho `map()`: Gửi tin nhắn, kiểm tra giá trị đầu ra có phải chữ hoa không.
    - [ ] 3.6: Viết test case cho `groupByKey()` và `count()`: Gửi nhiều tin nhắn với cùng key và khác key, kiểm tra số lượng đếm.
    - [ ] 3.7: Đảm bảo đóng `TopologyTestDriver` sau mỗi test.
    - [ ] 3.8: Ghi chú: Đảm bảo các test case bao gồm các trường hợp thành công và biên.

**Kafka Connect - Implementation:**

- [ ] Nhiệm vụ 4: Hướng dẫn cài đặt và cấu hình Kafka Connect.
    - [ ] 4.1: Tạo file `docs/setup_guides/kafka_connect_setup.md`.
    - [ ] 4.2: Viết hướng dẫn chi tiết cách tải xuống Kafka (bao gồm Connect), giải nén.
    - [ ] 4.3: Hướng dẫn cấu hình `connect-standalone.properties` (hoặc `connect-distributed.properties`) cho môi trường local.
    - [ ] 4.4: Hướng dẫn cách chạy Kafka Connect ở chế độ standalone.
    - [ ] 4.5: Ghi chú: Tập trung vào các bước cần thiết để người học có thể chạy các Connector ví dụ.

- [ ] Nhiệm vụ 5: Triển khai ví dụ `FileStreamSourceConnector`.
    - [ ] 5.1: Tạo file `source.txt` trong thư mục gốc của dự án với một số dòng dữ liệu mẫu.
    - [ ] 5.2: Tạo file cấu hình Connector `file-source-connector.properties` trong thư mục `kafka-connect-examples/configs` (tạo thư mục nếu chưa có).
        ```properties
        name=local-file-source
        connector.class=FileStreamSource
        tasks.max=1
        file=source.txt
        topic=input-topic
        ```
    - [ ] 5.3: Hướng dẫn chạy Connector bằng lệnh:
        ```bash
        bin/connect-standalone.sh config/connect-standalone.properties kafka-connect-examples/configs/file-source-connector.properties
        ```
    - [ ] 5.4: Ghi chú: Minh họa cách dữ liệu từ file được đưa vào Kafka.

- [ ] Nhiệm vụ 6: Triển khai ví dụ `FileStreamSinkConnector`.
    - [ ] 6.1: Tạo file cấu hình Connector `file-sink-connector.properties` trong thư mục `kafka-connect-examples/configs`.
        ```properties
        name=local-file-sink
        connector.class=FileStreamSink
        tasks.max=1
        file=sink.txt
        topic=output-topic-counts # Đọc từ topic kết quả của Kafka Streams
        key.converter=org.apache.kafka.connect.storage.StringConverter
        value.converter=org.apache.kafka.connect.json.JsonConverter
        value.converter.schemas.enable=false
        ```
    - [ ] 6.2: Hướng dẫn chạy Connector bằng lệnh:
        ```bash
        bin/connect-standalone.sh config/connect-standalone.properties kafka-connect-examples/configs/file-sink-connector.properties
        ```
    - [ ] 6.3: Ghi chú: Minh họa cách dữ liệu từ Kafka được xuất ra file.

- [ ] Nhiệm vụ 7: Giới thiệu và minh họa Single Message Transforms (SMTs).
    - [ ] 7.1: Cập nhật file cấu hình `file-source-connector.properties` để thêm một SMT đơn giản (ví dụ: `InsertField` để thêm timestamp).
        ```properties
        name=local-file-source
        connector.class=FileStreamSource
        tasks.max=1
        file=source.txt
        topic=input-topic
        transforms=InsertTimestamp
        transforms.InsertTimestamp.type=org.apache.kafka.connect.transforms.InsertField$Value
        transforms.InsertTimestamp.timestamp.field=recordTimestamp
        ```
    - [ ] 7.2: Hướng dẫn chạy Connector với SMT và kiểm tra tin nhắn trong topic Kafka (sử dụng `kafka-console-consumer.sh`).
    - [ ] 7.3: Ghi chú: Giải thích mục đích và cách thức hoạt động của SMTs trong ví dụ.

**Tài liệu:**

- [ ] Nhiệm vụ 8: Cập nhật `docs/kafka_coding_plan.md` với các ví dụ code đã triển khai.
    - [ ] 8.1: Thêm phần "Ví dụ code" và "Hướng dẫn chạy" chi tiết cho Kafka Streams trong Module 3.1.
    - [ ] 8.2: Thêm phần "Ví dụ code/Hướng dẫn" chi tiết cho Kafka Connect trong Module 3.2, bao gồm cấu hình Connector và lệnh chạy.
    - [ ] 8.3: Liên kết đến các file code và cấu hình mới.
    - [ ] 8.4: Ghi chú: Đảm bảo tài liệu rõ ràng và dễ theo dõi.

## Progress
Chưa bắt đầu.

## Dependencies
*   Hoàn thành Module 2 thực hành.
*   Môi trường Docker Compose cho Kafka đã được thiết lập và chạy.

## Key Considerations
*   **Kafka Streams:**
    *   **Tại sao sử dụng:** Kafka Streams cung cấp một API cấp cao để xử lý luồng dữ liệu theo thời gian thực trực tiếp từ Kafka, tích hợp chặt chẽ với Kafka mà không cần một cụm xử lý luồng riêng biệt.
    *   **Ưu điểm:** Nhẹ, không cần cluster riêng, hỗ trợ State Store, xử lý fault-tolerance và rebalance tự động.
    *   **Nhược điểm:** Chỉ dành cho Java/Scala, không có khả năng quản lý cluster riêng như Flink/Spark.
    *   **Lỗi thường gặp:** Sai cấu hình Serdes, vấn đề với State Store (RocksDB), rebalance storm.
*   **Kafka Connect:**
    *   **Tại sao sử dụng:** Kafka Connect là một framework mạnh mẽ để tích hợp Kafka với các hệ thống dữ liệu bên ngoài một cách đáng tin cậy và có khả năng mở rộng, giảm thiểu việc viết code boilerplate.
    *   **Ưu điểm:** Cấu hình hơn code, hỗ trợ nhiều Connector có sẵn, xử lý fault-tolerance, scaleable.
    *   **Nhược điểm:** Cần cài đặt riêng, không phù hợp cho logic nghiệp vụ phức tạp.
    *   **Lỗi thường gặp:** Cấu hình Connector sai, vấn đề với Schema Evolution, lỗi xử lý DLQ.

## Notes
*   Đối với Kafka Streams, cần chú ý đến việc cấu hình `Serdes` (Serializer/Deserializer) cho các kiểu dữ liệu sử dụng trong Streams API.
*   Đối với Kafka Connect, cần đảm bảo cài đặt và cấu hình đúng môi trường Connect (standalone hoặc distributed) trước khi chạy các Connector.
*   Việc tạo một sub-project mới cho Kafka Streams sẽ giữ cho codebase sạch sẽ và có tổ chức, đồng thời minh họa cách tích hợp Kafka Streams vào một dự án Java hiện có.

## Discussion
Module này sẽ giúp củng cố sự hiểu biết về hai công cụ quan trọng trong hệ sinh thái Kafka. Kafka Streams là lý tưởng cho việc xây dựng các ứng dụng xử lý luồng nội bộ, trong khi Kafka Connect là giải pháp tối ưu cho việc tích hợp dữ liệu với các hệ thống bên ngoài. Việc nắm vững cả hai sẽ cung cấp một bộ công cụ toàn diện để xây dựng các kiến trúc dựa trên Kafka.

## Next Steps
Bắt đầu triển khai Nhiệm vụ 1: Tạo một sub-project Gradle mới cho Kafka Streams.

## Current Status
Đang chờ triển khai.