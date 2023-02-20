package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Suggestion not found.")
public class SuggestionNotFoundException extends RuntimeException{
    public SuggestionNotFoundException(String message) {
        super(message);
    }
}
