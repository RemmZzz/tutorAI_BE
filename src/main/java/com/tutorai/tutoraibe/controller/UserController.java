package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.*;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Lấy danh sách user (filter theo role/status)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers(
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.Status status
    ) {
        return userService.getAllUsers(role, status);
    }

    // Lấy user theo ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Lấy hồ sơ của bản thân
    @GetMapping("/me")
    public UserResponse getMyProfile() {
        return userService.getMyProfile();
    }

    // Cập nhật hồ sơ bản thân
    @PutMapping("/me")
    public UserResponse updateMyProfile(@RequestBody UserUpdateRequest req) {
        return userService.updateMyProfile(req);
    }

    // Admin cập nhật role/status người dùng
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse updateUserByAdmin(@PathVariable Long id, @RequestBody AdminUpdateUserRequest req) {
        return userService.updateUserByAdmin(id, req);
    }
}
