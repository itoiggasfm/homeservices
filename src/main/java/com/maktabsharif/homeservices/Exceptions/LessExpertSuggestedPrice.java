package com.maktabsharif.homeservices.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Expert's suggested price less than client's suggested price.")
public class LessExpertSuggestedPrice extends RuntimeException{
    public LessExpertSuggestedPrice(String message) {
        super(message);
    }
}
