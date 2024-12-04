package com.imw.admin.controller.comapny;


import com.imw.admin.common.ApiConstants;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.admin.services.HolidayService;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.dto.ResponseHandler;
import com.imw.commonmodule.persistence.Shift;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping(ApiConstants.ADMIN)
public class HolidayController {

    @Autowired
    HolidayService holidayService;

//    @PutMapping(value = "holiday")
//    public ResponseEntity<?> createOrUpdateHoliday(
//            Authentication authentication,
//            @RequestBody @Valid Shift shift
//    ) {
//        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
//        ResponseDTO savedShift = holidayService;
//        if (savedShift.isSuccess()){
//            return ResponseHandler.generateResponse(HttpStatus.OK,true,savedShift.getData(),savedShift.getMessage());
//        }else {
//            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,savedShift.getMessage());
//        }
//    }

    @PostMapping(value = "holidays/upload")
    public ResponseEntity<?> uploadHolidayList(
            Authentication authentication,
            MultipartFile file
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO uploadHoliday = holidayService.uploadHolidayList(user, file);
        if (uploadHoliday.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,uploadHoliday.getData(),uploadHoliday.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,uploadHoliday.getMessage());
        }
    }

    @GetMapping(value = "holidays")
    public ResponseEntity<?> getHolidayList(
            Authentication authentication,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO holidayList = holidayService.getHolidayListByDateRange(user, startDate, endDate, pageable);
        if (holidayList.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,holidayList.getData(),holidayList.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,holidayList.getMessage());
        }
    }
    @DeleteMapping(value = "holiday/{id}")
    public ResponseEntity<?> deleteHoliday(
            Authentication authentication,
            @PathVariable Long id
    ) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        ResponseDTO holidayResponse = holidayService.deleteHolidayWithId(user, id);
        if (holidayResponse.isSuccess()){
            return ResponseHandler.generateResponse(HttpStatus.OK,true,holidayResponse.getData(),holidayResponse.getMessage());
        }else {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,false,null,holidayResponse.getMessage());
        }
    }
}
