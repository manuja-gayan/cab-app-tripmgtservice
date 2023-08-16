package com.ceyloncab.tripmgtservice.domain.entity;

import com.ceyloncab.tripmgtservice.domain.utils.DriverStatus;
import com.ceyloncab.tripmgtservice.domain.utils.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "DriverStatus")
public class DriverStatusEntity {
    @Id
    private String id;
    private String status;
    private String driverId;
    private VehicleType vehicleType;
    @GeoSpatialIndexed(name = "location", type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    private Double avgStatus;
    @LastModifiedDate
    private Date updatedTime;
    @DocumentReference(lazy = true)
    private DriverEntity driver;
}
