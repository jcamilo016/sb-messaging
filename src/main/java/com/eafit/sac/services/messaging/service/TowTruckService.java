package com.eafit.sac.services.messaging.service;

import com.eafit.sac.services.messaging.dto.SendSMSRequest;
import com.eafit.sac.services.messaging.dto.SendTowTruckRequest;
import com.eafit.sac.services.messaging.dto.TowTruckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TowTruckService {

    @Autowired
    Random random;

    @Autowired
    MessageService messageService;


    public TowTruckResponse sendTowTruck(SendTowTruckRequest request) {
        int minutesToArrive = randomValueGenerator(5, 15);
        String carPlate = generateCarPlate();
        String messageToSend = "Cordial saludo " + request.getClientName()
                + "\n\nLe informamos que su grúa ha sido programada. El vehiculo de placas "
                + carPlate
                + " llegará a su ubicación ("
                + request.getCurrentLocation()
                + ") en aproximadamente "
                + minutesToArrive
                + " minutos.";
        SendSMSRequest smsRequest = new SendSMSRequest(request.getPhoneNumber(), messageToSend);
        messageService.sendSMSMessage(smsRequest);

        threadToNotifyTruckArriving(minutesToArrive, request.getPhoneNumber(), carPlate);

        return new TowTruckResponse(carPlate, minutesToArrive);
    }

    public void threadToNotifyTruckArriving(int minutesToArrive, String phoneNumber, String carPlate) {
        String messageToSend = "La grúa solicitada ("
                + carPlate
                + ") ya se encuentra en su ubicación.";

        SendSMSRequest smsRequest = new SendSMSRequest(phoneNumber, messageToSend);
        new Thread(() -> {
            try {
                System.out.println("Se ha programado un hilo de ejecución el cual terminará en " + minutesToArrive + " minutos");
                Thread.sleep(1000L * 60 * minutesToArrive);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                messageService.sendSMSMessage(smsRequest);
                System.out.println("Hilo terminó!!!!!");
            }
        }).start();
    }

    public String generateCarPlate() {
        String sb = String.valueOf(randomLetterGenerator()) + randomLetterGenerator() + randomLetterGenerator() +
                randomValueGenerator(0, 9) + randomValueGenerator(0, 9) + randomValueGenerator(0, 9);

        return sb.toUpperCase();
    }

    public int randomValueGenerator(int minValue, int maxValue) {
        int generatedValue = random.nextInt(maxValue);

        if (generatedValue < minValue) {
            generatedValue += minValue;
        }

        return generatedValue;
    }

    public char randomLetterGenerator() {
        return (char) (random.nextInt(26) + 'a');
    }
}
