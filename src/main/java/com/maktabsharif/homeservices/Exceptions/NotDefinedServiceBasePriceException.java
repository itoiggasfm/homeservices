package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service base price not defined.")
public class NotDefinedServiceBasePriceException extends RuntimeException{
    public NotDefinedServiceBasePriceException(String message) {
        super(message);
    }
}
