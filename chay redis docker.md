# Hướng dẫn chạy Redis bằng Docker và kết nối redis-cli
1. Chuẩn bị
   Cài đặt Docker trên máy bạn.

Kiểm tra Docker đã chạy bằng lệnh:

```bash
docker --version
```

Cài đặt Redis image chính thức trên Docker Hub.

2. Chạy Redis server bằng Docker
   Chạy container Redis với cổng mặc định 6379 mở ra ngoài (host):


```bash
docker run -d -p 6379:6379 --name redis-server redis
```
Giải thích:

docker run: tạo và chạy một container mới

- -d: chạy container dưới chế độ nền (detached)

- -p 6379:6379: ánh xạ cổng 6379 của container ra cổng 6379 của máy host

- --name redis-server: đặt tên cho container là redis-server

redis: tên image Redis chính thức từ Docker Hub

3. Kiểm tra container Redis đã chạy
   Dùng lệnh:

```bash
docker ps
```
Bạn sẽ thấy container redis-server đang chạy.

4. Kết nối vào Redis server bằng redis-cli
   Có 2 cách để kết nối:

Cách 1: Chạy redis-cli trong container


```bash
docker exec -it redis-server redis-cli
```
- docker exec -it: chạy lệnh trong container đang chạy, ở chế độ tương tác

- redis-server: tên container

- redis-cli: công cụ command-line của Redis để kết nối

Sau khi vào được redis-cli, bạn sẽ thấy dòng lệnh:


```makefile
127.0.0.1:6379>
```

Bạn có thể nhập lệnh Redis để thao tác, ví dụ:


```bash
SET key "Hello Redis"
GET key
```
Cách 2: Kết nối redis-cli trên máy host (nếu đã cài redis-cli)
Nếu bạn đã cài Redis client trên máy host, chỉ cần chạy:


```bash
redis-cli -h 127.0.0.1 -p 6379
```
Sau đó thao tác như bình thường.

5. Tắt và xóa container Redis
   Dừng container:


```bash
docker stop redis-server
```

Xóa container (nếu cần):

```bash
docker rm redis-server
```
Tổng kết
# 1. Chạy Redis server trên Docker (port 6379 mở ra host)

```bash
docker run -d -p 6379:6379 --name redis-server redis
```

# 2. Vào redis-cli trong container


```bash
docker exec -it redis-server redis-cli
```

# hoặc kết nối từ máy host nếu đã cài redis-cli
```bash
redis-cli -h 127.0.0.1 -p 6379
```

# 3. Dừng và xóa container khi không dùng nữa

```bash
docker stop redis-server
docker rm redis-server

```