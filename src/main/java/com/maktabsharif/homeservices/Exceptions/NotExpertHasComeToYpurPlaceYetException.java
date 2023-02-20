package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Expert has not come to your place yet.")
public class NotExpertHasComeToYpurPlaceYetException extends RuntimeException{
    public NotExpertHasComeToYpurPlaceYetException(String message) {
        super(message);
    }
}
