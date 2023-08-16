package com.ceyloncab.tripmgtservice.domain.service.assembler.impl;

import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewCustomerDto;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewDriverDto;
import com.ceyloncab.tripmgtservice.domain.service.assembler.Assembler;
import org.springframework.stereotype.Service;

@Service
public class ViewDriverTripsAssembler implements Assembler<TripEntity, TripsViewDriverDto> {
    @Override
    public TripEntity fromDto(TripsViewDriverDto dto) {
        return null;
    }

    @Override
    public TripsViewDriverDto toDto(TripEntity model) {
        return  new TripsViewDriverDto( model.getTripId(), model.getDriverName(), model.getDriverNumber(), model.getCustomerName(),model.getCustomerNumber(),
                model.getTripBeginTime(),model.getTripEndTime(), model.getNetPrice(), model.getAvgSpeed(),model.getVehicleNumber(), model.getVehicleType(),
                model.getStart(),model.getStatus(),model.getDistance(), model.getEstimatedTime(), model.getPaymentType());
    }

    @Override
    public TripsViewDriverDto toDto(TripEntity model, Object object) {
        return null;
    }
}
