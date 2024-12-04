package com.imw.commonmodule.dto.attendance;

import lombok.Data;

@Data
public class CheckInCheckOutDto {
    private String location;
    private Double latitude;
    private Double longitude;
}
