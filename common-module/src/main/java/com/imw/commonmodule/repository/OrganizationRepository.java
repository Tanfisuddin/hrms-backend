package com.imw.commonmodule.repository;

import com.imw.commonmodule.dto.superadmin.OrganizationAndOwnerResponseDTO;
import com.imw.commonmodule.persistence.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("SELECT new com.imw.commonmodule.dto.superadmin.OrganizationAndOwnerResponseDTO(o.id, o.name, o.Abbreviation,o.createdAt,u.id, u.fullName,u.email ,c.phoneNumber1, (select count(u.id) from User as u where u.organization.id=o.id ) ) " +
            "FROM Organization as o LEFT JOIN User as u on o.id=u.organization.id LEFT JOIN Contact as c on c.user.id=u.id JOIN u.roles r on r.name='ROLE_ORG_OWNER' "
    )
    Page<OrganizationAndOwnerResponseDTO> findAllWithOwner(Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.superadmin.OrganizationAndOwnerResponseDTO(o.id, o.name, o.Abbreviation,o.createdAt,u.id, u.fullName,u.email ,c.phoneNumber1, (select count(u.id) from User as u where u.organization.id=o.id ) ) " +
            "FROM Organization as o LEFT JOIN User as u on o.id=u.organization.id LEFT JOIN Contact as c on c.user.id=u.id JOIN u.roles r on r.name='ROLE_ORG_OWNER' " +
            "WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :search, '%')) "
    )
    Page<OrganizationAndOwnerResponseDTO> findAllWithOwnerByOrganizationName(Pageable pageable, @Param("search") String search);
}
