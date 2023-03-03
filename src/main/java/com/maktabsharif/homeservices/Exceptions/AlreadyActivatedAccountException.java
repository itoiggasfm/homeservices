package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account is already activated.")
public class AlreadyActivatedAccountException extends RuntimeException{
    public AlreadyActivatedAccountException(String message) {
        super(message);
    }
}
