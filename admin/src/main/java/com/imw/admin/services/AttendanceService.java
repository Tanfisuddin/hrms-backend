package com.imw.admin.services;

import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.utils.GeoLocation;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.attendance.CheckInCheckOutDto;
import com.imw.commonmodule.dto.attendance.CheckInCheckOutStatusDto;
import com.imw.commonmodule.enums.attendance.AttendanceStatus;
import com.imw.commonmodule.enums.attendance.CheckInCheckOutStatus;
import com.imw.commonmodule.persistence.Attendance;
import com.imw.commonmodule.persistence.Shift;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.AttendanceRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(AttendanceService.class);

    public ResponseDTO userCheckIn(UserDetailsImpl currentUser, CheckInCheckOutDto checkInCheckOutDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Shift userShift = userRepository.findShiftByUserId(currentUser.getId()).getShift();
            if(userShift == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("No shift assigned by admin.");
                return responseDTO;
            }

            LocalDate currentDate = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByCheckInDateAndUserId(currentDate, currentUser.getId());

//            Not having attendance records meaning check in not yet performed
            if(attendance.isEmpty()){
                Attendance newAttendance = new Attendance();
                User user = userRepository.getReferenceById(currentUser.getId());
                newAttendance.setUser(user);
                newAttendance.setCheckInDate(currentDate);
                newAttendance.setCheckInTime(LocalTime.now());

                if(userShift.getIsGeoFencingEnabled() != null && userShift.getIsGeoFencingEnabled()){
                    if(
                            checkInCheckOutDto.getLocation()!= null && !checkInCheckOutDto.getLocation().isEmpty() &&
                            checkInCheckOutDto.getLongitude()!= null &&
                            checkInCheckOutDto.getLatitude()!= null
                    ){
                        if(GeoLocation.isLocationInsideCircle(userShift.getLatitude(), userShift.getLongitude(), checkInCheckOutDto.getLatitude(), checkInCheckOutDto.getLongitude(), userShift.getRadius())){
                            newAttendance.setCheckInLocation(checkInCheckOutDto.getLocation());
                            newAttendance.setCheckInLongitude(checkInCheckOutDto.getLongitude());
                            newAttendance.setCheckInLatitude(checkInCheckOutDto.getLatitude());
                        }else{
                            responseDTO.setSuccess(false);
                            responseDTO.setMessage("Current location is outside of check in location .");
                            return  responseDTO;
                        }
                    }else{
                        responseDTO.setSuccess(false);
                        responseDTO.setMessage("Location details needed to check in.");
                        return  responseDTO;
                    }
                }
                Attendance savedAttendance =  attendanceRepository.save(newAttendance);
                responseDTO.setData(savedAttendance);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Check in created successfully");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Check in already done!");
            }

        }catch(Exception e){
            log.error("Error creating attendance on check in: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating attendance on check in");
        }
        return responseDTO;

    }

    public ResponseDTO userCheckOut(UserDetailsImpl currentUser, CheckInCheckOutDto checkInCheckOutDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            Shift userShift = userRepository.findShiftByUserId(currentUser.getId()).getShift();
            if(userShift == null){
                responseDTO.setSuccess(false);
                responseDTO.setMessage("No shift assigned by admin.");
                return responseDTO;
            }

            LocalDate currentDate = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByCheckInDateAndUserId(currentDate, currentUser.getId());

            if(attendance.isPresent()){
                Attendance checkOutAttendance = attendance.get();

                if(checkOutAttendance.getCheckOutTime() != null){
                    responseDTO.setSuccess(false);
                    responseDTO.setMessage("Check out time already exists");
                    return  responseDTO;
                }

                checkOutAttendance.setCheckOutTime(LocalTime.now());
                checkOutAttendance.setStatus(getAttendanceStatus(checkOutAttendance, userShift));

                if(userShift.getIsGeoFencingEnabled() != null && userShift.getIsGeoFencingEnabled()){
                    if(
                            checkInCheckOutDto.getLocation()!= null && !checkInCheckOutDto.getLocation().isEmpty() &&
                                    checkInCheckOutDto.getLongitude()!= null &&
                                    checkInCheckOutDto.getLatitude()!= null
                    ){
                        if(GeoLocation.isLocationInsideCircle(userShift.getLatitude(), userShift.getLongitude(), checkInCheckOutDto.getLatitude(), checkInCheckOutDto.getLongitude(), userShift.getRadius())){
                            checkOutAttendance.setCheckOutLocation(checkInCheckOutDto.getLocation());
                            checkOutAttendance.setCheckOutLongitude(checkInCheckOutDto.getLongitude());
                            checkOutAttendance.setCheckOutLatitude(checkInCheckOutDto.getLatitude());
                        }else{
                            responseDTO.setSuccess(false);
                            responseDTO.setMessage("Current location is outside of check out location .");
                            return  responseDTO;
                        }
                    }else{
                        responseDTO.setSuccess(false);
                        responseDTO.setMessage("Location details needed to check out.");
                        return  responseDTO;
                    }
                }
                Attendance savedAttendance =  attendanceRepository.save(checkOutAttendance);
                responseDTO.setData(savedAttendance);
                responseDTO.setSuccess(true);
                responseDTO.setMessage("Check out created successfully");
            }else{
                responseDTO.setSuccess(false);
                responseDTO.setMessage("Check in required before checking out!");
            }

        }catch(Exception e){
            log.error("Error creating attendance on check out: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error creating attendance on check out");
        }
        return responseDTO;
    }

    public ResponseDTO userCheckInOutStatus(UserDetailsImpl currentUser) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            LocalDate currentDate = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByCheckInDateAndUserId(currentDate, currentUser.getId());

            CheckInCheckOutStatusDto checkInCheckOutStatusDto = new CheckInCheckOutStatusDto();
            if(attendance.isPresent()){
                if(attendance.get().getCheckOutTime() != null){
                    checkInCheckOutStatusDto.setCheckInTime(attendance.get().getCheckInTime());
                    checkInCheckOutStatusDto.setCheckOutTime(attendance.get().getCheckOutTime());
                    checkInCheckOutStatusDto.setStatus(CheckInCheckOutStatus.CHECKED_OUT);
                }
                else if(attendance.get().getCheckInTime() != null){
                    checkInCheckOutStatusDto.setCheckInTime(attendance.get().getCheckInTime());
                    checkInCheckOutStatusDto.setStatus(CheckInCheckOutStatus.CHECKED_IN);
                }
            }
            else{
                checkInCheckOutStatusDto.setStatus(CheckInCheckOutStatus.NOT_CHECKED_IN);
            }

            responseDTO.setData(checkInCheckOutStatusDto);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("User attendance status.");
        }catch(Exception e){
            log.error("Error getting the attendance status: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error getting the attendance status");
        }
        return responseDTO;
    }

    public  ResponseDTO getAttendanceByUserAndMonth(Long userId, LocalDate month, Pageable pageable){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(month == null){month = LocalDate.now();}
            YearMonth currentYearMonth = YearMonth.from(month);
            LocalDate startDateOfMonth = currentYearMonth.atDay(1);
            LocalDate endDateOfMonth = currentYearMonth.atEndOfMonth();
            Page<Attendance> attendancePage =  attendanceRepository.findByUserIdAndCheckInDateBetween(userId, startDateOfMonth, endDateOfMonth, pageable);
            responseDTO.setData(attendancePage);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Attendance for the month "+currentYearMonth+" found");
        }catch(Exception e){
            log.error("Error finding the attendance for the user by month: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding the attendance for the user by month");
        }
        return responseDTO;
    }

    public  ResponseDTO getAttendanceByOrganizationAndFilters(UserDetailsImpl currentUser, LocalDate date, String search, String designation, Long departmentId, Pageable pageable){
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if(date == null){date = LocalDate.now();}
            Long organizationId = currentUser.getOrganization().getId();
            List<Long> userIds;

            if(departmentId!=null) {
                if(search!=null && !search.isBlank()){
                    if(designation!=null && !designation.isBlank()){
                        userIds = userRepository.findUserIdsByOrganizationIdAndDepartmentIdAndDesignationAndSearch(organizationId,departmentId, designation, search);
                    }else{
                        userIds = userRepository.findUserIdsByOrganizationIdAndDepartmentIdAndSearch(organizationId,departmentId, search);
                    }
                }else{
                    if(designation!=null && !designation.isBlank()){
                        userIds = userRepository.findUserIdsByOrganizationIdAndDepartmentIdAndDesignation(organizationId,departmentId, designation);
                    }else{
                        userIds = userRepository.findUserIdsByOrganizationIdAndDepartmentId(organizationId,departmentId);
                    }
                }
            }else{
                if(search!=null && !search.isBlank()){
                    if(designation!=null && !designation.isBlank()){
                        userIds = userRepository.findUserIdsByOrganizationIdAndDesignationAndSearch(organizationId, designation, search);
                    }else{
                        userIds = userRepository.findUserIdsByOrganizationIdAndSearch(organizationId, search);
                    }
                }else{
                    if(designation!=null && !designation.isBlank()){
                        userIds = userRepository.findUserIdsByOrganizationIdAndDesignation(organizationId, designation);
                    }else{
                        userIds = userRepository.findUserIdsByOrganizationId(organizationId);
                    }
                }
            }

            Page<Attendance> attendancePage =  attendanceRepository.findByCheckInDateAndUserIdIn(date, userIds, pageable);
            responseDTO.setData(attendancePage);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("Attendance for the day "+date);
        }catch(Exception e){
            log.error("Error finding the attendance by date: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error finding the attendance by date");
        }
        return responseDTO;
    }

    private AttendanceStatus getAttendanceStatus(Attendance attendance, Shift shift){
        // Setting attendance status based on check in and check out time.
        LocalTime checkInTime = attendance.getCheckInTime();
        LocalTime checkOutTime = attendance.getCheckOutTime();
        LocalTime shiftBufferTime = shift.getBufferTimeTo();
        LocalTime shiftEndTime = shift.getShiftEndTime();
        LocalTime halfDayTime = findMidTime(shift.getShiftStartTime(), shift.getShiftEndTime());

        if(checkInTime.isBefore(shiftBufferTime.plusMinutes(1)) && checkOutTime.isAfter(shiftEndTime)){
            return AttendanceStatus.PRESENT;
        }
        else if(checkInTime.isBefore(halfDayTime.plusMinutes(1)) && checkOutTime.isAfter(shiftEndTime)){
            return AttendanceStatus.HALF_DAY;
        }
        else if(checkInTime.isBefore(shiftBufferTime.plusMinutes(1)) && checkOutTime.isAfter(halfDayTime)){
            return AttendanceStatus.HALF_DAY;
        }
        return AttendanceStatus.ABSENT;
    }

    public static LocalTime findMidTime(LocalTime time1, LocalTime time2) {
        int totalSeconds1 = time1.toSecondOfDay();
        int totalSeconds2 = time2.toSecondOfDay();
        int averageSeconds = (totalSeconds1 + totalSeconds2) / 2;

        return LocalTime.ofSecondOfDay(averageSeconds);
    }

}
