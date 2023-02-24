package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect file extension.")
public class IncorrectImageFileExtensionException extends RuntimeException{
    public IncorrectImageFileExtensionException(String message) {
        super(message);
    }
}
