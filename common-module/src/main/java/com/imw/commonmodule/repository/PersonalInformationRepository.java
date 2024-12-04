package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, Long> {
    PersonalInformation findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
