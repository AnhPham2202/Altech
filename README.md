## ⚙️ Yêu Cầu Môi Trường
- **Java**: 17+
- **Maven**: 3.9.9+

## 🐳 Hướng Dẫn Build Bằng Docker
```bash
docker build -t altech .
docker run -p 8080:8080 altech
```

## ![Maven](https://img.shields.io/badge/Maven-3.9.9-blue?logo=apachemaven&logoColor=white) Chạy Unit test
```bash
mvn test
```

## ⚠️ BAD PRACTICE ALERT
Bài tập này được thực hiện trong bối cảnh công việc tại công ty cũng như chuẩn bị cho các buổi phỏng vấn khác.  
Do thời gian và phạm vi có hạn nên một số phần vẫn còn hạn chế. Tuy nhiên, em đã dành nhiều effort cho phần **thiết kế**.

### Các điểm sẽ bổ sung nếu có thêm thời gian
- Bổ sung chi tiết cho **Response DTO**
- Thực hiện **validate Body DTO**
- Một số logic validate có thể tách thành **custom annotation** để dễ tái sử dụng
- **Unit test** còn ít case, cần mở rộng thêm
- Bổ sung **validate & phân quyền** (user / admin) thông qua **Spring Security** cho các api cần (liệt kê product)

---

## 🙏 Lời Cảm Ơn
- Tuy bài hơi dài nhưng em đánh giá cao phần câu hỏi thiết kế, đặc biệt là **deal** có thể mở rộng trong tương lai. Em cám ơn quý công ty đã cho cơ hội để suy nghĩ và thiết kế một bài take home có idea em nghĩ là khá hay
- Cảm ơn hai “người bạn” thân thiết: **Google** và **ChatGPT**, đã giúp em generate boilerplate code và tham khảo nhiều vấn đề khác.

---

## 🏗️ Thiết Kế Deal

Ngoài phần **deal**, các chức năng khác khá cơ bản nên không trình bày sâu.

Một **deal** gồm:
- Thông tin ngày/tháng/năm
- **dealCondition**: điều kiện áp dụng deal
- **dealAction**: hành động mà deal áp dụng lên sản phẩm

### Cấu trúc dealAction
Để mở rộng mà không cần thêm cột mới cho các deal khác nhau, **dealAction** có 4 cột chính:
- `target_product_id`: sản phẩm mục tiêu (giảm giá, tặng kèm, …)
- `vl`: giá trị (bao nhiêu)
- `un`: đơn vị (%, sản phẩm, tiền tệ, …)
- `actionType`: loại hành động (giảm giá, tặng kèm, …)

---

## 📌 Tổng Quan API

### Basket
- Thêm mới một basket trống
- Tính toán giá trị basket + deal
- Update basket

### Deal
- Thêm một deal (bao gồm condition + action cho danh sách product)
- Update deal

### Product
- Thêm product
- Xóa product
- Liệt kê (phân trang, filter)

---

## 📂 Tổ Chức Code

Sử dụng mô hình **MVC** với các package chính:

- `controller` → Xử lý request/response
- `service` → Chứa logic nghiệp vụ
- `repository` → Giao tiếp với DB

Các package bổ trợ:
- `config` → config (ExceptionHandler, Security, phân quyền, …)
- `constants` → hằng số, enum
- `dto` → các DTO phục vụ cho MVC
- `entity` → entity mapping với DB
- `mapper` → mapper & projection (mapping DTO, projection JPA)

---
