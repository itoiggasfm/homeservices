package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No expert selected yet.")
public class NotExpertSelectedYetException extends RuntimeException{
    public NotExpertSelectedYetException(String message) {
        super(message);
    }
}
