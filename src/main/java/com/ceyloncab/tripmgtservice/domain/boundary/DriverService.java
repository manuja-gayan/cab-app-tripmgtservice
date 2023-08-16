package com.ceyloncab.tripmgtservice.domain.boundary;

import com.ceyloncab.tripmgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.utils.VehicleType;

import java.util.List;

public interface DriverService {
    Object getDriverProfile(String driverId);

    List<DriverStatusEntity> getNearestDrivers(Location pickupPoint, String vehicleType, Integer driverCount);
}
