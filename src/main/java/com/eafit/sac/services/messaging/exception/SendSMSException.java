package com.eafit.sac.services.messaging.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class SendSMSException extends RuntimeException {
    public SendSMSException(String message) {
        super(message);
    }
}
