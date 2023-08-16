package com.ceyloncab.tripmgtservice.application.transport.request;

import com.ceyloncab.tripmgtservice.domain.utils.Pagination;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedTripsByDateRequest extends TripsRequest{
    private TripStatus status;
    @LastModifiedDate
    @NotNull(message = "startDate not found for operation. This action is not allowed")
    @Valid
    private Date startDate;
    @NotNull(message = "pagination not found for operation. This action is not allowed")
    @Valid
    private Pagination pagination;

}
