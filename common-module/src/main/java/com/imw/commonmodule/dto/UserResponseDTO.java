package com.imw.commonmodule.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    Long id;
    String email;
    String employeeId;
    String fullName;
    String profileImage;
    String designation;
    String jobType;
    String jobRole;
    String organizationName;
    Long organizationId;
    String departmentName;
    Long departmentId;
}
