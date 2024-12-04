package com.imw.admin.controller.admin;
import com.imw.admin.common.ApiConstants;
import com.imw.admin.services.superadmin.SupportService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.repository.SupportContactRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.SUPER_ADMIN+"/support")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SupportController {

    @Autowired
    SupportService supportService;

    @GetMapping(value = "messages")
    public ResponseEntity<?> getMessageList (Pageable pageable){
        ResponseDTO response = supportService.getSupportList(pageable);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @DeleteMapping(value = "message/{id}")
    public ResponseEntity<?> deleteMessageById (@PathVariable Long id){
        ResponseDTO response = supportService.deleteSupportTicketById(id);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }

    @DeleteMapping(value = "messages")
    public ResponseEntity<?> deleteMessagesByIds (@RequestBody List<Long> ids){
        ResponseDTO response = supportService.deleteSupportTicketsByIds(ids);
        if (response.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,response.getData(),response.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,response.getMessage());
        }
    }
}