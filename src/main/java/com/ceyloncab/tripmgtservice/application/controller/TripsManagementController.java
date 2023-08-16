package com.ceyloncab.tripmgtservice.application.controller;

import com.ceyloncab.tripmgtservice.application.transport.request.CompletedTripsByBetweenDateRequest;
import com.ceyloncab.tripmgtservice.application.transport.request.CompletedTripsByDateRequest;
import com.ceyloncab.tripmgtservice.application.transport.request.TripsRequest;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.TripResponseWithTotal;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewCustomerDto;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewDriverDto;
import com.ceyloncab.tripmgtservice.domain.service.TripManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("${base-url.context}/getTrips")
public class TripsManagementController extends BaseController{

    @Autowired
    TripManagementService tripManagementService;

   @PostMapping (value = "/completed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getCompletedTrips(@RequestBody(required = true)TripsRequest createRequest, HttpServletRequest request)throws Exception{

       CommonResponse response = tripManagementService.getCompletedTrips(createRequest);
       return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @PostMapping(value = "/ongoing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getOngoingTrips(@RequestBody(required = true)TripsRequest createRequest, HttpServletRequest request)throws Exception{
        CommonResponse response = tripManagementService.getOngoingTrips(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @PostMapping(value = "/customer/canceled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getCustomerCanceledTrips(@RequestBody(required = true)TripsRequest createRequest, HttpServletRequest request)throws Exception{
        CommonResponse<List<TripsViewCustomerDto>> response = tripManagementService.getCustomerCanceledTrips(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/driver/canceled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getDriverCanceledTrips(@RequestBody(required = true)TripsRequest createRequest, HttpServletRequest request)throws Exception{
        CommonResponse<List<TripsViewDriverDto>> response = tripManagementService.getDriverCanceledTrips(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/driver/completedByBetweenDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getDriverCompletedTripsByBetweenDate(@RequestBody(required = true) CompletedTripsByBetweenDateRequest createRequest, HttpServletRequest request)throws Exception{
        TripResponseWithTotal<List<TripsViewDriverDto>> response = tripManagementService.getDriverCompletedTripsByBetweenTimeBetweenDate(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/driver/completedByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getDriverCompletedTripsByDate(@RequestBody(required = true) CompletedTripsByDateRequest createRequest, HttpServletRequest request)throws Exception{
        TripResponseWithTotal<List<TripsViewDriverDto>> response = tripManagementService.getDriverCompletedTripsByTimeDate(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value = "/customer/completedByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>getCustomerCompletedTripsByDate(@RequestBody(required = true) CompletedTripsByBetweenDateRequest createRequest, HttpServletRequest request)throws Exception{
        CommonResponse<List<TripsViewCustomerDto>> response = tripManagementService.getCustomerCompletedTripsByDate(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

}
