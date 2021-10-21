package com.eafit.sac.services.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TowTruckResponse {
    private String autoPlate;
    private int estimateArrivingTime;
}
