package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service title already exists.")
public class DuplicateServiceTitleException extends RuntimeException{
    public DuplicateServiceTitleException(String message) {
        super(message);
    }
}
