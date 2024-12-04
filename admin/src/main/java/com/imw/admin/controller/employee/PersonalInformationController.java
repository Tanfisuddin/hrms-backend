package com.imw.admin.controller.employee;


import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.PersonalInformation;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.PersonalInformationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.EMPLOYEE)
@PreAuthorize("hasRole('ROLE_USER')")
public class PersonalInformationController {

    @Autowired
    private PersonalInformationService personalInformationService;

    @GetMapping(value = "personal-information")
    ResponseEntity<?> getPersonalInformation(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO personalInformation = personalInformationService.getPersonalInformation(user.getId());
        if (  personalInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,  personalInformation.getData(),  personalInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,  personalInformation.getMessage());
        }
    }

    @PutMapping(value = "personal-information")
    ResponseEntity<?> updatePersonalInformation(Authentication authentication, @RequestBody @Valid PersonalInformation personalInformation) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO savedPersonalInformation = personalInformationService.setPersonalInformation(personalInformation, user.getId());
        if (  savedPersonalInformation.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,  savedPersonalInformation.getData(),  savedPersonalInformation.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,  savedPersonalInformation.getMessage());
        }
    }

}
