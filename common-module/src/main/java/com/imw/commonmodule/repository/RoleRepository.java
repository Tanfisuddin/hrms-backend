package com.imw.commonmodule.repository;

import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);

}
