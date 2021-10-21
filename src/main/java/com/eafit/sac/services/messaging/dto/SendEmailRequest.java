package com.eafit.sac.services.messaging.dto;

import lombok.Getter;

@Getter
public class SendEmailRequest {
    private String to;
    private String subject;
    private String text;
}
