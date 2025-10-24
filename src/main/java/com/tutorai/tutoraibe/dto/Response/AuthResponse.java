package com.tutorai.tutoraibe.dto.Response;

import lombok.*;

@Builder @Getter @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
}
