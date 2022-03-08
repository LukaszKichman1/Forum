package com.example.Forum.Exception;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class RequestError {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime localDateTime;

    public RequestError(String message, HttpStatus httpStatus, LocalDateTime localDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.localDateTime = localDateTime;
    }
}