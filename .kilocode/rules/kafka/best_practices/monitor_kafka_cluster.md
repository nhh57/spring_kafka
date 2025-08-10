# Điều cần tránh: Bỏ qua việc giám sát và cảnh báo cho cụm Kafka

## Mô tả
Giám sát và cảnh báo là yếu tố sống còn đối với bất kỳ hệ thống phân tán nào, đặc biệt là Kafka. Bỏ qua việc thiết lập một hệ thống giám sát và cảnh báo mạnh mẽ cho cụm Kafka có thể dẫn đến việc không phát hiện được các vấn đề tiềm ẩn, sự cố hiệu suất hoặc lỗi hệ thống, gây ra thời gian chết (downtime), mất dữ liệu hoặc ảnh hưởng nghiêm trọng đến các ứng dụng phụ thuộc.

## Tại sao cần giám sát Kafka?
*   **Phát hiện sự cố sớm:** Nhanh chóng xác định các vấn đề như broker bị lỗi, phân vùng không có leader, consumer lag cao, hoặc lỗi Producer/Consumer.
*   **Đảm bảo hiệu suất:** Theo dõi thông lượng, độ trễ, và việc sử dụng tài nguyên để đảm bảo cụm hoạt động tối ưu và đáp ứng các SLA (Service Level Agreement).
*   **Lập kế hoạch dung lượng:** Dựa trên các chỉ số lịch sử để dự đoán nhu cầu tài nguyên trong tương lai và mở rộng cụm kịp thời.
*   **Gỡ lỗi hiệu quả:** Dữ liệu giám sát cung cấp ngữ cảnh quan trọng để gỡ lỗi các vấn đề phức tạp.
*   **Tuân thủ SLA:** Đảm bảo rằng hệ thống đang đáp ứng các yêu cầu về tính sẵn sàng và hiệu suất.

## Các chỉ số quan trọng cần giám sát
### Chỉ số Broker
*   **CPU, Memory, Disk I/O, Network I/O:** Sức khỏe tổng thể của máy chủ.
*   **Number of under-replicated partitions:** Số lượng phân vùng có ít bản sao hơn replication factor. Đây là một chỉ số quan trọng cho thấy rủi ro mất dữ liệu hoặc giảm độ bền.
*   **Number of offline partitions:** Số lượng phân vùng không có leader.
*   **Active controller count:** Số lượng controller đang hoạt động (chỉ nên là 1).
*   **Message in/out rate:** Thông lượng tin nhắn vào/ra của broker.
*   **Request latency and rate:** Độ trễ và tỷ lệ yêu cầu của các hoạt động Producer/Consumer.
*   **Disk usage:** Mức độ sử dụng đĩa của log segments.

### Chỉ số Producer
*   **Record-send-rate, byte-send-rate:** Tốc độ gửi tin nhắn và dữ liệu.
*   **Request-latency-avg, request-latency-max:** Độ trễ trung bình và tối đa của các yêu cầu gửi tin nhắn.
*   **Record-error-rate:** Tỷ lệ tin nhắn gửi thất bại.
*   **Compression-rate:** Tỷ lệ nén dữ liệu.

### Chỉ số Consumer
*   **Consumer lag:** Sự chênh lệch giữa offset cuối cùng của phân vùng và offset đã commit của consumer group. Lag cao cho thấy consumer không thể theo kịp tốc độ của Producer.
*   **Bytes-consumed-rate, records-consumed-rate:** Tốc độ tiêu thụ dữ liệu và tin nhắn.
*   **Commit-rate:** Tỷ lệ commit offset.
*   **Rebalance-rate:** Tỷ lệ tái cân bằng nhóm consumer.

### Chỉ số Zookeeper/KRaft (Quản lý siêu dữ liệu)
*   **Number of alive brokers:** Số lượng broker đang hoạt động.
*   **Number of leaders:** Số lượng leader của phân vùng.
*   **Latency:** Độ trễ của các hoạt động Zookeeper/KRaft.

## Công cụ giám sát phổ biến
*   **JMX (Java Management Extensions):** Kafka xuất các chỉ số thông qua JMX.
*   **Prometheus & Grafana:** Bộ đôi phổ biến để thu thập, lưu trữ và trực quan hóa các chỉ số. Prometheus thu thập chỉ số JMX từ Kafka, và Grafana tạo dashboard để hiển thị.
*   **Confluent Control Center:** Cung cấp giao diện người dùng để giám sát và quản lý cụm Kafka của Confluent.
*   **Datadog, New Relic, Splunk:** Các nền tảng giám sát thương mại cung cấp tích hợp Kafka.

## Chiến lược cảnh báo
*   **Ngưỡng (Threshold-based alerts):** Cảnh báo khi một chỉ số vượt quá ngưỡng định trước (ví dụ: consumer lag > X giây, under-replicated partitions > 0).
*   **Đường cơ sở (Baseline-based alerts):** Cảnh báo khi một chỉ số lệch đáng kể so với hành vi bình thường.
*   **Tích hợp:** Gửi cảnh báo đến các kênh phù hợp (Slack, PagerDuty, email) để đội ngũ vận hành có thể phản ứng kịp thời.

## Khuyến nghị
*   **Thiết lập giám sát ngay từ đầu:** Giám sát là một phần không thể thiếu của việc triển khai Kafka sản xuất.
*   **Tự động hóa cảnh báo:** Đừng dựa vào việc kiểm tra thủ công. Tự động hóa cảnh báo cho các chỉ số quan trọng.
*   **Thường xuyên xem xét dashboard:** Đội ngũ vận hành nên thường xuyên xem xét các dashboard giám sát để nắm bắt tình hình hoạt động của cụm.
*   **Thực hành khắc phục sự cố:** Chuẩn bị các quy trình và công cụ để khắc phục sự cố khi cảnh báo được kích hoạt.