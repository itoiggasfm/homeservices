package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User not approved.")
public class NotApprovedExpertException extends RuntimeException{
    public NotApprovedExpertException(String message) {
        super(message);
    }
}
