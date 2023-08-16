package com.ceyloncab.tripmgtservice.domain.entity.dto;

import com.ceyloncab.tripmgtservice.domain.utils.VehicleCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummeryResponseDTO {
    private List<VehicleCount> vehicles;

    private int customers;

    private int pendingDrivers;

    private int activeDrivers;

    private int ongoingTrips;

    private int completedTrips;

    private int cancelledTrips;

    private Double revenue;

    private Double commission;
}
