package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient funds.")
public class InsufficientFondsException extends RuntimeException{
    public InsufficientFondsException(String message) {
        super(message);
    }
}
