# 🎓 EduTech Backend (Spring Boot)

EduTech Backend là hệ thống API được phát triển bằng **Java Spring Boot** phục vụ cho nền tảng **EduTech** – nơi **học viên** và **gia sư** có thể tương tác, quản lý hồ sơ, khóa học, và tiến trình học tập.  
Dự án được xây dựng theo kiến trúc **RESTful API**, bảo mật bằng **JWT Authentication** và **Google OAuth2**.

---

## 🧩 Thông tin tổng quan

| Thành phần | Chi tiết |
|-------------|-----------|
| **Ngôn ngữ** | Java 17 |
| **Framework** | Spring Boot 3.x |
| **Cơ sở dữ liệu** | MySQL |
| **IDE khuyến nghị** | IntelliJ IDEA |
| **Authentication** | JWT, OAuth2 (Google) |
| **Quản lý phụ thuộc** | Maven |
| **ORM** | Spring Data JPA (Hibernate) |
| **Tài liệu API** | Swagger UI |

---

## 🧠 Chức năng hiện có

### 1️⃣ **Authentication (Auth)**
- **Endpoints:**
  - `POST /auth/register` – Đăng ký tài khoản mới
  - `POST /auth/login` – Đăng nhập hệ thống, sinh JWT token
  - `POST /auth/refresh` – Làm mới token truy cập (refresh token)
- **Mô tả:**
  - Xác thực người dùng bằng email và mật khẩu
  - Đăng nhập, đăng ký, xác thực JWT token
  - Hỗ trợ **quên mật khẩu** (qua email)
  - Đăng nhập bằng **Google OAuth2**

---

### 2️⃣ **Users**
- **Endpoints:**
  - `GET /users` – Lấy danh sách người dùng
  - `GET /users/me` – Lấy thông tin người dùng hiện tại (qua JWT)
  - `PUT /users/{id}` – Cập nhật thông tin người dùng
- **Mô tả:**
  - Quản lý thông tin cá nhân của người dùng (User/Admin)
  - Phân loại người dùng theo vai trò (**ROLE_USER**, **ROLE_ADMIN**, **ROLE_TUTOR**)
  - Lọc và tìm kiếm user theo **role**, **status**

---

### 3️⃣ **Tutors**
- **Endpoints:**
  - `GET /tutors/search` – Tìm kiếm gia sư theo bộ lọc (tên, môn học, kinh nghiệm,…)
  - `GET /tutors/{id}` – Xem chi tiết hồ sơ gia sư
  - `POST /tutors` – Tạo hoặc cập nhật hồ sơ gia sư
- **Mô tả:**
  - CRUD hồ sơ gia sư (tên, mô tả, môn dạy, giá,…)
  - Duyệt hoặc ẩn hồ sơ (admin kiểm duyệt)
  - Tìm kiếm theo từ khóa và bộ lọc thông minh

---

### 4️⃣ **Students**
- **Endpoints:**
  - `GET /students/me` – Xem hồ sơ học viên
  - `PUT /students/me` – Cập nhật thông tin học viên
- **Mô tả:**
  - Quản lý thông tin cá nhân học viên
  - Lưu mục tiêu học tập, trình độ hiện tại
  - Tự động liên kết với tài khoản người dùng (`user_id`)

---

## 🧰 Công nghệ sử dụng

| Thành phần | Công nghệ |
|-------------|------------|
| **Backend Framework** | Spring Boot |
| **Security** | Spring Security + JWT |
| **OAuth2 Login** | Google OAuth2 |
| **ORM & Database** | Spring Data JPA + MySQL |
| **Mail Service** | JavaMailSender |
| **Mapping DTO** | ModelMapper |
| **Giảm boilerplate** | Lombok |
| **Tài liệu API** | Swagger OpenAPI |

---

## 🧩 Cấu trúc thư mục

```bash
src/
 ├── main/
 │   ├── java/com/edutech/backend/
 │   │    ├── controller/       # Chứa các REST Controllers
 │   │    ├── dto/              # Data Transfer Objects
 │   │    ├── entity/           # Entity tương ứng bảng CSDL
 │   │    ├── repository/       # Interface JPA Repository
 │   │    ├── service/          # Xử lý logic nghiệp vụ
 │   │    ├── security/         # Cấu hình JWT, filter, OAuth2
 │   │    └── EduTechApplication.java
 │   └── resources/
 │        ├── application.properties
 │        ├── templates/ (nếu có)
 │        └── static/ (nếu có)
 └── test/
      └── ...                   # Unit Test

