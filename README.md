
# 🎓 EduTech - Backend API Documentation

## 📘 Overview
**EduTech** là hệ thống web học tập trực tuyến với tính năng kết nối giữa **học viên – gia sư – quản trị viên**.  
Dự án được xây dựng bằng **Java Spring Boot** (Backend) và có thể kết nối với frontend ReactJS/Tailwind qua RESTful API.

---

## 🏗️ Tech Stack

| Layer | Technology |
|--------|-------------|
| **Backend Framework** | Spring Boot 3.x |
| **Database** | MySQL 8 |
| **ORM** | Spring Data JPA (Hibernate) |
| **Authentication** | JWT + Refresh Token + Google OAuth2 |
| **Email** | Spring Mail Sender (Password Reset / Verification) |
| **Build Tool** | Maven |
| **Deployment** | Render / Aiven / Docker |

---

## 📂 Project Structure
```
edutech-be/
│
├── src/main/java/com/edutech/
│   ├── controller/      # REST controllers
│   ├── entity/          # Entities (JPA)
│   ├── repository/      # Repositories
│   ├── service/         # Business logic
│   ├── dto/             # Data transfer objects
│   ├── security/        # JWT + OAuth2 config
│   └── EdutechApplication.java
│
├── src/main/resources/
│   ├── application.properties
│   └── templates/ (nếu có email template)
│
└── pom.xml
```

---

## 🔐 Authentication & Authorization
- Hỗ trợ **JWT Access Token + Refresh Token**
- Hỗ trợ **Google OAuth2 Login**
- Có phân quyền **ROLE_USER**, **ROLE_TUTOR**, **ROLE_ADMIN**
- Middleware kiểm tra quyền truy cập từng endpoint.

---

## 🚀 API Modules Overview

| Module | Endpoints | Description |
|---------|------------|--------------|
| **Auth** | `/auth/register`, `/auth/login`, `/auth/refresh` | Đăng ký, đăng nhập, xác thực, refresh token, quên mật khẩu, Google OAuth2 |
| **Users** | `/users`, `/users/me`, `/users/{id}` | Lấy & cập nhật hồ sơ, quản lý user, lọc theo role/status |
| **Tutors** | `/tutors/search`, `/tutors/{id}`, `/tutors` | Tìm kiếm gia sư, xem hồ sơ, CRUD hồ sơ, duyệt/ẩn |
| **Students** | `/students/me` | Xem & cập nhật hồ sơ học viên |
| **Subjects** | `/subjects` | Quản lý danh mục môn học (CRUD) |
| **Courses** | `/courses`, `/courses/{id}`, `/courses/{id}/lessons` | CRUD khóa học, bài học, đăng ký, tiến độ, review |
| **Enrollments** | `/courses/{id}/enroll`, `/enrollments/me` | Đăng ký khóa học, xem khóa học đã mua |
| **Schedules** | `/tutors/{id}/schedules` | Tạo/lấy lịch dạy, cập nhật trạng thái |
| **Bookings** | `/bookings`, `/bookings/{id}` | Đặt lịch học, hủy, nhận, hoàn tất buổi học |
| **Payments** | `/payments/{provider}/create`, `/callback` | Tạo thanh toán (VNPay...), xử lý callback & transaction |
| **Transactions** | `/transactions/me`, `/transactions/{id}` | Lịch sử giao dịch, chi tiết transaction |
| **Reviews** | `/tutors/{id}/reviews` | Đánh giá, xem đánh giá gia sư |
| **AI Exercises** | `/ai/exercises`, `/ai/exercises/upload` | Sinh & chấm bài tập AI, lưu kết quả |
| **AI Chat** | `/ai/sessions`, `/ai/sessions/{id}/messages` | Trò chuyện AI học tập, lưu lịch sử |
| **Notifications** | `/notifications`, `/notifications/{id}/read` | Xem danh sách & đánh dấu đã đọc |
| **Admin** | `/admin/*` | Quản trị: duyệt tutor, quản lý user, thống kê, báo cáo |
| **Blogs** | `/blogs` | Tin tức, bài viết, kiến thức học tập |
| **FAQs** | `/faqs` | Câu hỏi thường gặp |
| **Wishlist** | `/wishlist` | Thêm/xóa gia sư hoặc khóa học yêu thích |
| **Certificates** | `/certificates/me` | Sinh & tải chứng chỉ (PDF) sau khi hoàn thành khóa học |

---

## 🧠 Features Summary

### 👩‍🏫 For Tutors
- Tạo & quản lý hồ sơ giảng dạy
- Thiết lập lịch dạy, quản lý học viên
- Xem đánh giá và doanh thu

### 🎓 For Students
- Đăng ký tài khoản, cập nhật hồ sơ
- Tìm kiếm gia sư, khóa học
- Học online, đánh giá & nhận chứng chỉ

### 🛠️ For Admin
- Quản lý người dùng, gia sư, khóa học
- Thống kê giao dịch, báo cáo tài chính
- Duyệt nội dung và phản hồi người dùng

---

## ⚙️ Installation & Setup

### 1. Clone project
```bash
git clone https://github.com/RemmZzz/tutorAI_BE
cd edutech-be
```

### 2. Create database
```sql
CREATE DATABASE edutech CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure environment
Cập nhật file `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/edutech?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password
jwt.secret=your_secret_key
spring.security.oauth2.client.registration.google.client-id=GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=GOOGLE_CLIENT_SECRET
```

### 4. Run project
```bash
mvn spring-boot:run
```

Server sẽ chạy tại: [http://localhost:9000](http://localhost:9000)

---

## 🧩 Future Enhancements
- Tích hợp OpenAI API cho AI Chat
- Gợi ý khóa học cá nhân hóa
- Dashboard phân tích học tập
- Thanh toán quốc tế (PayPal, Stripe)

---

## 👨‍💻 Contributors
- **Rem** – Backend Developer
- (Thêm các thành viên khác nếu có)

---

## 📄 License
This project is licensed under the MIT License.
