# Hướng dẫn Cài đặt và Cấu hình Kafka Connect

Tài liệu này cung cấp hướng dẫn chi tiết về cách cài đặt và cấu hình Apache Kafka Connect ở chế độ độc lập (standalone mode) để chạy các ví dụ Connector.

## 1. Tải xuống Apache Kafka

Đầu tiên, bạn cần tải xuống Apache Kafka, trong đó đã bao gồm Kafka Connect.

1.  Truy cập trang tải xuống chính thức của Apache Kafka: [https://kafka.apache.org/downloads](https://kafka.apache.org/downloads)
2.  Chọn phiên bản Kafka ổn định mới nhất. Ví dụ: `kafka_2.13-3.6.1.tgz`
3.  Tải xuống file `tgz` (tar.gz) và giải nén nó vào một thư mục bạn muốn, ví dụ: `C:\kafka` (Windows) hoặc `/opt/kafka` (Linux/macOS).
    *   **Windows:** Sử dụng một công cụ giải nén như 7-Zip hoặc WinRAR.
    *   **Linux/macOS:** Mở terminal và chạy lệnh:
        ```bash
        tar -xzf kafka_2.13-3.6.1.tgz -C /opt/kafka --strip-components 1
        ```
        (Thay thế phiên bản Kafka và đường dẫn giải nén phù hợp)

Sau khi giải nén, cấu trúc thư mục sẽ trông như sau:
```
kafka/
├── bin/
├── config/
├── libs/
├── licenses/
└── ...
```

## 2. Cấu hình Kafka Connect ở chế độ Standalone

Kafka Connect có thể chạy ở hai chế độ: standalone (độc lập) và distributed (phân tán). Đối với các ví dụ thực hành đơn giản, chế độ standalone là đủ.

1.  **Mở file cấu hình:**
    Điều hướng đến thư mục `config` trong thư mục Kafka bạn vừa giải nén.
    Mở file `connect-standalone.properties` bằng một trình soạn thảo văn bản.

    *   **Windows:** `kafka\config\connect-standalone.properties`
    *   **Linux/macOS:** `kafka/config/connect-standalone.properties`

2.  **Chỉnh sửa các thuộc tính quan trọng:**

    *   **`bootstrap.servers`**: Đảm bảo thuộc tính này trỏ đến địa chỉ của Kafka Broker đang chạy. Trong môi trường Docker Compose của dự án này, Kafka Broker chạy trên `localhost:9092`.
        ```properties
        bootstrap.servers=localhost:9092
        ```

    *   **`key.converter` và `value.converter`**: Các thuộc tính này xác định cách dữ liệu được chuyển đổi từ định dạng byte của Kafka sang định dạng đối tượng Java và ngược lại. Đối với các ví dụ file đơn giản, chúng ta sẽ sử dụng `StringConverter`.
        ```properties
        key.converter=org.apache.kafka.connect.storage.StringConverter
        value.converter=org.apache.kafka.connect.storage.StringConverter
        ```
        Nếu bạn muốn sử dụng JSON, bạn có thể cấu hình như sau:
        ```properties
        key.converter=org.apache.kafka.connect.json.JsonConverter
        key.converter.schemas.enable=false
        value.converter=org.apache.kafka.connect.json.JsonConverter
        value.converter.schemas.enable=false
        ```
        (`schemas.enable=false` sẽ loại bỏ thông tin schema khỏi payload JSON, giúp dễ đọc hơn cho các ví dụ đơn giản).

    *   **`offset.storage.file.filename`**: Đây là nơi Kafka Connect lưu trữ offset của các Connector ở chế độ standalone. Đảm bảo đường dẫn có thể ghi được.
        ```properties
        offset.storage.file.filename=/tmp/connect.offsets # Linux/macOS
        # hoặc
        # offset.storage.file.filename=C:/tmp/connect.offsets # Windows
        ```
        (Bạn có thể tạo thư mục `tmp` nếu nó không tồn tại.)

    *   **`plugin.path`**: Nếu bạn muốn sử dụng các Connector bên ngoài (không đi kèm với Kafka), bạn sẽ cần chỉ định đường dẫn đến thư mục chứa các plugin Connector đó. Đối với các ví dụ `FileStreamSource/Sink` có sẵn, bạn có thể bỏ qua hoặc để trống.

3.  **Lưu file cấu hình.**

## 3. Tạo thư mục cấu hình Connector

Để giữ cho các file cấu hình Connector của chúng ta có tổ chức, hãy tạo một thư mục riêng trong thư mục dự án của bạn.

Trong thư mục gốc của dự án `spring_kafka`, tạo một thư mục mới có tên `kafka-connect-examples`. Bên trong `kafka-connect-examples`, tạo một thư mục con `configs`.

```
spring_kafka/
├── ...
├── kafka-connect-examples/
│   └── configs/
├── ...
```

## 4. Chạy Kafka Connect ở chế độ Standalone

Sau khi đã cấu hình `connect-standalone.properties` và tạo thư mục cho các file cấu hình Connector, bạn có thể chạy Kafka Connect.

1.  **Mở một Terminal mới.**
2.  **Điều hướng đến thư mục gốc của cài đặt Kafka của bạn.**
    *   Ví dụ: `cd C:\kafka` (Windows) hoặc `cd /opt/kafka` (Linux/macOS)
3.  **Chạy lệnh sau:**
    ```bash
    bin/connect-standalone.sh config/connect-standalone.properties [ĐƯỜNG_DẪN_ĐẾN_FILE_CẤU_HÌNH_CONNECTOR_CỦA_BẠN]
    ```
    Thay thế `[ĐƯỜNG_DẪN_ĐẾN_FILE_CẤU_HÌNH_CONNECTOR_CỦA_BẠN]` bằng đường dẫn đầy đủ đến file cấu hình Connector mà bạn sẽ tạo (ví dụ: `file-source-connector.properties`).

    Ví dụ, nếu bạn đang ở thư mục gốc của cài đặt Kafka và file cấu hình Connector của bạn nằm trong `../spring_kafka/kafka-connect-examples/configs/file-source-connector.properties`:

    *   **Windows (PowerShell):**
        ```powershell
        .\bin\windows\connect-standalone.bat .\config\connect-standalone.properties ..\spring_kafka\kafka-connect-examples\configs\file-source-connector.properties
        ```
    *   **Linux/macOS:**
        ```bash
        bin/connect-standalone.sh config/connect-standalone.properties ../spring_kafka/kafka-connect-examples/configs/file-source-connector.properties
        ```

Khi Kafka Connect khởi động thành công, bạn sẽ thấy các log cho biết Connector của bạn đã được tải và đang chạy.