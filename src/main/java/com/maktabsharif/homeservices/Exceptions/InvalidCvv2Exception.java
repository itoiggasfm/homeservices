package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid CVV2.")
public class InvalidCvv2Exception extends RuntimeException{
    public InvalidCvv2Exception(String message) {
        super(message);
    }
}
