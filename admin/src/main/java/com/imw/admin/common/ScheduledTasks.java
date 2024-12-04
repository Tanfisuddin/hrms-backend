package com.imw.admin.common;

import com.imw.commonmodule.dto.attendance.AttendanceSchedulerDTO;
import com.imw.commonmodule.enums.attendance.AttendanceStatus;
import com.imw.commonmodule.persistence.Attendance;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.AttendanceRepository;
import com.imw.commonmodule.repository.HolidayRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    HolidayRepository holidayRepository;

    @Scheduled(cron = "0 59 23 * * ?")
    public void scheduledTask() {
        LocalDate today = LocalDate.now();
        List<Attendance> attendances = new ArrayList<>();
        LocalTime time = LocalTime.now();

        for(AttendanceSchedulerDTO attendance : attendanceRepository.findAllUsersByAttendanceNotPresentByCurrentDate()){
            Attendance atnd = new Attendance();
            atnd.setUser(attendance.getUser());
            atnd.setCheckInDate(today);

            if(today.getDayOfWeek() == DayOfWeek.SUNDAY){
                atnd.setStatus(AttendanceStatus.HOLIDAY);
            }else if(attendance.getHoliday()!= null){
                atnd.setStatus(AttendanceStatus.HOLIDAY);
            }else{
                atnd.setStatus(AttendanceStatus.ABSENT);
            }
            attendances.add(atnd);
        }
        attendanceRepository.saveAll(attendances);
        Duration duration = Duration.between(time, LocalTime.now());
        System.out.println(duration.getSeconds()+" "+duration.toMillis());
    }
}
