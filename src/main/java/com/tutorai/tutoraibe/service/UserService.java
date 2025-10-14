package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.*;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponse> getAllUsers(User.Role role, User.Status status) {
        return userRepository.findAllByRoleAndStatus(role, status)
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse getMyProfile() {
        return UserResponse.fromEntity(getCurrentUser());
    }

    public UserResponse updateMyProfile(UserUpdateRequest req) {
        User user = getCurrentUser();
        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAvatarUrl() != null) user.setAvatarUrl(req.getAvatarUrl());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

    public UserResponse updateUserByAdmin(Long id, AdminUpdateUserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (req.getRole() != null) user.setRole(req.getRole());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }
}
