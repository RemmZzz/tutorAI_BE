package com.tutorai.tutoraibe.repository;

import com.tutorai.tutoraibe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE (:role IS NULL OR u.role = :role) AND (:status IS NULL OR u.status = :status)")
    List<User> findAllByRoleAndStatus(@Param("role") User.Role role, @Param("status") User.Status status);
}
