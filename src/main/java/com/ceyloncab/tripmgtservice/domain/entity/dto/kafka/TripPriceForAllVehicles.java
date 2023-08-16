package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.TripDataObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripPriceForAllVehicles {
    private String userId;
    private String channel;
    private String uuid;
    private List<TripDataObject> tripData;
}
