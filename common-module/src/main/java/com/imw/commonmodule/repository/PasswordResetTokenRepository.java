package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.PasswordResetToken;
import com.imw.commonmodule.persistence.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUserIdAndResetToken(Long userId, String token);
    void deleteAllByUserId(Long userId);
}
