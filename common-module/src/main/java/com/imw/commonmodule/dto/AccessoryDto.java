package com.imw.commonmodule.dto;

import com.imw.commonmodule.enums.accessories.DeviceType;
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

public class AccessoryDto {

    private Long id;

    @NotNull
    private String employeeId;

    private String employeeName;

    @NotNull
    private DeviceType deviceType;

    @NotBlank
    private String accessoryName;

    @NotBlank
    private String serialNo;
}
