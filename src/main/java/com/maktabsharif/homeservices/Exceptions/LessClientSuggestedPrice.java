package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Client's suggested price less than base price of service.")
public class LessClientSuggestedPrice extends RuntimeException{
    public LessClientSuggestedPrice(String message) {
        super(message);
    }
}
