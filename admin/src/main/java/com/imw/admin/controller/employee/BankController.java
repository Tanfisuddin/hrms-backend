package com.imw.admin.controller.employee;


import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Bank;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.BankService;
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
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping(value = "bank")
    ResponseEntity<?> getContact(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO bank = bankService.getBankByUserId(user.getId());
        if (bank.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,bank.getData(),bank.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,bank.getMessage());
        }

    }
    @PutMapping(value = "bank")
    ResponseEntity<?> saveContact(Authentication authentication, @RequestBody @Valid Bank bank) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO savedBank = bankService.saveBank(bank , user.getId());
        if (savedBank.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedBank.getData(),savedBank.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedBank.getMessage());
        }
    }
}
