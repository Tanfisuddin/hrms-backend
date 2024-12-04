package com.imw.admin.controller.admin;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.UserService;
import com.imw.admin.services.subscription.OrderService;
import com.imw.admin.services.subscription.SubscriptionService;
import com.imw.admin.services.superadmin.OrganizationService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.enums.ERole;
import com.imw.commonmodule.persistence.User;
import com.imw.commonmodule.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(ApiConstants.SUPER_ADMIN)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping(value = "who-is")
    public ResponseEntity<?> whoIs (Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @GetMapping(value = "organizations")
    public ResponseEntity<?> getOrganizationAndOwners (Pageable pageable, @RequestParam(required = false) String search){
        ResponseDTO response = organizationService.getOrganizationsAndOwner(search, pageable);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
    @GetMapping(value = "organization/{id}/users")
    public ResponseEntity<?> getOrganizationAndEmployees (Pageable pageable, @PathVariable Long id , @RequestParam(required = false) String search){
        ResponseDTO response = userService.GetAllUsers(id,pageable, search, null, null, ERole.ROLE_USER);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<?> getListOfOrders(Authentication authentication, Pageable pageable){
        ResponseDTO response = orderService.getAllOrder(null, pageable);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @GetMapping(value = "/subscriptions")
    public ResponseEntity<?> getListOdSubscriptions(Authentication authentication, Pageable pageable){
        ResponseDTO response = subscriptionService.getAllSubscriptions(null, pageable);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

}
