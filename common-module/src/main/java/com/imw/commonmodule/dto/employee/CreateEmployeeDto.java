package com.imw.commonmodule.dto.employee;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {

    @NotBlank(message = "Employee Id is required")
    private String employeeId;

    @NotBlank(message = "full name is required")
    private String fullName;

    @Temporal(TemporalType.DATE)
//    @NotBlank(message = "joining Date is required")
    private Date joiningDate;

    @Email(message = "A valid email is required")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "phone number is required")
    private String phone;

    @NotBlank(message = "role is required")
    private String role;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotBlank(message = "Job Type is required")
    private String jobType;

//    @NotBlank(message = "department is required")
    private Long departmentId;

    private Long reportingAuthorityId;

}
