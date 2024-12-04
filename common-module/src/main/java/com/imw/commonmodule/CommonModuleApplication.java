package com.imw.commonmodule;

import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.Role;
import com.imw.commonmodule.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommonModuleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CommonModuleApplication.class, args);
    }

    private final RoleRepository roleRepository;

    public CommonModuleApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            Role roleUser = new Role();
            roleUser.setName(ERole.ROLE_USER);
            roleRepository.save(roleUser);
        }
        if (roleRepository.findByName(ERole.ROLE_ORG_ADMIN).isEmpty()) {
            Role roleOrgAdmin = new Role();
            roleOrgAdmin.setName(ERole.ROLE_ORG_ADMIN);
            roleRepository.save(roleOrgAdmin);
        }
        if (roleRepository.findByName(ERole.ROLE_ORG_OWNER).isEmpty()) {
            Role roleOrgOwner = new Role();
            roleOrgOwner.setName(ERole.ROLE_ORG_OWNER);
            roleRepository.save(roleOrgOwner);
        }
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            Role roleAdmin = new Role();
            roleAdmin.setName(ERole.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
        }

    }
}
