package com.imw.commonmodule.dto.attendance;

import com.imw.commonmodule.enums.attendance.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// used in attendance repo string query.
public class AttendanceResponseDto {

    private Long id;

    private String checkInLocation;
    private Double checkInLatitude;
    private Double checkInLongitude;

    private String checkOutLocation;
    private Double checkOutLatitude;
    private Double checkOutLongitude;

    private Boolean isRegularized;

    private LocalDate checkInDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private AttendanceStatus status;

    private String employeeName;
    private String employeeId;
}
