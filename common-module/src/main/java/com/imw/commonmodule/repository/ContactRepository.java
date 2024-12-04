package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Contact getByUserId(Long id);
    void deleteByUserId(Long userId);
}
