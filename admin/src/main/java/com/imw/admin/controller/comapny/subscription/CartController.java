package com.imw.admin.controller.comapny.subscription;


import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.subscription.OrderService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.subscription.BillingDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class CartController {

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/cart")
    public ResponseEntity<?> getCart(Authentication authentication){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = orderService.getPendingOrderForOrganization(currentUser.getOrganization().getId());
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PostMapping(value = "/cart/plan/{planId}")
    public ResponseEntity<?> selectPlanForOrder(Authentication authentication, @PathVariable Long planId){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = orderService.selectPlanForOrder(currentUser.getOrganization().getId(), planId);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "/cart/addon/{addonId}/member-count/{memberCount}")
    public ResponseEntity<?> selectMemberCountForAddOn(Authentication authentication, @PathVariable Long addonId, @PathVariable Integer memberCount){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = orderService.updateOrderAddOnsMemberCount(currentUser.getOrganization().getId(), addonId, memberCount);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "/cart/billing")
    public ResponseEntity<?> setBilling(Authentication authentication, @Valid @RequestBody BillingDetails billingDetails){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = orderService.setBillingDetailsForOrder(currentUser.getOrganization().getId(), billingDetails);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

}
