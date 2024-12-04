package com.imw.commonmodule.dto.attendance;

import com.imw.commonmodule.enums.attendance.CheckInCheckOutStatus;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CheckInCheckOutStatusDto {
    LocalTime checkInTime;
    LocalTime checkOutTime;
    CheckInCheckOutStatus status;
}
