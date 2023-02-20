package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Subservice not found.")
public class SubservicesNotFoundException extends RuntimeException{
    public SubservicesNotFoundException(String message) {
        super(message);
    }
}
