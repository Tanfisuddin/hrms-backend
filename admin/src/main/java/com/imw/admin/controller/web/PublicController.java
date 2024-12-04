package com.imw.admin.controller.web;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.subscription.PlansService;
import com.imw.admin.services.superadmin.SupportService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.SupportContact;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.PUBLIC)
public class PublicController {

    @Autowired
    SupportService supportService;

    @Autowired
    PlansService plansService;

    @PostMapping(value = "/support/message")
    public ResponseEntity<?> createSupportTicket (@RequestBody @Valid SupportContact supportContact){
        ResponseDTO response = supportService.createSupportMessage(supportContact);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
    @GetMapping(value = "/plans")
    public ResponseEntity<?> getPlansList (){
        ResponseDTO response = plansService.getPlans();
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
}
