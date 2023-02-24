package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Subservice title already exists.")
public class DuplicateSubserviceTitleException extends RuntimeException{
    public DuplicateSubserviceTitleException(String message) {
        super(message);
    }
}
