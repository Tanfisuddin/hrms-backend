package com.imw.commonmodule.repository;

import com.imw.commonmodule.persistence.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Page<Holiday> findByOrganizationIdAndHolidayDateBetweenOrderByHolidayDateAsc(Long organizationId, LocalDate holidayStartDate, LocalDate holidayEndDate, Pageable pageable);
    Holiday findByIdAndOrganizationId(Long id, Long organizationId);
}
