package com.imw.admin.controller;


import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.utils.FileUploadService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(ApiConstants.API_V1)
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ORG_ADMIN','ROLE_ORG_OWNER','ROLE_ADMIN')")
public class FileController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "file/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam(value = "file") MultipartFile file,
            Authentication authentication
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO fileUpload = fileUploadService.fileUpload(user, file);
        if (fileUpload.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,fileUpload.getData(),fileUpload.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,fileUpload.getMessage());
        }
    }
}