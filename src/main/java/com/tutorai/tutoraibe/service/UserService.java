package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.AdminUpdateUserRequest;
import com.tutorai.tutoraibe.dto.Request.UserUpdateRequest;
import com.tutorai.tutoraibe.dto.Response.UserResponse;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.repository.PasswordResetTokenRepository;
import com.tutorai.tutoraibe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    // Lấy user hiện tại từ JWT
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Lấy tất cả user (filter)
    public List<UserResponse> getAllUsers(User.Role role, User.Status status) {
        return userRepository.findAllByRoleAndStatus(role, status)
                .stream().map(UserResponse::fromEntity).toList();
    }

    // Lấy user theo ID
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Lấy hồ sơ bản thân
    public UserResponse getMyProfile() {
        return UserResponse.fromEntity(getCurrentUser());
    }

    // Cập nhật hồ sơ bản thân
    public UserResponse updateMyProfile(UserUpdateRequest req) {
        User user = getCurrentUser();
        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAvatarUrl() != null) user.setAvatarUrl(req.getAvatarUrl());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    // Admin cập nhật user khác
    public UserResponse updateUserByAdmin(Long id, AdminUpdateUserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (req.getRole() != null) user.setRole(req.getRole());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    // Admin xoá mềm - giữ lại trong DB
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(User.Status.INACTIVE);
        userRepository.save(user);
    }

    // Admin xoá cứng - xóa hoàn toàn khỏi DB
    public void hardDeleteUser(Long id) {
        // 1️ Xoá các token reset mật khẩu (liên kết tới user)
        passwordResetTokenRepository.deleteByUserId(id);

        // 2️ Sau đó xoá user
        userRepository.deleteById(id);
    }

}

