# Vấn đề: Hiểu nhầm Kafka là Message Queue truyền thống

## Mô tả
Một trong những sai lầm phổ biến nhất khi bắt đầu với Kafka là xem nó như một hệ thống hàng đợi tin nhắn (Message Queue - MQ) truyền thống như RabbitMQ, ActiveMQ. Mặc dù Kafka có thể được sử dụng cho các trường hợp sử dụng giống MQ, nhưng kiến trúc và các thuộc tính cốt lõi của nó lại khác biệt đáng kể.

## Sự khác biệt chính
1.  **Mô hình Consumer:**
    *   **MQ truyền thống:** Thường theo mô hình point-to-point (hàng đợi) hoặc publish-subscribe (topic). Trong mô hình hàng đợi, mỗi tin nhắn chỉ được một consumer duy nhất xử lý và sau đó bị xóa khỏi hàng đợi.
    *   **Kafka:** Theo mô hình log phân tán. Tin nhắn không bị xóa sau khi được tiêu thụ. Thay vào đó, chúng được lưu giữ trong một khoảng thời gian nhất định (retention period). Nhiều nhóm consumer có thể đọc cùng một tin nhắn mà không ảnh hưởng lẫn nhau.

2.  **Khả năng lưu trữ (Durability):**
    *   **MQ truyền thống:** Tin nhắn thường được lưu trữ trong bộ nhớ hoặc trên đĩa cho đến khi được tiêu thụ.
    *   **Kafka:** Được thiết kế để lưu trữ bền vững các luồng dữ liệu trên đĩa. Dữ liệu được ghi vào các phân vùng (partitions) và được sao chép (replicated) trên nhiều broker, đảm bảo khả năng chịu lỗi cao và không mất dữ liệu.

3.  **Thứ tự tin nhắn (Message Ordering):**
    *   **MQ truyền thống:** Thứ tự tin nhắn thường được đảm bảo trong một hàng đợi duy nhất.
    *   **Kafka:** Thứ tự tin nhắn được đảm bảo trong phạm vi một phân vùng. Đối với một topic có nhiều phân vùng, thứ tự tổng thể không được đảm bảo trừ khi bạn chỉ có một phân vùng hoặc sử dụng khóa (key) để đảm bảo các tin nhắn liên quan đến cùng một phân vùng.

4.  **Khả năng mở rộng (Scalability):**
    *   **MQ truyền thống:** Khả năng mở rộng thường bị hạn chế bởi một server hoặc cụm server.
    *   **Kafka:** Được thiết kế để mở rộng theo chiều ngang. Bạn có thể thêm nhiều broker và phân vùng để tăng thông lượng và khả năng lưu trữ.

## Hậu quả của việc hiểu nhầm
Việc hiểu nhầm Kafka là một MQ truyền thống có thể dẫn đến:
*   **Thiết kế hệ thống không tối ưu:** Áp dụng các mẫu thiết kế của MQ truyền thống vào Kafka có thể không tận dụng được hết các lợi thế của Kafka (ví dụ: khả năng phát lại dữ liệu, stream processing).
*   **Vấn đề hiệu suất:** Cố gắng bắt chước hành vi của MQ truyền thống (ví dụ: xóa tin nhắn sau khi đọc) có thể gây ra gánh nặng không cần thiết cho Kafka.
*   **Khó khăn trong gỡ lỗi và vận hành:** Không hiểu rõ các khái niệm như offset, consumer group, retention policy có thể khiến việc gỡ lỗi và quản lý hệ thống trở nên phức tạp.

## Cách tiếp cận đúng đắn
Hãy xem Kafka như một **nền tảng sự kiện phân tán** hoặc một **distributed commit log**. Nó không chỉ là nơi để gửi và nhận tin nhắn, mà còn là một kho lưu trữ bền vững cho các sự kiện, cho phép nhiều ứng dụng đọc và xử lý lại dữ liệu theo nhiều cách khác nhau.

Việc hiểu đúng bản chất của Kafka sẽ giúp bạn thiết kế các hệ thống mạnh mẽ, có khả năng mở rộng và hiệu quả hơn.