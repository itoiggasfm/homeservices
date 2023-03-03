package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid card number.")
public class InvalidCardNumberException extends RuntimeException{
    public InvalidCardNumberException(String message) {
        super(message);
    }
}
