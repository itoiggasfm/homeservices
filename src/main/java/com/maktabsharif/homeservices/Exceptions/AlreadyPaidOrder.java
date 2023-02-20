package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order is already paid.")
public class AlreadyPaidOrder extends RuntimeException{
    public AlreadyPaidOrder(String message) {
        super(message);
    }
}
