package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service title not defined.")
public class NotDefinedServiceTitleException extends RuntimeException{
    public NotDefinedServiceTitleException(String message) {
        super(message);
    }
}
