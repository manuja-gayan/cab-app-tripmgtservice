package com.ceyloncab.tripmgtservice.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionHistoryResponseDTO {

    private String tripId;

    private String customerId;

    private String driverId;

    private String driverName;

    private Double revenue;

    private Double commission;
}
