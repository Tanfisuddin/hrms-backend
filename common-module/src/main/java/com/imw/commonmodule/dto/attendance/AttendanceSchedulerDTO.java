package com.imw.commonmodule.dto.attendance;

import com.imw.commonmodule.persistence.Holiday;
import com.imw.commonmodule.persistence.Shift;
import com.imw.commonmodule.persistence.User;
import lombok.Data;

@Data
public class AttendanceSchedulerDTO {
    User user ;
    Shift shift;
    Holiday holiday;

    AttendanceSchedulerDTO(User user, Shift shift, Holiday holiday){
        this.user = user;
        this.shift = shift;
        this.holiday = holiday;
    }
}
