package com.ceyloncab.tripmgtservice.external.serviceimpl;

import com.ceyloncab.tripmgtservice.domain.boundary.MapService;
import com.ceyloncab.tripmgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.DistanceAndTime;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.exception.IgnorableException;
import com.ceyloncab.tripmgtservice.domain.utils.UtilityService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
@Service
public class GoogleMapServiceImpl implements MapService {

    @Value(value = "${google-maps.api-key}")
    private String apiKey;

//    private static final GeoApiContext context = new GeoApiContext.Builder()
//            .apiKey(apiKey)
//            .build();


    @Override
    public Map<String, Long> getDriversSortWithTime(List<DriverStatusEntity> drivers, Location pickupPoint) {
        int driverCount = drivers.size();
        Map<String, Long> driverTimeMap = new LinkedHashMap<>();
        LatLng[] latlong = new LatLng[drivers.size()];
        try {
            // generate url context related to API_KEY
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            for (int i = 0; i < driverCount; i++) {
                latlong[i] = new LatLng(drivers.get(i).getLocation().getCoordinates().get(0), drivers.get(i).getLocation().getCoordinates().get(1));
            }

            DistanceMatrix results = DistanceMatrixApi.newRequest(context)
                    .origins(latlong)
                    .destinations(new LatLng(Double.parseDouble(pickupPoint.getLatitude()), Double.parseDouble(pickupPoint.getLongitude())))
                    .await();

            DistanceMatrixRow[] rows = results.rows;
            context.shutdown();
            for (int j = 0; j < driverCount; j++) {
                driverTimeMap.put(drivers.get(j).getDriverId(), rows[j].elements[0].duration.inSeconds);
            }
            return UtilityService.sortByValue(driverTimeMap);
        } catch (Exception ex) {
            log.error("google map access failure:{}", ex.getMessage(), ex);
            throw new IgnorableException("google map access failure:{}"+ex.getMessage());
        }
    }

    @Override
    public DistanceAndTime getDistanceAndTime(Location origin, Location destination) {
        try {
            // generate url context related to API_KEY
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();

            DistanceMatrix results = DistanceMatrixApi.newRequest(context)
                    .origins(new LatLng(Double.parseDouble(origin.getLatitude()), Double.parseDouble(origin.getLongitude())))
                    .destinations(new LatLng(Double.parseDouble(destination.getLatitude()), Double.parseDouble(destination.getLongitude())))
                    .await();

            DistanceMatrixElement element = results.rows[0].elements[0];
            context.shutdown();
            return new DistanceAndTime(element.distance.humanReadable,element.duration.humanReadable,
                    element.distance.inMeters,element.duration.inSeconds);
        } catch (Exception ex) {
            log.error("google map access failure:{}", ex.getMessage(), ex);
            throw new IgnorableException("google map access failure:{}"+ex.getMessage());
        }
    }
}
