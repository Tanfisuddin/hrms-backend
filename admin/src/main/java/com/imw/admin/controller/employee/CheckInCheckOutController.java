package com.imw.admin.controller.employee;


import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.AttendanceService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.attendance.CheckInCheckOutDto;
import com.imw.commonmodule.persistence.Contact;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE + "/attendance")
@PreAuthorize("hasRole('ROLE_USER')")
public class CheckInCheckOutController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping(value = "check-in")
    ResponseEntity<?> checkIn(Authentication authentication, @RequestBody @Valid CheckInCheckOutDto checkInCheckOutDto){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO attendanceResponse = attendanceService.userCheckIn(currentUser, checkInCheckOutDto);
        if (attendanceResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,attendanceResponse.getData(),attendanceResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,attendanceResponse.getMessage());
        }
    }

    @PostMapping(value = "check-out")
    ResponseEntity<?> checkOut(Authentication authentication, @RequestBody @Valid CheckInCheckOutDto checkInCheckOutDto){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO attendanceResponse = attendanceService.userCheckOut(currentUser, checkInCheckOutDto);
        if (attendanceResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,attendanceResponse.getData(),attendanceResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,attendanceResponse.getMessage());
        }
    }

    @GetMapping(value = "check-in-out-status")
    ResponseEntity<?> checkInOutStatus(Authentication authentication){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO attendanceResponse = attendanceService.userCheckInOutStatus(currentUser);
        if (attendanceResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,attendanceResponse.getData(),attendanceResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,attendanceResponse.getMessage());
        }
    }
}
