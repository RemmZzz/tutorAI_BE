package com.tutorai.tutoraibe.dto.Request;

import lombok.Getter; import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter @Setter
public class RefreshTokenRequest {
    @NotBlank private String refreshToken;
}
