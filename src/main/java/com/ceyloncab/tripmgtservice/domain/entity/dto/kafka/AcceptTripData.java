package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class AcceptTripData{
    private String driverId;
    private String driverName;
    private String mobileNumber;
    private String vehicleNumber;
    private String profilePicRef;
    private Double price;
    private String tripId;
}
