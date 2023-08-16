package com.ceyloncab.tripmgtservice.domain.service.assembler.impl;

import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewCustomerDto;
import com.ceyloncab.tripmgtservice.domain.service.assembler.Assembler;
import org.springframework.stereotype.Service;

@Service
public class ViewCustomerTripsAssembler implements Assembler<TripEntity, TripsViewCustomerDto> {

    @Override
    public TripEntity fromDto(TripsViewCustomerDto dto) {
        return null;
    }

    @Override
    public TripsViewCustomerDto toDto(TripEntity model) {
        return new TripsViewCustomerDto( model.getTripId(), model.getDriverName(), model.getDriverNumber(), model.getCustomerName(),model.getCustomerNumber(),
             model.getTripBeginTime(),model.getTripEndTime(), model.getNetPrice(), model.getAvgSpeed(),model.getVehicleNumber(), model.getVehicleType(),
             model.getStart(),model.getStatus(),model.getDistance(), model.getEstimatedTime(), model.getPaymentType());
    }

 /*   @Override
    public TripsViewDto toDto(TripEntity model) {
        return new TripsViewDto(model.getId(), model.getTripId(), model.getCustomerId(),model.getDriverId(),model.getStatus(),model.getVehicleNumber(),
                model.getCustomerName(), model.getDriverName(), model.getDriverNumber(),model.getCustomerNumber(), model.getSecondaryNumber(),
                model.getDraftPrice(),model.getOriginalTripPrice(),model.getNetPrice(),model.getDriverPrice(),model.getDistance(),
                model.getEstimatedTime(),model.getTripBeginTime(),model.getTripTime(),model.getTripEndTime(),model.getAvgSpeed(),
                model.getUuid(),model.getStart(),model.getMidStops(),model.getDestination(),model.getVehicleType(),model.getNote(),model.getPromoCode(),
                model.getPaymentType(),model.getUpdatedTime());
    }
*/
    @Override
    public TripsViewCustomerDto toDto(TripEntity model, Object object) {
        return null;
    }
}
