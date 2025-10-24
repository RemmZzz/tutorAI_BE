package com.tutorai.tutoraibe.dto.Request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {
    @Email
    @NotBlank private String email;
}
