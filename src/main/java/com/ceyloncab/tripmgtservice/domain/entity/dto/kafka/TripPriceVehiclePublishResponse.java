package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripPriceVehiclePublishResponse {
    private String status;
    private UserInfo userInfo;
    private List<TripPriceForAllVehiclesPublish> data;
    private ResHeaderPublish responseHeader;
}
