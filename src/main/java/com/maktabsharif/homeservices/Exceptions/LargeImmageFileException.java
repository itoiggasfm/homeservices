package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Largge image file.")
public class LargeImmageFileException extends RuntimeException{
    public LargeImmageFileException(String message) {
        super(message);
    }
}
