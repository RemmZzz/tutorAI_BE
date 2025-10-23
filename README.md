# 🎓 EduTech Backend API

Dự án **EduTech** là hệ thống web học trực tuyến kết nối **học viên** và **gia sư**, hỗ trợ quản lý khóa học, bài tập, lịch học, đánh giá, thanh toán, chứng chỉ và nhiều tính năng khác.

---

## 🧩 Tổng quan
- **Ngôn ngữ:** Java 17  
- **Framework:** Spring Boot  
- **Database:** MySQL  
- **Bảo mật:** JWT Authentication, OAuth2 Google Login  
- **Cấu trúc API:** RESTful  
- **Môi trường:** Dev / Prod  

---

## 📚 Các Module Chính

| Module | Endpoints | Mô tả chức năng | Trạng thái |
|--------|------------|----------------|-------------|
| **Auth** | `/auth/register`, `/auth/login`, `/auth/refresh` | Đăng ký, đăng nhập, xác thực, refresh token, quên mật khẩu, Google OAuth2 | ✅ Done |
| **Users** | `/users`, `/users/me`, `/users/{id}` | Lấy và cập nhật hồ sơ người dùng, quản lý user, lọc theo role/status | ✅ Done |
| **Tutors** | `/tutors/search`, `/tutors/{id}`, `/tutors` | Tìm kiếm gia sư, xem hồ sơ, CRUD hồ sơ, duyệt/ẩn | ✅ Done |
| **Students** | `/students/me` | Xem và cập nhật hồ sơ học viên | ✅ Done |
| **Subjects** | `/subjects` | Quản lý danh mục môn học (CRUD) | 🕓 To Do |
| **Courses** | `/courses`, `/courses/{id}`, `/courses/{id}/lessons` | CRUD khóa học, bài học, đăng ký, tiến độ học, review | 🕓 To Do |
| **Enrollments** | `/courses/{id}/enroll`, `/enrollments/me` | Đăng ký khóa học, xem khóa học đã mua | 🕓 To Do |
| **Schedules** | `/tutors/{id}/schedules` | Tạo/xem/xóa slot dạy, cập nhật trạng thái | 🕓 To Do |
| **Bookings** | `/bookings`, `/bookings/{id}` | Đặt lịch học, xác nhận, hủy, hoàn tất buổi học | 🕓 To Do |
| **Payments** | `/payments/{provider}/create`, `/callback` | Tạo link thanh toán, xử lý callback, lưu transaction | 🕓 To Do |
| **Transactions** | `/transactions/me`, `/transactions/{id}` | Lịch sử giao dịch, chi tiết giao dịch | 🕓 To Do |
| **Reviews** | `/tutors/{id}/reviews` | Đánh giá gia sư, xem đánh giá | 🕓 To Do |
| **AI Exercises** | `/ai/exercises`, `/ai/exercises/upload` | Upload bài tập AI, sinh lời giải, lưu kết quả | 🕓 To Do |
| **AI Chat** | `/ai/sessions`, `/ai/sessions/{id}/messages` | Chat học tập với AI, lưu lịch sử hội thoại | 🕓 To Do |
| **Notifications** | `/notifications`, `/notifications/{id}/read` | Xem thông báo, đánh dấu đã đọc | 🕓 To Do |
| **Admin** | `/admin/*` | Thống kê, duyệt tutor, quản lý user, xử lý report | 🕓 To Do |
| **Blogs** | `/blogs` | Tạo bài viết chia sẻ kiến thức | 🕓 To Do |
| **FAQs** | `/faqs` | Quản lý câu hỏi thường gặp | 🕓 To Do |
| **Wishlist** | `/wishlist` | Thêm/Xóa khóa học hoặc gia sư yêu thích | 🕓 To Do |
| **Certificates** | `/certificates/me` | Sinh và tải chứng chỉ PDF khi hoàn thành khóa học | 🕓 To Do |

---

## 🚀 Hướng dẫn cài đặt

### 1. Clone dự án
```bash
git clone https://github.com/<your-org>/edutech-be.git
cd edutech-be
