package com.imw.admin.controller.comapny;

import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.ShiftService;
import com.imw.commonmodule.dto.AccessoryDto;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.dto.UserIdsDTO;
import com.imw.commonmodule.persistence.Shift;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.ADMIN)
public class ShiftController {

    @Autowired
    ShiftService shiftService;

    @PostMapping(value = "shift")
    public ResponseEntity<?> createShift(
        Authentication authentication,
        @RequestBody @Valid Shift shift
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO savedShift = shiftService.createShift(user, shift);
        if (savedShift.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedShift.getData(),savedShift.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedShift.getMessage());
        }
    }

    @DeleteMapping(value = "shift/{shiftId}")
    public ResponseEntity<?> deleteShift(
            Authentication authentication,
            @PathVariable Long shiftId
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO deletedShift = shiftService.deleteShift(user, shiftId);
        if (deletedShift.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,deletedShift.getData(),deletedShift.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,deletedShift.getMessage());
        }
    }

    @PutMapping(value = "shift/{shiftId}")
    public ResponseEntity<?> updateShift(
            Authentication authentication,
            @PathVariable Long shiftId,
            @RequestBody @Valid Shift shift
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO updateShift = shiftService.updateShift(user, shiftId, shift);
        if (updateShift.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,updateShift.getData(),updateShift.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,updateShift.getMessage());
        }
    }

    @GetMapping(value = "shifts")
    public ResponseEntity<?> getShiftList(Authentication authentication) {

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO shifts = shiftService.getShiftList(user);
        if (shifts.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,shifts.getData(),shifts.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,shifts.getMessage());
        }
    }

    @GetMapping(value = "shift/{shiftId}/users")
    public ResponseEntity<?> getUsersListInShift(
        Authentication authentication,
        @PathVariable Long shiftId,
        Pageable pageable
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO shiftUsers = shiftService.getShiftUserList(user, shiftId, pageable);
        if (shiftUsers.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,shiftUsers.getData(),shiftUsers.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,shiftUsers.getMessage());
        }
    }
    @PutMapping(value = "shift/{shiftId}/users")
    public ResponseEntity<?> addUsersInShift(
            Authentication authentication,
            @PathVariable Long shiftId,
            @RequestBody @Valid UserIdsDTO userIdsDTO
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO shiftUsers = shiftService.addUsersInShift(user, shiftId, userIdsDTO);
        if (shiftUsers.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,shiftUsers.getData(),shiftUsers.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,shiftUsers.getMessage());
        }
    }
    @DeleteMapping(value = "shift/{shiftId}/users")
    public ResponseEntity<?> removeUsersFromShift(
            Authentication authentication,
            @PathVariable Long shiftId,
            @RequestBody @Valid UserIdsDTO userIdsDTO
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO shiftUsers = shiftService.removeUsersFromShift(user, shiftId, userIdsDTO);
        if (shiftUsers.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,shiftUsers.getData(),shiftUsers.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,shiftUsers.getMessage());
        }
    }
}
