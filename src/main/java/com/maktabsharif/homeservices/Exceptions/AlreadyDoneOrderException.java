package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order already done.")
public class AlreadyDoneOrderException extends RuntimeException{
    public AlreadyDoneOrderException(String message) {
        super(message);
    }
}
