package com.tutorai.tutoraibe.repository;

import com.tutorai.tutoraibe.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Lấy token còn hiệu lực (chưa dùng + chưa hết hạn)
    Optional<PasswordResetToken> findByTokenAndUsedFalseAndExpiryDateAfter(String token, LocalDateTime now);

    // (Giữ) lấy theo token bất kể trạng thái - dùng cho debug/log nếu cần
    Optional<PasswordResetToken> findByToken(String token);

    // Xoá tất cả token theo userId (khi reset xong hoặc đổi mật khẩu)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // Đánh dấu token đã dùng (update nhanh không cần load entity)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PasswordResetToken t SET t.used = true WHERE t.token = :token")
    int markUsed(@Param("token") String token);

    // Dọn rác token hết hạn (cron hoặc job nền)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}
