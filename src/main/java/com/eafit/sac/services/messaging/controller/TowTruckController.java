package com.eafit.sac.services.messaging.controller;

import com.eafit.sac.services.messaging.dto.SendTowTruckRequest;
import com.eafit.sac.services.messaging.dto.TowTruckResponse;
import com.eafit.sac.services.messaging.service.TowTruckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/truck")
public class TowTruckController {

    @Autowired
    private TowTruckService towTruckService;

    @Operation(summary = "Service to send a tow truck to the customer according to the location provided in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Tow truck send scheduled"),
            @ApiResponse(responseCode = "406", description = "An error with the tow truck scheduling",
                    content = @Content)
    })
    @PostMapping(value = "/request")
    public ResponseEntity<TowTruckResponse> sendTowTruck(@RequestBody SendTowTruckRequest towTruckRequest) {
        TowTruckResponse response = towTruckService.sendTowTruck(towTruckRequest);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
