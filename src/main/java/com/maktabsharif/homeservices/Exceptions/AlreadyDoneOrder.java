package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order already done.")
public class AlreadyDoneOrder extends RuntimeException{
    public AlreadyDoneOrder(String message) {
        super(message);
    }
}
