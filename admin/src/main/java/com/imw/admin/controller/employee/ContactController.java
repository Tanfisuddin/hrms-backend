package com.imw.admin.controller.employee;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.ContactService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Contact;
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
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "contact")
    ResponseEntity<?> getContact(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO contact = contactService.getContactByUserId(user.getId());
        if (contact.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,contact.getData(),contact.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,contact.getMessage());
        }
    }

    @PutMapping(value = "contact")
    ResponseEntity<?> saveContact(Authentication authentication, @RequestBody @Valid Contact contact) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO savedContact = contactService.saveContact(contact , user.getId());
        if (savedContact.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedContact.getData(),savedContact.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedContact.getMessage());
        }
    }
}
