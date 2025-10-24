package com.tutorai.tutoraibe.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegisterRequest {

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email là bắt buộc")
    @Size(max = 160, message = "Email tối đa 160 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6–100 ký tự")
    // Nếu muốn bắt buộc độ mạnh, bỏ comment dòng dưới:
    // @Pattern(regexp="^(?=.*[A-Z])(?=.*\\d).{8,}$", message="Mật khẩu cần ≥8 ký tự, có chữ hoa và số")
    private String password;

    @NotBlank(message = "Họ tên là bắt buộc")
    @Size(min = 2, max = 160, message = "Họ tên phải từ 2–160 ký tự")
    private String fullName;

    /** ADMIN, TUTOR, STUDENT (mặc định STUDENT nếu null – xử lý ở service) */
    @Size(max = 20, message = "Role quá dài")
    private String role;
}
