package com.eafit.sac.services.messaging.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "mailsender")
public class EmailConfig {
    private String from;
    private String username;
    private String password;
    private String host;
}
