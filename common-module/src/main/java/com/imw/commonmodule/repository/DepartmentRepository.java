package com.imw.commonmodule.repository;

import com.imw.commonmodule.dto.department.DepartmentResponseDto;
import com.imw.commonmodule.persistence.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(
            "SELECT new com.imw.commonmodule.dto.department.DepartmentResponseDto(d.id, d.name, COUNT(u.id),d.imageUrl, d.departmentHead.id) " +
                    "FROM Department d LEFT JOIN d.employees u " +
                    "WHERE d.organization.id = :organizationId " +
                    "GROUP BY d.id "
    )
    Page<DepartmentResponseDto> findAllByOrganizationId(Pageable pageable, @Param("organizationId") Long organizationId);

    Department findByIdAndOrganizationId(@Param("departmentId") Long departmentId,@Param("organizationId") Long organizationId);

    Boolean existsByIdAndOrganizationId(Long departmentId, Long organizationId);

    Boolean existsByNameAndOrganizationId(String departmentName, Long organizationId);

    @Modifying
    @Query("UPDATE Department d SET d.departmentHead.id = :newDepartmentId WHERE d.departmentHead.id = :oldDepartmentId")
    int updateAllDepartmentHeadByDepartmentHeadId(@Param("newDepartmentId") Long newDepartmentId, @Param("oldDepartmentId") Long oldDepartmentId);

}
