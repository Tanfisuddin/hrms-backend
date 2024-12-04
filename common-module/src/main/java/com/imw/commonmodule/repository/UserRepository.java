package com.imw.commonmodule.repository;

import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmployeeId(String employeeId);
    Optional<User> findByEmployeeIdAndOrganizationId(String employeeId, Long organizationId);
    Boolean existsByEmail(String email);
    Boolean existsByIdAndOrganizationId(Long id, Long organizationId);
    Boolean existsByEmployeeIdAndOrganizationId(String employeeId, Long organizationId);
    Page<User> findAllByOrganizationId(Long organizationId, Pageable pageable);

    // for filtering users starts.
    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and r.name=:roleName and u.designation=:designation and LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    Page<User> findAllByOrganizationIdAndDesignationContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId,@Param("designation") String designation ,@Param("search") String search,@Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and r.name=:roleName  and LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    Page<User> findAllByOrganizationIdAndFullNameContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId,@Param("search") String search,@Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and r.name=:roleName  and u.designation=:designation"
    )
    Page<User> findAllByOrganizationIdAndDesignationContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId,@Param("designation") String designation,@Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and r.name=:roleName  "
    )
    Page<User> findAllByOrganizationIdAndRolesName(@Param("organizationId") Long organizationId,@Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and u.department.id=:departmentId and r.name=:roleName and u.designation=:designation and LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    Page<User> findAllByOrganizationIdAndDepartmentIdAndDesignationContainingIgnoreCaseAndFullNameContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId, @Param("departmentId") Long departmentId, @Param("designation") String designation ,@Param("search") String search,@Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and u.department.id=:departmentId and r.name=:roleName and LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    Page<User> findAllByOrganizationIdAndDepartmentIdAndFullNameContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId, @Param("departmentId") Long departmentId, @Param("search") String search, @Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and u.department.id=:departmentId and r.name=:roleName and u.designation=:designation "
    )
    Page<User> findAllByOrganizationIdAndDepartmentIdAndDesignationContainingIgnoreCaseAndRolesName(@Param("organizationId") Long organizationId, @Param("departmentId") Long departmentId, @Param("designation") String designation, @Param("roleName") ERole roleName, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id JOIN u.roles r " +
            "WHERE u.organization.id =:organizationId and u.department.id=:departmentId and r.name=:roleName "
    )
    Page<User> findAllByOrganizationIdAndDepartmentIdAndRolesName(@Param("organizationId") Long organizationId, @Param("departmentId") Long departmentId, @Param("roleName") ERole roleName, Pageable pageable);
    // for filtering users ends.

    List<User> findAllByShiftId(Long shiftId);
    List<User> findAllByOrganizationIdAndIdIn(Long organizationId, List<Long> userIds);


    @Query("SELECT u FROM User as u LEFT JOIN FETCH u.shift WHERE u.id = ?1")
    User findShiftByUserId(Long userId);

    // for filtering users and return Ids array "start".

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.department.id=?2 AND u.designation=?3 AND u.fullName LIKE %?4%")
    List<Long> findUserIdsByOrganizationIdAndDepartmentIdAndDesignationAndSearch(Long organizationId, Long departmentId, String designation, String search);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.department.id=?2 AND u.fullName LIKE %?3%")
    List<Long> findUserIdsByOrganizationIdAndDepartmentIdAndSearch(Long organizationId, Long departmentId, String search);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.department.id=?2 AND u.designation=?3")
    List<Long> findUserIdsByOrganizationIdAndDepartmentIdAndDesignation(Long organizationId, Long departmentId, String designation);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.department.id=?2")
    List<Long> findUserIdsByOrganizationIdAndDepartmentId(Long organizationId, Long departmentId);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.designation=?2 AND u.fullName LIKE %?3%")
    List<Long> findUserIdsByOrganizationIdAndDesignationAndSearch(Long organizationId, String designation, String search);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.fullName LIKE %?2%")
    List<Long> findUserIdsByOrganizationIdAndSearch(Long organizationId, String search);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1 AND u.designation=?2")
    List<Long> findUserIdsByOrganizationIdAndDesignation(Long organizationId, String designation);

    @Query("SELECT u.id FROM User u WHERE u.organization.id = ?1")
    List<Long> findUserIdsByOrganizationId(Long organizationId);

    // for filtering users and return Ids array "end".

    @Query("SELECT new com.imw.commonmodule.dto.UserResponseDTO(u.id, u.email, u.employeeId, u.fullName, u.profileImage, u.designation, u.jobType, u.jobRole, o.name, o.id, d.name, d.id)  " +
            "FROM User as u LEFT JOIN Department as d ON u.department.id=d.id LEFT JOIN Organization as o ON u.organization.id=o.id " +
            "WHERE u.shift.id =:shiftId "
    )
    Page<User> findAllByShiftId(@Param("shiftId")  Long shiftId, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.reportingAuthority.id = :newReportingHeadId WHERE u.reportingAuthority.id = :oldReportingHeadId")
    int updateAllUserReportingAuthorityByUserReportingAuthorityId(@Param("newReportingHeadId") Long newReportingHeadId, @Param("oldReportingHeadId") Long oldReportingHeadId);
}
