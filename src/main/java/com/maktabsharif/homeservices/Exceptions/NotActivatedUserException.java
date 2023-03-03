package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User not activated.")
public class NotActivatedUserException extends RuntimeException{
    public NotActivatedUserException(String message) {
        super(message);
    }
}
