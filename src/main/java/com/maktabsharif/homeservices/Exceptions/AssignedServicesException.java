package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Service already assigned to expert.")
public class AssignedServicesException extends RuntimeException{
    public AssignedServicesException(String message) {
        super(message);
    }
}
