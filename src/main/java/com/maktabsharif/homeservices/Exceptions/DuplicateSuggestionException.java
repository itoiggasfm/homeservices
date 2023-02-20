package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Suggestion is already made.")
public class DuplicateSuggestionException extends RuntimeException{
    public DuplicateSuggestionException(String message) {
        super(message);
    }
}
