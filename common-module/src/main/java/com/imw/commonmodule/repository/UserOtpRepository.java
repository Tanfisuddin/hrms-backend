package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    Optional<UserOtp> findByUserIdAndOtp(Long userId, String otp);
    void deleteAllByUserId(Long userId);
}
