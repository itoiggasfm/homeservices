package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order already exists.")
public class DuplicateOrderException extends RuntimeException{
    public DuplicateOrderException(String message) {
        super(message);
    }
}
