package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order already started.")
public class AlreadyStartedOrder extends RuntimeException{
    public AlreadyStartedOrder(String message) {
        super(message);
    }
}
