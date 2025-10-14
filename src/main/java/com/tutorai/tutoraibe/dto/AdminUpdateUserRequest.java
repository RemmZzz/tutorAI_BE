package com.tutorai.tutoraibe.dto;

import com.tutorai.tutoraibe.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateUserRequest {
    private User.Role role;
    private User.Status status;
}
