package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    void deleteByUserId(Long userId);
    void deleteAllByUserId(Long userId);
}
