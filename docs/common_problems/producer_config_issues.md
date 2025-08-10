# Vấn đề: Cấu hình Producer không đúng dẫn đến mất dữ liệu hoặc trùng lặp

## Mô tả
Kafka Producer chịu trách nhiệm gửi tin nhắn đến các Kafka broker. Việc cấu hình Producer không đúng có thể dẫn đến các vấn đề nghiêm trọng như mất dữ liệu (data loss) hoặc trùng lặp tin nhắn (message duplication), ảnh hưởng đến tính toàn vẹn của dữ liệu trong hệ thống.

## Các cấu hình Producer quan trọng
1.  **`acks` (Acknowledgements):** Kiểm soát số lượng bản sao tin nhắn phải nhận được xác nhận từ leader broker trước khi Producer coi tin nhắn đã được ghi thành công.
    *   **`acks=0`:** Producer không chờ bất kỳ xác nhận nào từ broker. Tin nhắn được gửi "fire and forget".
        *   **Vấn đề:** Thông lượng cao nhất, nhưng rủi ro mất dữ liệu cao nhất (nếu leader broker gặp sự cố ngay sau khi nhận tin nhắn).
        *   **Khi nào nên dùng:** Đối với dữ liệu không quan trọng, nơi việc mất một số tin nhắn là chấp nhận được (ví dụ: log không quan trọng).
    *   **`acks=1`:** Producer chờ xác nhận từ leader broker rằng tin nhắn đã được ghi thành công.
        *   **Vấn đề:** Thông lượng thấp hơn `acks=0`. Có thể mất dữ liệu nếu leader broker gặp sự cố trước khi các follower sao chép tin nhắn.
        *   **Khi nào nên dùng:** Cân bằng giữa độ bền và hiệu suất, phù hợp với hầu hết các trường hợp sử dụng nơi mất một lượng nhỏ dữ liệu là chấp nhận được trong các tình huống lỗi hiếm gặp.
    *   **`acks=all` (hoặc `-1`):** Producer chờ xác nhận từ leader broker và tất cả các follower trong tập hợp In-Sync Replicas (ISR) rằng tin nhắn đã được ghi thành công.
        *   **Vấn đề:** Thông lượng thấp nhất, độ trễ cao nhất.
        *   **Khi nào nên dùng:** Độ bền dữ liệu cao nhất, đảm bảo tin nhắn không bị mất miễn là ít nhất một bản sao vẫn hoạt động. Bắt buộc cho các giao dịch tài chính hoặc dữ liệu quan trọng.

2.  **`retries`:** Số lần Producer sẽ thử lại gửi tin nhắn nếu có lỗi tạm thời (ví dụ: lỗi mạng, leader broker thay đổi).
    *   **Vấn đề:** Nếu không được cấu hình cẩn thận, có thể dẫn đến trùng lặp tin nhắn nếu lỗi xảy ra sau khi tin nhắn đã được ghi nhưng trước khi Producer nhận được xác nhận.
    *   **Khi nào nên dùng:** Luôn bật để tăng cường khả năng chịu lỗi. Kết hợp với `idempotence` để tránh trùng lặp.

3.  **`enable.idempotence`:** Đảm bảo tin nhắn được ghi chính xác một lần (exactly-once delivery) ngay cả khi có lỗi mạng hoặc thử lại Producer.
    *   **Mô tả:** Producer gán một ID duy nhất và số thứ tự cho mỗi tin nhắn. Broker sử dụng thông tin này để phát hiện và loại bỏ các tin nhắn trùng lặp.
    *   **Vấn đề:** Chỉ hoạt động với `acks=all` và `retries > 0`. Có thể ảnh hưởng nhẹ đến thông lượng.
    *   **Khi nào nên dùng:** Rất khuyến nghị cho các trường hợp cần đảm bảo chính xác một lần.

4.  **`max.in.flight.requests.per.connection`:** Số lượng yêu cầu gửi tin nhắn không được xác nhận tối đa mà Producer có thể gửi trên mỗi kết nối.
    *   **Vấn đề:** Nếu lớn hơn 1 và `idempotence` không được bật, có thể dẫn đến việc thay đổi thứ tự tin nhắn nếu có lỗi.
    *   **Khi nào nên dùng:** Đặt bằng 1 nếu `idempotence` không được bật và thứ tự tin nhắn là quan trọng.

## Các tình huống dẫn đến mất dữ liệu hoặc trùng lặp
*   **Mất dữ liệu:**
    *   `acks=0` và leader broker gặp sự cố.
    *   `acks=1` và leader broker gặp sự cố trước khi follower sao chép.
    *   Lỗi không thể khắc phục (ví dụ: phân vùng không có leader).
*   **Trùng lặp tin nhắn:**
    *   `retries` được bật nhưng `enable.idempotence` không được bật.
    *   Producer gửi lại tin nhắn do lỗi mạng, nhưng tin nhắn gốc thực sự đã được ghi thành công trên broker.

## Giải pháp và khuyến nghị
*   **Đối với dữ liệu quan trọng:** Luôn sử dụng `acks=all` và `enable.idempotence=true`.
*   **Luôn bật `retries`:** Kết hợp với `enable.idempotence=true` để đảm bảo an toàn.
*   **Thử nghiệm và giám sát:** Luôn kiểm tra cấu hình Producer trong môi trường thử nghiệm và giám sát các chỉ số Producer (ví dụ: `request-latency-avg`, `record-error-rate`) để phát hiện sớm các vấn đề.
*   **Hiểu rõ ngữ nghĩa phân phối:** Chọn cấu hình phù hợp với yêu cầu ngữ nghĩa phân phối của ứng dụng (at-most-once, at-least-once, exactly-once).