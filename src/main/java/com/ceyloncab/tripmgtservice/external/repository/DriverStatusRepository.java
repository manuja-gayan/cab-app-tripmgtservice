package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.DriverStatusEntity;
import com.ceyloncab.tripmgtservice.domain.utils.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverStatusRepository extends MongoRepository<DriverStatusEntity,String> {
//    GeoResults<DriverStatusEntity> findByLocationNearAndVehicleType(Point point, Distance distance, VehicleType vehicleType, Pageable pageable);
    Page<DriverStatusEntity> findByVehicleTypeAndStatusAndLocationNear(String vehicleType,String status, Point point, Distance distance, Pageable pageable);
    DriverStatusEntity findOneByDriverId(String driverId);
}
