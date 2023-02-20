package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No expert suggested yet.")
public class NotExpertsSuggestedYetException extends RuntimeException{
    public NotExpertsSuggestedYetException(String message) {
        super(message);
    }
}
