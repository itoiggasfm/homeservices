package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Start date by expert is after start date by client.")
public class StartDateByExpertBeforeStartDateByClintException extends RuntimeException{
    public StartDateByExpertBeforeStartDateByClintException(String message) {
        super(message);
    }
}
