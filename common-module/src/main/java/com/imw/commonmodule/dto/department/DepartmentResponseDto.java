package com.imw.commonmodule.dto.department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imw.commonmodule.persistence.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {

    private Long id;

    private String name;

    private Long employeesCount;

    private String imageUrl;

    private Long departmentHeadId;
}
