package com.ceyloncab.tripmgtservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Vehicles")
public class VehiclesEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String type;
    private String displayName;
    private Integer passengers;
    private Double commissionRate;
    private Double waitingRate;
    private Double startingRate;
    private Double flatRate;
    private Boolean isActive = true;
    @LastModifiedDate
    private Date updatedTime;
}
