package com.imw.admin.controller.employee;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.*;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.employee.EmployeeInformationDto;
import com.imw.commonmodule.dto.employee.JoiningDetailsDto;
import com.imw.commonmodule.persistence.Education;
import com.imw.commonmodule.persistence.Policy;
import com.imw.commonmodule.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping(ApiConstants.EMPLOYEE)
@PreAuthorize("hasRole('ROLE_USER')")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessoryService accessoryService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    PolicyRepository policyRepository;

    @GetMapping(value = "")
    ResponseEntity<?> getEmployeeInformation(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO employeeInformation = userService.GetUserInfo(user.getId());
        if (employeeInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,employeeInformation.getData(),employeeInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,employeeInformation.getMessage());
        }
    }
    @PostMapping(value = "/profile-image")
    ResponseEntity<?> setEmployeeProfile(Authentication authentication, @RequestBody Map<String, String> body) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO employeeInformation = userService.updateUserProfileImage(user.getId(), body.get("profileImage"));
        if (employeeInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,employeeInformation.getData(),employeeInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,employeeInformation.getMessage());
        }
    }

    @GetMapping(value = "/joining")
    ResponseEntity<?> getEmployeeJoining(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO joiningDetails = userService.GetUserJoining(user.getId());
        if (joiningDetails.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,joiningDetails.getData(),joiningDetails.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,joiningDetails.getMessage());
        }
    }

    @GetMapping(value = "/accessories")
    ResponseEntity<?> getEmployeeAccessoryList(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO accessoriesDto = accessoryService.getAccessoryListByUser(user);
        if (accessoriesDto.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,accessoriesDto.getData(),accessoriesDto.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,accessoriesDto.getMessage());
        }
    }

    @GetMapping(value = "/attendances")
    ResponseEntity<?> getEmployeeAttendanceList(
        Authentication authentication,
        @RequestParam(required = false) LocalDate month,
        Pageable pageable
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO attendanceResponse = attendanceService.getAttendanceByUserAndMonth(user.getId(), month, pageable);
        if (attendanceResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,attendanceResponse.getData(),attendanceResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,attendanceResponse.getMessage());
        }
    }

    @GetMapping(value = "holidays")
    public ResponseEntity<?> getHolidayList(
            Authentication authentication,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO holidayList = holidayService.getHolidayListByDateRange(user, startDate, endDate, pageable);
        if (holidayList.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,holidayList.getData(),holidayList.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,holidayList.getMessage());
        }
    }

    @GetMapping(value = "shift")
    public ResponseEntity<?> getEmployeeShift(
            Authentication authentication
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO shiftResponse = shiftService.getShiftByUserId(user.getId());
        if (shiftResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,shiftResponse.getData(),shiftResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,shiftResponse.getMessage());
        }
    }

    @GetMapping(value = "/policy")
    public ResponseEntity<?> getOrganizationPolicy(Authentication authentication){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = new ResponseDTO();
        Policy policy = policyRepository.findByOrganizationId(currentUser.getOrganization().getId());
        response.setData(policy);
        response.setSuccess(true);
        response.setMessage("Policy retrieved successfully");
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

}
