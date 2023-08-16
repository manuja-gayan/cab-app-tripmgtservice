package com.ceyloncab.tripmgtservice.application.transport.request;

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
public class RevenueHistoryRequest {
    @LastModifiedDate
    @NotNull(message = "startDate not found for operation. This action is not allowed")
    @Valid
    private Date startDate;
    @NotNull(message = "endDate not found for operation. This action is not allowed")
    @Valid
    @LastModifiedDate
    private Date endDate;
    private String queryType;
}

