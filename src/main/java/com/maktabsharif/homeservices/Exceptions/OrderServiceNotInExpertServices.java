package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Order service is not in expert services.")
public class OrderServiceNotInExpertServices extends RuntimeException{
    public OrderServiceNotInExpertServices(String message) {
        super(message);
    }
}
