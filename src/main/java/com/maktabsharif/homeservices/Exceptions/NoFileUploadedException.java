package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No file uploaded.")
public class NoFileUploadedException extends RuntimeException{
    public NoFileUploadedException(String message) {
        super(message);
    }
}
