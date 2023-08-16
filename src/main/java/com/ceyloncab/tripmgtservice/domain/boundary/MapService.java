package com.ceyloncab.tripmgtservice.domain.boundary;

import com.ceyloncab.tripmgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.DistanceAndTime;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;

import java.util.List;
import java.util.Map;

public interface MapService {
    Map<String, Long> getDriversSortWithTime(List<DriverStatusEntity> drivers, Location pickupPoint);

    DistanceAndTime getDistanceAndTime(Location origin, Location destination);
}
