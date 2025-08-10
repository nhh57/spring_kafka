# Hướng dẫn cài đặt Docker và Docker Compose

Tài liệu này cung cấp hướng dẫn chi tiết về cách cài đặt Docker và Docker Compose trên các hệ điều hành phổ biến. Docker và Docker Compose là các công cụ cần thiết để thiết lập môi trường Kafka cục bộ cho mục đích học tập và phát triển.

## 1. Cài đặt Docker Desktop (Windows và macOS)

Docker Desktop là ứng dụng dễ sử dụng để xây dựng và chia sẻ các ứng dụng được container hóa trên Windows và macOS. Nó bao gồm Docker Engine, Docker CLI client, Docker Compose, Kubernetes và Credential Helper.

### 1.1. Cài đặt trên Windows

Nếu bạn đang sử dụng Windows, bạn nên cài đặt Docker Desktop với WSL 2 (Windows Subsystem for Linux 2) để có hiệu suất tốt nhất.

1.  **Kiểm tra yêu cầu hệ thống**: Đảm bảo hệ thống của bạn đáp ứng các yêu cầu tối thiểu của Docker Desktop (Windows 10 64-bit: Pro 2004 trở lên hoặc Enterprise/Education 1909 trở lên).
2.  **Kích hoạt WSL 2**:
    *   Mở PowerShell với quyền Administrator và chạy:
        ```powershell
        dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
        dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
        ```
    *   Khởi động lại máy tính khi được yêu cầu.
    *   Đặt WSL 2 làm phiên bản mặc định:
        ```powershell
        wsl --set-default-version 2
        ```
    *   Cài đặt bản phân phối Linux (ví dụ: Ubuntu) từ Microsoft Store.
3.  **Tải xuống và cài đặt Docker Desktop**:
    *   Truy cập trang tải xuống chính thức của Docker Desktop: [https://docs.docker.com/desktop/install/windows-install/](https://docs.docker.com/desktop/install/windows-install/)
    *   Tải xuống trình cài đặt `Docker Desktop Installer.exe`.
    *   Chạy trình cài đặt và làm theo hướng dẫn. Đảm bảo rằng tùy chọn "Use WSL 2 instead of Hyper-V" được chọn trong quá trình cài đặt.
4.  **Khởi động Docker Desktop**: Sau khi cài đặt hoàn tất, khởi động Docker Desktop từ Start Menu. Docker Desktop sẽ tự động khởi động và chạy trong nền. Bạn sẽ thấy biểu tượng Docker trên thanh tác vụ.
5.  **Kiểm tra cài đặt**: Mở Command Prompt hoặc PowerShell và chạy các lệnh sau để xác minh Docker đã được cài đặt đúng cách:
    ```bash
    docker --version
    docker compose version
    ```

### 1.2. Cài đặt trên macOS

1.  **Kiểm tra yêu cầu hệ thống**: Đảm bảo hệ thống của bạn đáp ứng các yêu cầu tối thiểu của Docker Desktop.
2.  **Tải xuống và cài đặt Docker Desktop**:
    *   Truy cập trang tải xuống chính thức của Docker Desktop: [https://docs.docker.com/desktop/install/mac-install/](https://docs.docker.com/desktop/install/mac-install/)
    *   Tải xuống trình cài đặt phù hợp với chip của bạn (Intel hoặc Apple chip).
    *   Mở tệp `.dmg` đã tải xuống và kéo biểu tượng Docker vào thư mục Applications.
    *   Mở Docker Desktop từ thư mục Applications.
3.  **Khởi động Docker Desktop**: Docker Desktop sẽ yêu cầu quyền truy cập vào các tệp hệ thống cần thiết. Cấp quyền và chờ Docker Desktop khởi động hoàn tất.
4.  **Kiểm tra cài đặt**: Mở Terminal và chạy các lệnh sau để xác minh Docker đã được cài đặt đúng cách:
    ```bash
    docker --version
    docker compose version
    ```

## 2. Cài đặt Docker Engine và Docker Compose (Linux)

Đối với người dùng Linux, bạn có thể cài đặt Docker Engine và Docker Compose Plugin riêng biệt.

### 2.1. Cài đặt Docker Engine

Thực hiện theo hướng dẫn cài đặt chính thức của Docker cho bản phân phối Linux của bạn: [https://docs.docker.com/engine/install/](https://docs.docker.com/engine/install/)

Ví dụ cho Ubuntu:
1.  **Cập nhật gói**:
    ```bash
    sudo apt-get update
    sudo apt-get install ca-certificates curl gnupg lsb-release
    ```
2.  **Thêm GPG key của Docker**:
    ```bash
    sudo mkdir -p /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    ```
3.  **Thiết lập kho lưu trữ**:
    ```bash
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    ```
4.  **Cài đặt Docker Engine**:
    ```bash
    sudo apt-get update
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
    ```
5.  **Thêm người dùng vào nhóm `docker` (tùy chọn nhưng được khuyến nghị)**: Điều này cho phép bạn chạy lệnh Docker mà không cần `sudo`.
    ```bash
    sudo usermod -aG docker $USER
    newgrp docker # Hoặc đăng xuất/đăng nhập lại để áp dụng thay đổi
    ```
6.  **Kiểm tra cài đặt**:
    ```bash
    docker run hello-world
    ```

### 2.2. Cài đặt Docker Compose Plugin

Nếu bạn đã cài đặt Docker Engine theo hướng dẫn trên, Docker Compose Plugin thường đã được cài đặt cùng. Bạn có thể xác minh bằng lệnh:

```bash
docker compose version
```

Nếu nó không được cài đặt, bạn có thể cài đặt nó theo hướng dẫn chính thức: [https://docs.docker.com/compose/install/](https://docs.docker.com/compose/install/)

## 3. Xác minh cài đặt

Sau khi cài đặt xong, luôn kiểm tra lại các phiên bản để đảm bảo mọi thứ hoạt động đúng:

```bash
docker --version
docker compose version
```

Nếu bạn gặp bất kỳ vấn đề nào trong quá trình cài đặt, hãy tham khảo tài liệu chính thức của Docker hoặc tìm kiếm giải pháp trên các diễn đàn cộng đồng.