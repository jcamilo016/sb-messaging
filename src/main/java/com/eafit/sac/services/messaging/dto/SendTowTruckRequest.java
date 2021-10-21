package com.eafit.sac.services.messaging.dto;

import lombok.Getter;

@Getter
public class SendTowTruckRequest {
    private String clientName;
    private String phoneNumber;
    private String currentLocation;
}
