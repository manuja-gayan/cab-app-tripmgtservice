package com.ceyloncab.tripmgtservice.application.controller;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.ConfirmPickup;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.EndTrip;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.TripPriceForAllVehicles;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.UpDateStatusOnTrip;
import com.ceyloncab.tripmgtservice.domain.service.TripManagementService;
import com.ceyloncab.tripmgtservice.domain.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = Constants.TOPIC_TRIP,groupId = "${kafka.consumer.groupId}", containerFactory = "multiTypeKafkaListenerContainerFactory")
public class TripDataKafkaListener {

    @Autowired
    private TripManagementService tripManagementService;

    @KafkaHandler
    public void handleGetTripPrice(TripPriceForAllVehicles tripPriceForAllVehicles) {
        System.out.println("Trip Price received: " + tripPriceForAllVehicles);
        tripManagementService.getTripPriceForAllVehicles(tripPriceForAllVehicles);
    }

    @KafkaHandler
    public void handleConfirmPickup(ConfirmPickup confirmPickup) {
        System.out.println("Pickup received: " + confirmPickup);
        tripManagementService.searchDriversAndNotify(confirmPickup);
    }

    @KafkaHandler
    public void handleUpdateTripStatus(UpDateStatusOnTrip acceptTrip) {
        System.out.println("Update Trip Status: " + acceptTrip);
        tripManagementService.updateTripStatus(acceptTrip);
    }

    @KafkaHandler
    public void handleEndTrip(EndTrip endTrip) {
        System.out.println("Trip End Received: " + endTrip);
        tripManagementService.performTripEnd(endTrip);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unknown type received: " + object);
    }

}
