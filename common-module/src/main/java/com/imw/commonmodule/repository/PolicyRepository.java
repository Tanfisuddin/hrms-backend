package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Policy findByOrganizationId(Long organizationId);
    boolean existsByOrganizationId(Long organizationId);
}
