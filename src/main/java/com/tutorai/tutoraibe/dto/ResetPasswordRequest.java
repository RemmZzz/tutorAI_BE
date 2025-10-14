package com.tutorai.tutoraibe.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotBlank private String token;
    @NotBlank private String newPassword;
}
