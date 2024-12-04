package com.imw.admin.controller.employee;

import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Address;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.AddressService;
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
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "address")
    ResponseEntity<?> getAllAddress(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO addresses = addressService.getAddressesByUserId(user.getId());
        if (addresses.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,addresses.getData(),addresses.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,addresses.getMessage());
        }
    }
    @PutMapping(value = "address")
    ResponseEntity<?> saveAddress(Authentication authentication, @RequestBody @Valid Address address) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO  addressResponse = addressService.saveAddress(address , user.getId());
        if (addressResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,addressResponse.getData(),addressResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,addressResponse.getMessage());
        }
    }
}
