package com.imw.commonmodule.repository;

import com.imw.commonmodule.dto.attendance.AttendanceSchedulerDTO;
import com.imw.commonmodule.persistence.Attendance;
import com.imw.commonmodule.persistence.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByCheckInDateAndUserId(LocalDate checkInDate, Long userId);
    Page<Attendance>  findByUserIdAndCheckInDateBetween(Long userId, LocalDate checkInStartDate,LocalDate checkInEndDate , Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.attendance.AttendanceResponseDto(a.id, a.checkInLocation, a.checkInLatitude, a.checkInLongitude, a.checkOutLocation, a.checkOutLatitude, a.checkOutLongitude, a.isRegularized, a.checkInDate, a.checkInTime, a.checkOutTime, a.status, u.fullName, u.employeeId)  " +
            "FROM Attendance as a LEFT JOIN User as u ON a.user.id=u.id " +
            "WHERE a.checkInDate =:checkInDate and a.user.id IN :userIds"
    )
    Page<Attendance>  findByCheckInDateAndUserIdIn(@Param("checkInDate") LocalDate checkInDate,@Param("userIds") List<Long> userIds, Pageable pageable);

    @Query("SELECT new com.imw.commonmodule.dto.attendance.AttendanceSchedulerDTO(u, s, h)  " +
            "FROM User as u LEFT JOIN Attendance as a ON u.id=a.user.id and a.checkInDate=CURDATE() " +
            "LEFT JOIN Shift  as s on u.shift.id=s.id "+
            "LEFT JOIN Holiday  as h on h.organization.id=u.organization.id and h.holidayDate=CURDATE() "+
            "where a is null "
    )
    List<AttendanceSchedulerDTO> findAllUsersByAttendanceNotPresentByCurrentDate();

    void deleteAllByUserId(Long userId);
}
