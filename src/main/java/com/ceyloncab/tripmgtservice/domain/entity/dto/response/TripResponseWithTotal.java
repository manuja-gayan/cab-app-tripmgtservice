package com.ceyloncab.tripmgtservice.domain.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripResponseWithTotal<T> {
    private String total="10000";
    private T data;
    private ResponseHeader responseHeader;
}
