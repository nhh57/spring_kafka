# Hướng dẫn cài đặt và cấu hình Kafka Connect Standalone

Kafka Connect là một framework để kết nối Kafka với các hệ thống bên ngoài như cơ sở dữ liệu, hệ thống tệp, hoặc các dịch vụ khác. Hướng dẫn này sẽ chỉ cho bạn cách thiết lập và chạy Kafka Connect ở chế độ standalone để thực hiện các ví dụ.

## 1. Tải xuống Apache Kafka

Kafka Connect được đóng gói cùng với Apache Kafka. Bạn cần tải xuống bản phân phối Kafka từ trang web chính thức của Apache Kafka.

1.  Truy cập trang tải xuống của Apache Kafka: [https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
2.  Chọn phiên bản Kafka phù hợp (ví dụ: `kafka_2.13-3.6.1.tgz` cho Scala 2.13 và Kafka 3.6.1).
3.  Tải xuống tệp `.tgz` hoặc `.zip`.

## 2. Giải nén Kafka

Sau khi tải xuống, giải nén tệp vào một thư mục bạn chọn. Ví dụ, nếu bạn giải nén vào `D:\kafka`, bạn sẽ có cấu trúc thư mục như sau:

```
D:\kafka
├── bin
├── config
├── libs
├── LICENCE
├── NOTICE
├── site-docs
└── ...
```

Trong đó:
*   `bin`: Chứa các script dòng lệnh để chạy Kafka broker, Zookeeper, Producer, Consumer, và Kafka Connect.
*   `config`: Chứa các tệp cấu hình.

## 3. Cấu hình Kafka Connect

Các tệp cấu hình cho Kafka Connect nằm trong thư mục `config` của bản phân phối Kafka. Chúng ta sẽ tập trung vào `connect-standalone.properties` cho chế độ standalone.

1.  Mở tệp `D:\kafka\config\connect-standalone.properties` bằng một trình soạn thảo văn bản.
2.  Đảm bảo các cấu hình sau trỏ đến Kafka broker của bạn (trong trường hợp này là cụm Docker Compose bạn đã khởi động):

    ```properties
    # Kafka broker to connect to
    bootstrap.servers=localhost:9092,localhost:9093

    # Where to store Kafka Connect's internal data (offsets, configs, status)
    # Make sure these directories exist and are writable
    offset.storage.file.filename=/tmp/connect.offsets
    config.storage.file.filename=/tmp/connect.configs
    status.storage.file.filename=/tmp/connect.status

    # Converters for key and value. Use StringConverter for plain text files.
    key.converter=org.apache.kafka.connect.storage.StringConverter
    value.converter=org.apache.kafka.connect.storage.StringConverter

    # Optional: If you want to use JSON converter with schema disabled for simplicity
    # value.converter=org.apache.kafka.connect.json.JsonConverter
    # value.converter.schemas.enable=false

    # Plugin path for external connectors (if any)
    # plugin.path=/usr/local/share/kafka/plugins
    ```
    *   **`bootstrap.servers`**: Quan trọng nhất, hãy đảm bảo nó trỏ đến các broker Kafka đang chạy của bạn. Nếu bạn đang sử dụng `docker-compose.yml` trong dự án này, các broker sẽ là `localhost:9092` và `localhost:9093`.
    *   **`offset.storage.file.filename`**, **`config.storage.file.filename`**, **`status.storage.file.filename`**: Đây là nơi Kafka Connect lưu trữ trạng thái nội bộ của nó. Trên Windows, bạn có thể thay đổi `/tmp/` thành một đường dẫn hợp lệ, ví dụ: `D:/kafka/connect-data/connect.offsets`. Đảm bảo các thư mục này tồn tại.
    *   **`key.converter`** và **`value.converter`**: Xác định cách dữ liệu được tuần tự hóa/giải tuần tự hóa khi di chuyển giữa Kafka và Connector. Đối với các ví dụ file, `StringConverter` là phù hợp.

## 4. Chạy Kafka Connect ở chế độ Standalone

Chế độ standalone phù hợp cho việc phát triển và thử nghiệm.

1.  Mở một cửa sổ terminal mới.
2.  Điều hướng đến thư mục gốc của bản cài đặt Kafka của bạn (ví dụ: `D:\kafka`).
3.  Chạy lệnh sau để khởi động Kafka Connect. Lệnh này cần hai đối số:
    *   Đường dẫn đến tệp cấu hình Connect (ví dụ: `config/connect-standalone.properties`).
    *   Đường dẫn đến tệp cấu hình của Connector mà bạn muốn chạy (chúng ta sẽ tạo ở các bước sau, ví dụ: `path/to/your/connector-config.properties`).

    ```bash
    # Trên Linux/macOS
    bin/connect-standalone.sh config/connect-standalone.properties path/to/your/connector-config.properties

    # Trên Windows (sử dụng PowerShell hoặc Command Prompt)
    .\bin\windows\connect-standalone.bat .\config\connect-standalone.properties .\path\to\your\connector-config.properties
    ```
    *   **Lưu ý quan trọng**: Để chạy các ví dụ tiếp theo, bạn sẽ cần tạo các tệp cấu hình Connector riêng biệt và truyền chúng vào lệnh này.

    Ví dụ: Nếu bạn có file cấu hình source connector là `my-source.properties` trong thư mục `config`, bạn sẽ chạy:
    ```bash
    .\bin\windows\connect-standalone.bat .\config\connect-standalone.properties .\config\my-source.properties
    ```

Bây giờ bạn đã sẵn sàng để chạy các Kafka Connectors.