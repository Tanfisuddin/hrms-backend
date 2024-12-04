package com.imw.commonmodule.dto.employee;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class JoiningDetailsDto {

    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    @NotBlank(message = "Job Type is required")
    private String jobType;

    private Integer noticePeriod;

}
