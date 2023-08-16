package com.ceyloncab.tripmgtservice.domain.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCount {
    private String type;
    private int tripCount;
}
