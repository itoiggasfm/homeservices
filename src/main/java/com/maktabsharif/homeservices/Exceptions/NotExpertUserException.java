package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User not an expert.")
public class NotExpertUserException extends RuntimeException{
    public NotExpertUserException(String message) {
        super(message);
    }
}
