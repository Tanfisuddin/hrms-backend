package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.AccessoryService;
import com.imw.commonmodule.dto.AccessoryDto;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.enums.accessories.DeviceType;
import com.imw.commonmodule.persistence.Accessory;
import com.imw.commonmodule.repository.AccessoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.ADMIN)
@PreAuthorize("hasRole('ROLE_ORG_ADMIN')")
public class AccessoryController {

    @Autowired
    AccessoryService accessoryService;

    @GetMapping(value = "accessories")
    public ResponseEntity<?> createAccessory(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) DeviceType deviceType
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO accessories = accessoryService.findAccessories(pageable, user, search, deviceType);
        if (accessories.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,accessories.getData(),accessories.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,accessories.getMessage());
        }
    }

    @PostMapping(value = "accessory")
    public ResponseEntity<?> createAccessory(
            Authentication authentication,
            @RequestBody @Valid AccessoryDto accessory
            ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO savedAccessory = accessoryService.createAccessory(user, accessory);
        if (savedAccessory.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedAccessory.getData(),savedAccessory.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedAccessory.getMessage());
        }
    }

    @DeleteMapping(value = "accessory/{id}")
    public ResponseEntity<?> deleteAccessory(
            Authentication authentication,
            @PathVariable Long id
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO responseDto = accessoryService.deleteAccessory(user, id);
        if (responseDto.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,responseDto.getData(),responseDto.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,responseDto.getMessage());
        }
    }
}

