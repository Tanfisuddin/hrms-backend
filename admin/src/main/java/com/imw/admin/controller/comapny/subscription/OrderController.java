package com.imw.admin.controller.comapny.subscription;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.subscription.OrderService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(value = "/orders")
    public ResponseEntity<?> getListOfOrders(Authentication authentication, Pageable pageable){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = orderService.getAllOrder(currentUser.getOrganization().getId(), pageable);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
}
