package com.imw.admin.controller.admin;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.subscription.PlansService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.subscription.Plans;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.SUPER_ADMIN)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PlansController {

    @Autowired
    private PlansService plansService;

    @PostMapping(value = "/plan")
    public ResponseEntity<?> createPlan (@Valid @RequestBody  Plans plans, @RequestParam(required = false) Long organizationId){
        ResponseDTO response = plansService.createPlan(plans, organizationId, null);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "/plan/{planId}")
    public ResponseEntity<?> updatePlanWithId (@Valid @RequestBody Plans plans, @PathVariable Long planId){
        ResponseDTO response = plansService.updatePlanWithId(plans, planId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @GetMapping(value = "/plans")
    public ResponseEntity<?> get(){
        ResponseDTO response = plansService.getPlans();
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @DeleteMapping(value = "/plan/{planId}")
    public ResponseEntity<?> deletePlanWithId ( @PathVariable Long planId){
        ResponseDTO response = plansService.deletePlanWithId(planId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
}
