package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.*;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ADMIN - Lấy danh sách user (filter theo role/status)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers(
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.Status status) {
        return userService.getAllUsers(role, status);
    }

    // ADMIN - Lấy user theo ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Tất cả user - Lấy hồ sơ bản thân
    @GetMapping("/me")
    public UserResponse getMyProfile() {
        return userService.getMyProfile();
    }

    // Tất cả user - Cập nhật hồ sơ bản thân
    @PutMapping("/me")
    public UserResponse updateMyProfile(@RequestBody UserUpdateRequest req) {
        return userService.updateMyProfile(req);
    }

    // ADMIN - Cập nhật user khác
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public UserResponse updateUserByAdmin(@PathVariable Long id, @RequestBody AdminUpdateUserRequest req) {
        return userService.updateUserByAdmin(id, req);
    }

    // ADMIN - Xoá user
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deactivated successfully");
    }
}

