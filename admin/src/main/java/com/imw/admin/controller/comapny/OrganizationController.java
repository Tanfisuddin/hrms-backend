package com.imw.admin.controller.comapny;


import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.organization.OrganizationCreateDto;
import com.imw.commonmodule.dto.auth.UserAuthResponseDto;
import com.imw.commonmodule.enums.ERole;
import com.imw.admin.payload.MessageResponse;
import com.imw.commonmodule.repository.ContactRepository;
import com.imw.commonmodule.repository.OrganizationRepository;
import com.imw.commonmodule.repository.RoleRepository;
import com.imw.commonmodule.repository.UserRepository;
import com.imw.admin.security.JwtUtils;
import com.imw.commonmodule.persistence.Contact;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.Role;
import com.imw.commonmodule.persistence.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(ApiConstants.ADMIN)
public class OrganizationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(value = "organization")
    public ResponseEntity<?> CreateOrganization (@RequestBody @Valid OrganizationCreateDto organizationCreateDto){

        if (userRepository.existsByEmail(organizationCreateDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Set<Role> roles = new HashSet<>();
        Role orgOwnerRole = roleRepository.findByName(ERole.ROLE_ORG_OWNER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(orgOwnerRole);
        Role orgAdminRole = roleRepository.findByName(ERole.ROLE_ORG_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(orgAdminRole);
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        Organization organization = new Organization();
        organization.setName(organizationCreateDto.getCompanyName());
        Organization savedOrganization = organizationRepository.save(organization);

        User user = new User(organizationCreateDto.getEmail(), encoder.encode(organizationCreateDto.getPassword()));
        user.setRoles(roles);
        user.setFullName(organizationCreateDto.getFullName());
        savedOrganization.setOrganizationOwner(user);
        user.setOrganization(savedOrganization);
        User savedUser = userRepository.save(user);

        Contact contact = new Contact();
        contact.setPhoneNumber1(organizationCreateDto.getPhoneNumber());
        contact.setUser(savedUser);
        contactRepository.save(contact);


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(organizationCreateDto.getEmail(), organizationCreateDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User registeredUser = userRepository.findByEmail(organizationCreateDto.getEmail()).get();

        UserAuthResponseDto responseBody = new UserAuthResponseDto();
        responseBody.setUser( registeredUser);
        responseBody.setToken(jwt);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
