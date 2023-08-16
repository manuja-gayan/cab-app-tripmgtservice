package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndTrip{
    private String userId;
    private String channel;
    private String uuid;
    private String status;
    private String tripId;
    private Double distance;
    private Double waitingTime = 0.0;
    private String tripTime;
    private Double avgSpeed;
}
