package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Passwords do not match.")
public class PasswordsNotMatchException extends RuntimeException{
    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
