package com.imw.commonmodule.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationCreateDto {

    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @NotBlank(message = "company name is required")
    private String companyName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

}
