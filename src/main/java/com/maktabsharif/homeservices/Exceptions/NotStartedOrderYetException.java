package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order not started yet.")
public class NotStartedOrderYetException extends RuntimeException{
    public NotStartedOrderYetException(String message) {
        super(message);
    }
}
