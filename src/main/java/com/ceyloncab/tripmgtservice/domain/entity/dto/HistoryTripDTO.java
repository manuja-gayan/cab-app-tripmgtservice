package com.ceyloncab.tripmgtservice.domain.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTripDTO {
   private Double revenue;
   private Double commission;
   private String driverName;
}
