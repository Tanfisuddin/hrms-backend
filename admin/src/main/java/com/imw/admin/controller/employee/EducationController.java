package com.imw.admin.controller.employee;


import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Education;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.EducationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE)
@PreAuthorize("hasRole('ROLE_USER')")

public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping(value = "educations")
    ResponseEntity<?> getAllEducation(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO  educations = educationService.getALlByUserId(user.getId());
        if ( educations.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true, educations.getData(), educations.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null, educations.getMessage());
        }
    }
    @PutMapping(value = "education")
    ResponseEntity<?> saveEducation(Authentication authentication, @RequestBody @Valid Education education) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO  savedEducation = educationService.saveEducation(education , user.getId());
        if (  savedEducation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,  savedEducation.getData(),  savedEducation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,  savedEducation.getMessage());
        }
    }
}
