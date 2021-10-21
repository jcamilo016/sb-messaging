package com.eafit.sac.services.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RepositoryRequestException extends RuntimeException {
    public RepositoryRequestException(String message) {
        super(message);
    }
}
