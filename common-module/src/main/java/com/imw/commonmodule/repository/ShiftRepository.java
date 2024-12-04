package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findAllByOrganizationId(Long organizationId);
    Optional<Shift> findByIdAndOrganizationId(Long id, Long organizationId);
}
