package com.eafit.sac.services.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendTowTruckRequest {
    private String clientName;
    private String phoneNumber;
    private String currentLocation;
}
