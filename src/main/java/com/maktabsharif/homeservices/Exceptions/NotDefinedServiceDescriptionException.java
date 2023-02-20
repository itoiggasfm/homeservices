package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service description not defined.")
public class NotDefinedServiceDescriptionException extends RuntimeException{
    public NotDefinedServiceDescriptionException(String message) {
        super(message);
    }
}
