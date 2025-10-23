package com.tutorai.tutoraibe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;
    private String phone;
    private String avatarUrl;
}
