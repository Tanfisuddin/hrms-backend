package com.imw.admin.controller.admin;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.subscription.AddOnsService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.subscription.AddOns;
import com.imw.commonmodule.persistence.subscription.Plans;
import com.imw.commonmodule.repository.subscription.AddOnsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.SUPER_ADMIN)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AddOnsController {

    @Autowired
    AddOnsService addOnsService;

    @PostMapping(value = "/addon")
    public ResponseEntity<?> createAddOn (@Valid @RequestBody AddOns addons, @RequestParam(required = false) Long organizationId){
        ResponseDTO response = addOnsService.createAddOn(addons);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "/addon/{addonId}")
    public ResponseEntity<?> updateAddOnWithId (@Valid @RequestBody AddOns addOns, @PathVariable Long addonId){
        ResponseDTO response = addOnsService.updateAddOnById(addonId, addOns);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @GetMapping(value = "/addons")
    public ResponseEntity<?> getAddOns(){
        ResponseDTO response = addOnsService.getListOfAddOns();
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @DeleteMapping(value = "/addon/{addonId}")
    public ResponseEntity<?> deleteAddOnWithId ( @PathVariable Long addonId){
        ResponseDTO response = addOnsService.deleteAddOnById(addonId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

}
