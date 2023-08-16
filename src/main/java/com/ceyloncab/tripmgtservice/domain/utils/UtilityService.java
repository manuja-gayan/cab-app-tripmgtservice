package com.ceyloncab.tripmgtservice.domain.utils;

import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
import com.ceyloncab.tripmgtservice.domain.entity.VehiclesEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.DistanceAndTime;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.exception.IgnorableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UtilityService {

    public static <K,V extends Comparable<? super V>> Map<K,V> sortByValue(Map<K,V> map){
        try {
            List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByValue());
            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;

        } catch (Exception ex){
            log.error("Sort values in map error:"+ex.getMessage());
            throw new IgnorableException("Sort values in map error:"+ex.getMessage());
        }
    }

    public Double calculatePrice(Location pickupLocation, DistanceAndTime distanceAndTime, LocalDateTime startTime, VehiclesEntity vehicle){
        Double price = distanceAndTime.getDistance()*vehicle.getFlatRate();
        return price;
    }

    /**
     * Distance and waitingTime should in base unit(Meters & Seconds)
     * @param distance
     * @param waitingTime
     * @param trip
     * @param vehicle
     * @return
     */
    public Double calculatePriceWithWaitingCharges(Double distance,Double waitingTime,TripEntity trip,VehiclesEntity vehicle){

        if(Math.abs(distance-Double.parseDouble(trip.getDistance()))>2000){
            log.warn("predicted distance and actual distance has large difference.TripId:{}|PredictedDistance:{}|ActualDistance:{}|Difference:{}",trip.getTripId(),trip.getDistance(),distance,Math.abs(distance-Double.parseDouble(trip.getDistance())));
        }
        Double distancePrice = distance*vehicle.getFlatRate();
        Double waitingCharges = waitingTime*vehicle.getWaitingRate();
        log.info("DistancePrice:{}|WaitingCharges:{}|TotalPrice:{}",distancePrice,waitingCharges,distancePrice+waitingCharges);
        return distancePrice+waitingCharges;
    }
}
