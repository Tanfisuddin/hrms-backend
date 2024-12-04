package com.imw.admin.controller.employee;

import com.imw.admin.common.ApiConstants;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Document;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.employee.DocumentService;
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
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @GetMapping(value = "documents")
    ResponseEntity<?> getAllDocuments(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO documents = documentService.getDocumentsByUserId(user.getId());
        if (documents.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,documents.getData(),documents.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,documents.getMessage());
        }
    }
    @PutMapping(value = "document")
    ResponseEntity<?> saveDocument(Authentication authentication, @RequestBody @Valid Document document) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        ResponseDTO savedDocument = documentService.saveDocument(document , user.getId());
        if (savedDocument.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedDocument.getData(),savedDocument.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedDocument.getMessage());
        }
    }

}
