package com.tutorai.tutoraibe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_users_email", columnList = "email")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    /** Với OAuth2 vẫn set password random hash để thỏa ràng buộc not-null */
    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 160)
    private String fullName;

    @Column(length = 512)
    private String avatarUrl;

    @Column(length = 32)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private Role role = Role.STUDENT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Builder.Default
    private Status status = Status.INACTIVE;

    /** Đã xác minh email (verify link/Google) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    /** Nguồn xác thực: LOCAL (mật khẩu) hoặc GOOGLE (OAuth2) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    /** Lần đăng nhập gần nhất (set trong login thành công) */
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum Gender { MALE, FEMALE, OTHER }
    public enum Role { ADMIN, TUTOR, STUDENT }
    public enum Status { ACTIVE, INACTIVE, BANNED }
    public enum AuthProvider { LOCAL, GOOGLE }

    @PrePersist
    void prePersist() {
        if (role == null) role = Role.STUDENT;
        if (status == null) status = Status.INACTIVE;
        if (emailVerified == null) emailVerified = false;
        if (authProvider == null) authProvider = AuthProvider.LOCAL;
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
