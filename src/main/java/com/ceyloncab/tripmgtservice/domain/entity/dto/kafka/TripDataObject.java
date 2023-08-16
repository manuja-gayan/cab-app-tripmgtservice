package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripDataObject {
    private Location start;
    private List<Location> midStops;
    private Location destination;
}
