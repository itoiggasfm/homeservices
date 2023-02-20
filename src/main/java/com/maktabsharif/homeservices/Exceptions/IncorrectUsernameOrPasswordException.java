package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect username or password.")
public class IncorrectUsernameOrPasswordException extends RuntimeException{
    public IncorrectUsernameOrPasswordException(String message) {
        super(message);
    }
}
