package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Start date by client is before now.")
public class StartDateByClientBeforeNowException extends RuntimeException{
    public StartDateByClientBeforeNowException(String message) {
        super(message);
    }
}
