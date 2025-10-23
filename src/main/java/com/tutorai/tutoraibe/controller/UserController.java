package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.AdminUpdateUserRequest;
import com.tutorai.tutoraibe.dto.Request.UserUpdateRequest;
import com.tutorai.tutoraibe.dto.Response.UserResponse;
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

    //  ADMIN - Lấy danh sách user (filter theo role/status)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.Status status) {
        return ResponseEntity.ok(userService.getAllUsers(role, status));
    }

    //  ADMIN - Lấy user theo ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //  USER / ADMIN - Lấy hồ sơ bản thân
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    //  USER / ADMIN - Cập nhật hồ sơ bản thân
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(@RequestBody UserUpdateRequest req) {
        return ResponseEntity.ok(userService.updateMyProfile(req));
    }

    //  ADMIN - Cập nhật user khác
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserByAdmin(
            @PathVariable Long id,
            @RequestBody AdminUpdateUserRequest req
    ) {
        return ResponseEntity.ok(userService.updateUserByAdmin(id, req));
    }

    //  ADMIN - Xoá mềm (vô hiệu hoá)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("✅ User deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(" Failed to deactivate user: " + e.getMessage());
        }
    }

    //  ADMIN - Xoá cứng (xoá khỏi DB + xoá dữ liệu liên quan)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<String> hardDeleteUser(@PathVariable Long id) {
        try {
            userService.hardDeleteUser(id);
            return ResponseEntity.ok("✅ User permanently deleted from database");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("⚠️ Cannot delete user due to related data (foreign key constraint). Please remove related records first.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(" Failed to delete user: " + e.getMessage());
        }
    }

}
