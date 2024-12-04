package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.AttendanceService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("attendances")
    public ResponseEntity<?> getAttendanceList(
        Authentication authentication,
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String designation,
        @RequestParam(required = false) Long departmentId,
        Pageable pageable
    ) {
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO departments = attendanceService.getAttendanceByOrganizationAndFilters(currentUser, date, search, designation, departmentId, pageable);
        if (departments.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,departments.getData(),departments.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,departments.getMessage());
        }
    }
    @GetMapping(value = "user/{id}/attendances")
    ResponseEntity<?> getEmployeeAttendanceList(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate month,
            Pageable pageable
    ) {
        ResponseDTO attendanceResponse = attendanceService.getAttendanceByUserAndMonth(id, month, pageable);
        if (attendanceResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,attendanceResponse.getData(),attendanceResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,attendanceResponse.getMessage());
        }
    }
}
