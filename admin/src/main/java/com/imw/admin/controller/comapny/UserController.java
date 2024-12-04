package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.UserService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.employee.CreateEmployeeDto;
import com.imw.commonmodule.enums.ERole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
    @PostMapping(value = "user")
    public ResponseEntity<?> createEmployee (Authentication authentication,@RequestBody @Valid CreateEmployeeDto createEmployeeDto){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO user = userService.CreateUser(currentUser,createEmployeeDto);
        if (user.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,user.getData(),user.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,user.getMessage());
        }
    }

    @DeleteMapping(value = "user/{userId}")
    public ResponseEntity<?> deleteEmployee(Authentication authentication, @PathVariable Long userId){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO user = userService.deleteUser(currentUser, userId);
        if (user.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,user.getData(),user.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,user.getMessage());
        }
    }

    @GetMapping(value = "users")
    public ResponseEntity<?> getEmployees(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false, defaultValue = "ROLE_USER") ERole role
    ){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO users =  userService.GetAllUsers(currentUser.getOrganization().getId(), pageable, search , designation, departmentId, role );
        if (users.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,users.getData(),users.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,users.getMessage());
        }
    }

    @PutMapping(value = "user/{userId}/roles")
    public ResponseEntity<?> updateUserRoles(
            Authentication authentication,
            @PathVariable Long userId,
            @RequestParam(required = false) String role
    ){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response =  userService.updateUserRole(currentUser, userId, role);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "user/{userId}/department")
    public ResponseEntity<?> updateUserDepartment(
            Authentication authentication,
            @PathVariable Long userId,
            @RequestParam Long departmentId
    ){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response =  userService.updateUserDepartment(currentUser, userId, departmentId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "user/{userId}/reporting")
    public ResponseEntity<?> updateUserReportingAuthority(
            Authentication authentication,
            @PathVariable Long userId,
            @RequestParam Long reportingAuthorityId
    ){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response =  userService.updateUserReportingAuthority(currentUser, userId, reportingAuthorityId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

}

