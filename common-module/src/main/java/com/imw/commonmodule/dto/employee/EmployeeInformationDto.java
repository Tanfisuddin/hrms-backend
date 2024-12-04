package com.imw.commonmodule.dto.employee;

import com.imw.commonmodule.enums.employeeDetails.EmployeeTitle;
import com.imw.commonmodule.enums.employeeDetails.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInformationDto {

    @NotNull(message = "Title is required")
    private EmployeeTitle title;

    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "Employee Id is required")
    private String employeeId;

    private String profileImage;

    @Email(message = "A valid email is required")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "phone number is required")
    private String phone;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private Long reportingAuthorityId;

    private String reportingAuthorityName;
}
