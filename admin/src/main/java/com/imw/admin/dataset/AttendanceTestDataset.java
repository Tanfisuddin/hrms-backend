package com.imw.admin.dataset;

import com.imw.admin.services.AttendanceService;
import com.imw.commonmodule.persistence.Attendance;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.AttendanceRepository;
import com.imw.commonmodule.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class AttendanceTestDataset implements CommandLineRunner {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
//        generateAttendance();
    }

    @Transactional
    public void generateAttendance() {
        System.out.println("Creating Attendance Test Dataset for employees");
        List<User> users = userRepository.findAll();

        LocalTime startTimeSession1 = LocalTime.of(7, 0);
        LocalTime endTimeSession1 = LocalTime.of(14, 0);

        LocalTime startTimeSession2 = LocalTime.of(14, 0);
        LocalTime endTimeSession2 = LocalTime.of(20, 0);

        for (User user : users) {
            LocalDate tempDate = LocalDate.parse("2000-01-01");
            LocalDate endDate = LocalDate.parse("3000-12-31");

            List<Attendance> attendances = new ArrayList<>();

            System.out.println("Creating attendance records for user "+user.getId());
            while(tempDate.isBefore(endDate)){
                Attendance attendance = new Attendance();
                attendance.setUser(user);
                attendance.setCheckInDate(tempDate);
                attendance.setCheckInTime(getRandomTimeBetween(startTimeSession1, endTimeSession1));
                attendance.setCheckOutTime(getRandomTimeBetween(startTimeSession2, endTimeSession2));
                attendances.add(attendance);

                tempDate = tempDate.plusDays(1);
            }
            System.out.println("saving user attendance");
            attendanceRepository.saveAll(attendances);
            System.out.println("Saved "+attendances.size()+" attendances for user "+user.getId());
        }
    }
    private static LocalTime getRandomTimeBetween(LocalTime startTime, LocalTime endTime) {
        int startSecond = startTime.toSecondOfDay();
        int endSecond = endTime.toSecondOfDay();

        int randomSecond = ThreadLocalRandom.current().nextInt(startSecond, endSecond + 1);
        return LocalTime.ofSecondOfDay(randomSecond);
    }

}
