---
title: Cấu hình các broker Kafka để bật SSL/TLS
type: task
status: planned
created: 2025-08-12T14:06:41
updated: 2025-08-12T14:18:29
id: TASK-KAFKA-M4-S2-006
priority: high
memory_types: [procedural]
dependencies: [TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS]
tags: [kafka, security, ssl, tls, broker, configuration]
---

## Mô tả

Nhiệm vụ này tập trung vào việc cấu hình các broker Kafka để bật SSL/TLS cho giao tiếp an toàn. Điều này liên quan đến việc cập nhật file `server.properties` cho mỗi broker với đường dẫn đến các keystore và truststore đã tạo, và chỉ định giao thức bảo mật. Đây là một bước quan trọng để đảm bảo tất cả giao tiếp giữa client và broker, và có thể cả giao tiếp giữa các broker, đều được mã hóa và xác thực.

## Mục tiêu

*   Sửa đổi file `server.properties` cho mỗi broker Kafka.
*   Cấu hình broker sử dụng SSL/TLS cho kết nối client.
*   Cấu hình broker sử dụng SSL/TLS cho giao tiếp giữa các broker (tùy chọn, nhưng được khuyến nghị).
*   Chỉ định vị trí và mật khẩu cho keystore và truststore của broker.
*   Cấu hình xác thực client cho TLS lẫn nhau.

## Danh sách kiểm tra

### Cấu hình

- [ ] Đối với mỗi broker Kafka, định vị file `server.properties` của nó.
    - Ghi chú: Thường được tìm thấy trong thư mục cài đặt Kafka dưới `config/`.
- [ ] **Cấu hình Listeners:**
    - [ ] Cập nhật `listeners` để bao gồm giao thức SSL/TLS, ví dụ: `listeners=PLAINTEXT://localhost:9092,SSL://localhost:9093`.
    - [ ] Cập nhật `advertised.listeners` nếu chạy trong Docker hoặc môi trường phân tán, ví dụ: `advertised.listeners=PLAINTEXT://your.host:9092,SSL://your.host:9093`.
    - Ghi chú: Định nghĩa cả listener plaintext và SSL, hoặc chỉ SSL nếu tất cả giao tiếp đều được bảo mật.
- [ ] **Cấu hình SSL Keystore:**
    - [ ] Đặt `ssl.keystore.location` thành đường dẫn của file keystore của broker (được tạo trong Nhiệm vụ 5).
    - [ ] Đặt `ssl.keystore.password` thành mật khẩu cho keystore.
    - [ ] Đặt `ssl.key.password` thành mật khẩu cho khóa riêng trong keystore (có thể giống với mật khẩu keystore).
    - Ghi chú: Các thuộc tính này cung cấp danh tính của broker cho SSL/TLS.
- [ ] **Cấu hình SSL Truststore:**
    - [ ] Đặt `ssl.truststore.location` thành đường dẫn của file truststore của broker (chứa chứng chỉ CA từ Nhiệm vụ 5).
    - [ ] Đặt `ssl.truststore.password` thành mật khẩu cho truststore.
    - Ghi chú: Điều này cho phép broker tin cậy các chứng chỉ client (đối với TLS lẫn nhau) và các chứng chỉ broker khác (đối với SSL giữa các broker).
- [ ] **Cấu hình xác thực client:**
    - [ ] Đặt `ssl.client.auth=required` cho TLS lẫn nhau.
    - Ghi chú: Điều này đảm bảo rằng các client kết nối qua SSL phải xuất trình một chứng chỉ hợp lệ được ký bởi một CA đáng tin cậy. Đặt thành `requested` hoặc `none` nếu không muốn TLS lẫn nhau.
- [ ] **Cấu hình giao tiếp giữa các broker (tùy chọn nhưng được khuyến nghị):**
    - [ ] Đặt `security.inter.broker.protocol=SSL`.
    - Ghi chú: Điều này đảm bảo rằng các broker giao tiếp với nhau bằng SSL/TLS, mã hóa lưu lượng nội bộ.
- [ ] **Khởi động lại (các) broker Kafka:**
    - [ ] Khởi động lại mỗi broker Kafka sau khi áp dụng các thay đổi cấu hình.
    - Ghi chú: Các thay đổi cấu hình yêu cầu khởi động lại để có hiệu lực.

## Tiến độ

### Cấu hình

- [ ] Cấu hình SSL/TLS của Broker đã được khởi tạo.

## Phụ thuộc

*   `TASK-KAFKA-M4-S2-005-GENERATE-SSL-CERTS`: Nhiệm vụ này dựa vào các chứng chỉ SSL/TLS, keystore và truststore đã được tạo.

## Các cân nhắc chính

*   **Thứ tự hoạt động:** Đảm bảo chứng chỉ được tạo chính xác trước khi cố gắng cấu hình broker.
*   **Quy tắc tường lửa:** Đảm bảo cổng SSL/TLS (ví dụ: 9093) được mở trong các quy tắc tường lửa giữa broker và client.
*   **Xác minh tên máy chủ:** Theo mặc định, Kafka thực hiện xác minh tên máy chủ. Đảm bảo Tên chung (CN) hoặc Tên thay thế chủ thể (SAN) trong chứng chỉ của broker khớp với tên máy chủ mà client sử dụng để kết nối. Nếu không, client có thể cần đặt `ssl.endpoint.identification.algorithm=` thành một chuỗi rỗng (không được khuyến nghị cho sản xuất).
*   **Kiểm thử:** Sau khi cấu hình, hãy thử kết nối bằng một client Kafka đơn giản (ví dụ: `kafka-console-producer.sh` với các thuộc tính SSL) để xác minh thiết lập.

## Ghi chú

*   **Docker/Kubernetes:** Nếu Kafka được triển khai trong các container, các cấu hình này thường sẽ được truyền qua các biến môi trường hoặc các file cấu hình được gắn kết.
*   **Khắc phục sự cố:** Các vấn đề phổ biến bao gồm đường dẫn file không chính xác, mật khẩu sai hoặc chứng chỉ không khớp. Kiểm tra log của broker để tìm các lỗi liên quan đến SSL/TLS.

## Thảo luận

Việc bật SSL/TLS trên các broker Kafka là một bước cơ bản để bảo mật cluster Kafka. Nó đảm bảo rằng dữ liệu đang truyền được mã hóa, bảo vệ chống nghe lén. Với TLS lẫn nhau, nó cũng cung cấp xác thực mạnh mẽ, ngăn chặn các client trái phép kết nối. Cấu hình này làm cho cluster Kafka có khả năng chống lại các mối đe dọa bảo mật khác nhau tốt hơn.

## Các bước tiếp theo

Khi các broker Kafka được cấu hình cho SSL/TLS, bước tiếp theo sẽ là cấu hình các ứng dụng client (Producer và Consumer) để kết nối an toàn bằng SSL/TLS.

## Trạng thái hiện tại

Cấu hình các broker Kafka cho SSL/TLS đã được lên kế hoạch.