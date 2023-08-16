package com.ceyloncab.tripmgtservice.domain.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistanceAndTime {
    private String distanceTxt;
    private String timeTxt;
    private Long distance;
    private Long time;
}
