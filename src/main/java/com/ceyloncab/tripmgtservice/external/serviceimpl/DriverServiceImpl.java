package com.ceyloncab.tripmgtservice.external.serviceimpl;

import com.ceyloncab.tripmgtservice.domain.boundary.DriverService;
import com.ceyloncab.tripmgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.utils.VehicleType;
import com.ceyloncab.tripmgtservice.external.repository.DriverStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**;
 *
 */
@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    @Value(value = "${search.range}")
    private Integer searchRange;

    @Autowired
    private DriverStatusRepository driverStatusRepository;

    @Override
    public Object getDriverProfile(String driverId){
        return new Object();
    }

    @Override
    public List<DriverStatusEntity> getNearestDrivers(Location pickupPoint, String vehicleType, Integer driverCount){

        Point pickup = new Point(Double.parseDouble(pickupPoint.getLatitude()),Double.parseDouble(pickupPoint.getLongitude()));
        Pageable pageable = PageRequest.of(0,driverCount);
//        GeoResults<DriverStatusEntity> drivers = driverStatusRepository.findByLocationNearAndVehicleType(pickup,new Distance(1, Metrics.KILOMETERS),vehicleType,pageable);
//        ArrayList<DriverStatusEntity> driverArray = new ArrayList<>();
//        for (GeoResult<DriverStatusEntity> geoResult: drivers) {
//            driverArray.add(geoResult.getContent());
//        }
        Page<DriverStatusEntity> drivers = driverStatusRepository.findByVehicleTypeAndStatusAndLocationNear(vehicleType,"IDLE",pickup,new Distance(searchRange, Metrics.KILOMETERS),pageable);
        return new ArrayList<>(drivers.getContent());
    }
}
