package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User isn't active.")
public class UserIsActiveException extends RuntimeException{
    public UserIsActiveException(String message) {
        super(message);
    }
}
