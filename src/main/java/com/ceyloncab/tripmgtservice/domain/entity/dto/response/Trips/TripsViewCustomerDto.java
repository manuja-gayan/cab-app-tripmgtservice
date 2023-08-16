package com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.utils.PaymentType;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripsViewCustomerDto {
    private String tripId;
    private String driverName;
    private String driverNumber;
    private String customerName;
    private String customerNumber;
    private String tripBeginTime;
    private String tripEndTime;
    private Double netPrice;
    private Double avgSpeed;
    private String vehicleNumber;
    private String vehicleType;
    private Location start;
    private TripStatus status;
    private String  distance;
    private Long estimatedTime;
    private PaymentType paymentType;
}
