package com.imw.commonmodule.dto.superadmin;

import com.imw.commonmodule.persistence.Contact;
import com.imw.commonmodule.persistence.Organization;
import com.imw.commonmodule.persistence.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OrganizationAndOwnerResponseDTO {
    private Long id;
    private String name;
    private String abbreviation;
    private Date createdAd;

    private Long ownerId;
    private String ownerName;
    private String email;
    private String contact;

    private Long employeeCount;

}
