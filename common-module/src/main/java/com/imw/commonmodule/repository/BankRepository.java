package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    Bank getByUserId(Long userId);
    void deleteByUserId(Long userId);
}
