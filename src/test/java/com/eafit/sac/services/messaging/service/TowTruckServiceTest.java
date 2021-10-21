package com.eafit.sac.services.messaging.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class TowTruckServiceTest {

    @Mock
    Random random;

    @InjectMocks
    TowTruckService towTruckService;

    @DisplayName("Test random letter generator")
    @Test
    void testRandomLetterGenerator() {
        when(random.nextInt()).thenReturn(1);
        assertEquals('a', towTruckService.randomLetterGenerator());
    }

    @DisplayName("Test random randomValueGenerator")
    @Test
    void testRandomValueGenerator() {
        int minValue = 5;
        int maxValue = 10;

        when(random.nextInt(maxValue)).thenReturn(6);
        assertEquals(6, towTruckService.randomValueGenerator(minValue, maxValue));

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
}