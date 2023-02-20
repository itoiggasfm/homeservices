package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User already approved.")
public class ApprovedExpertException extends RuntimeException{
    public ApprovedExpertException(String message) {
        super(message);
    }
}
