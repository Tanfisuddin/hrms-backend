package com.imw.admin.controller.comapny.subscription;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Policy;
import com.imw.commonmodule.repository.PolicyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class PolicyController {

    @Autowired
    PolicyRepository policyRepository;

    @GetMapping(value = "/policy")
    public ResponseEntity<?> getOrganizationPolicy(Authentication authentication){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = new ResponseDTO();
        Policy policy = policyRepository.findByOrganizationId(currentUser.getOrganization().getId());
        response.setData(policy);
        response.setSuccess(true);
        response.setMessage("Policy retrieved successfully");
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @PutMapping(value = "/policy")
    public ResponseEntity<?> createOrganizationPolicy(Authentication authentication,@Valid @RequestBody Policy policy){
        UserDetailsImpl currentUser = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO response = new ResponseDTO();

        try{
            if(policyRepository.existsByOrganizationId(currentUser.getOrganization().getId())){
                Policy updatePolicy = policyRepository.findByOrganizationId(currentUser.getOrganization().getId());
                updatePolicy.setFileUrl(policy.getFileUrl());
                updatePolicy.setUploadedAt(new Date());
                response.setData(policyRepository.save(updatePolicy));
            }else{
                policy.setUploadedAt(new Date());
                policy.setOrganization(currentUser.getOrganization());
                response.setData(policyRepository.save(policy));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        response.setSuccess(true);
        response.setMessage("Policy updated successfully");
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
}
