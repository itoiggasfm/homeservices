package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service not found.")
public class ServicesNotFoundException extends RuntimeException{
    public ServicesNotFoundException(String message) {
        super(message);
    }
}
