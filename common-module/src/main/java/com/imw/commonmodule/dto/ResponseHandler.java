package com.imw.commonmodule.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    private ResponseHandler(){
    }
    public static <T>ResponseEntity<ResponseDTO> generateResponse(HttpStatus status, boolean isSuccess, T responseObj, String message){
        ResponseDTO responseDTO = new ResponseDTO(status.value(),isSuccess, responseObj,message);
        return new ResponseEntity<>(responseDTO,status);
    }
}
