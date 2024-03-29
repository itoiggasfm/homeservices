package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password does not meet.")
public class PasswordPolicyException extends RuntimeException{
    public PasswordPolicyException(String message) {
        super(message);
    }
}
