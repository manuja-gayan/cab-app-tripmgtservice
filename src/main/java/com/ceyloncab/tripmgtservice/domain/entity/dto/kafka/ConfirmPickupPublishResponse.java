package com.ceyloncab.tripmgtservice.domain.entity.dto.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmPickupPublishResponse {
    private String status;
    private UserInfo userInfo;
    private ConfirmPickupData data;
    private ResHeaderPublish responseHeader;
}
