package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findAllByUserId(Long userId);
    Education findByEducationTypeAndUserId(String EducationType, Long userId);

    void deleteAllByUserId(Long userId);
}
