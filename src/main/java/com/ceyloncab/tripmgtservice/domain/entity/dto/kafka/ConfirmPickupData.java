package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ConfirmPickupData extends ConfirmPickup{
    private String customerName;
    private String mobileNumber;
    private String distance;
    private String estimatedTime;
    private Double price;
}
