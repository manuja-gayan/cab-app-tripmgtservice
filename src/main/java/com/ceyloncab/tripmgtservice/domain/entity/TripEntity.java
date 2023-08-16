package com.ceyloncab.tripmgtservice.domain.entity;

import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.Location;
import com.ceyloncab.tripmgtservice.domain.utils.PaymentType;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Trip")
public class TripEntity {
    @Id
    private String id;
    private String tripId;
    private String customerId;
    private String driverId="NOT_ASSIGNED";
    private TripStatus status;
    private String vehicleNumber="NOT_ASSIGNED";
    private String customerName;
    private String driverName="NOT_ASSIGNED";
    private String driverNumber="NOT_ASSIGNED";
    private String customerNumber;
    private String secondaryNumber;
    private Double draftPrice;
    private Double originalTripPrice;
    private Double netPrice=0.0;
    private Double driverPrice=0.0;
    private String distance;
    private Long estimatedTime;
    private String tripBeginTime= LocalDateTime.now().toString();
    private String tripTime="NOT_ASSIGNED";
    private String tripEndTime="NOT_ASSIGNED";
    private Double avgSpeed=0.0;
    private String uuid;
    private Location start;
    private List<Location> midStops;
    private Location destination;
    private String vehicleType;
    private String note;
    private String promoCode;
    private PaymentType paymentType;
    @LastModifiedDate
    private Date updatedTime;

    public TripEntity(String tripId, String customerId, TripStatus status, String customerName, String customerNumber,
                      String secondaryNumber, Double draftPrice, Double originalTripPrice, String distance, String estimatedTime,
                      String uuid, Location start, List<Location> midStops, Location destination, String vehicleType,
                      String note, String promoCode, PaymentType paymentType) {
        this.tripId = tripId;
        this.customerId = customerId;
        this.status = status;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.secondaryNumber = secondaryNumber;
        this.draftPrice = draftPrice;
        this.originalTripPrice = originalTripPrice;
        this.distance = distance;
        this.estimatedTime = Long.parseLong(estimatedTime);
        this.uuid = uuid;
        this.start = start;
        this.midStops = midStops;
        this.destination = destination;
        this.vehicleType = vehicleType;
        this.note = note;
        this.promoCode = promoCode;
        this.paymentType = paymentType;

    }
}
