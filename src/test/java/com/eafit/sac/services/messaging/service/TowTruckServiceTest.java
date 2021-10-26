package com.eafit.sac.services.messaging.service;

import com.eafit.sac.services.messaging.dto.SendSMSRequest;
import com.eafit.sac.services.messaging.dto.SendTowTruckRequest;
import com.eafit.sac.services.messaging.dto.TowTruckResponse;
import com.eafit.sac.services.messaging.entity.Message;
import com.eafit.sac.services.messaging.utils.MessageStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TowTruckServiceTest {

    @Mock
    Random random;

    @Mock
    MessageService messageService;

    @InjectMocks
    TowTruckService towTruckService;

    @DisplayName("Test random letter generator")
    @Test
    void testRandomLetterGenerator() {
        doReturn(1).when(random).nextInt(26);
        char result = towTruckService.randomLetterGenerator();

        assertEquals('b', result);
    }

    @DisplayName("Test random randomValueGenerator")
    @Test
    void testRandomValueGenerator() {
        int minValue = 5;
        int maxValue = 10;

        when(random.nextInt(maxValue)).thenReturn(6);
        assertEquals(7, towTruckService.randomValueGenerator(minValue, maxValue));

        when(random.nextInt(maxValue)).thenReturn(4);
        assertEquals(9, towTruckService.randomValueGenerator(minValue, maxValue));
    }

    @DisplayName("Test random randomValueGenerator")
    @Test
    void testCarPlateGenerator() {
        String carPlate = towTruckService.generateCarPlate();
        assertEquals(6, carPlate.length());
        verify(random, times(3)).nextInt(26);
        verify(random, times(3)).nextInt(9);
    }

    @DisplayName("Test send tow truck service")
    @Test
    void testSendTowTruckService(){
        when(random.nextInt(15)).thenReturn(6);
        when(random.nextInt(26)).thenReturn(1);
        when(messageService.sendSMSMessage(any(SendSMSRequest.class))).thenReturn(new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "grúa enviada"));

        SendTowTruckRequest sendTowTruckRequest = new SendTowTruckRequest("Juan", "3168684548", "calle 12 #68-23");

        TowTruckResponse towTruckResponse = towTruckService.sendTowTruck(sendTowTruckRequest);

        assertEquals("BBB000", towTruckResponse.getAutoPlate());
        assertEquals(6, towTruckResponse.getEstimateArrivingTime());
    }
}