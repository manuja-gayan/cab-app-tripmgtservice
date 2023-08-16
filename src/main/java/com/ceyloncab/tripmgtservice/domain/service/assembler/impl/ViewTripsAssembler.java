//package com.ceyloncab.tripmgtservice.domain.service.assembler.impl;
//
//import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
//import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewDto;
//import com.ceyloncab.tripmgtservice.domain.service.assembler.Assembler;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ViewTripsAssembler implements Assembler<TripEntity, TripsViewDto> {
//
//    @Override
//    public TripEntity fromDto(TripsViewDto dto) {
//        return null;
//    }
//
//    @Override
//    public TripsViewDto toDto(TripEntity model) {
//        return new TripsViewDto(model.getTripId(), model.getFullName(), model.getMobilNumber(),model.getPickupTime(),
//                model.getDropTime(), model.getAvgSpeed(), model.getVehicleId(), model.getTrip(), model.getStatus(), model.getDistance());
//    }
//
//    @Override
//    public TripsViewDto toDto(TripEntity model, Object object) {
//        return null;
//    }
//}
