package com.fullstack.Backend.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ErrorMessage {
    private HttpStatus statusCode;

    private String message;

    private String serverTime;
}
