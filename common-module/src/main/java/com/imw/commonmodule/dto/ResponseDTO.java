package com.imw.commonmodule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResponseDTO<T> {

    private int status;
    private boolean success;
    private T data;
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String message;

    public ResponseDTO(int status, boolean success, T data, String message) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }
}
