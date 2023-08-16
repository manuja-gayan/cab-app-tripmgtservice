package com.ceyloncab.tripmgtservice.application.transport.request;

import com.ceyloncab.tripmgtservice.domain.utils.Pagination;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripsRequest {
    @NotNull(message = "distance not found for operation. This action is not allowed")
    @Valid
    private TripStatus status;
    @NotNull(message = "pagination not found for operation. This action is not allowed")
    @Valid
    private Pagination pagination;


}
