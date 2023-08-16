package com.ceyloncab.tripmgtservice.domain.service;

import com.ceyloncab.tripmgtservice.application.aop.AopConstants;
import com.ceyloncab.tripmgtservice.application.transport.request.TripsRequest;
import com.ceyloncab.tripmgtservice.application.transport.request.CompletedTripsByBetweenDateRequest;
import com.ceyloncab.tripmgtservice.application.transport.request.CompletedTripsByDateRequest;
import com.ceyloncab.tripmgtservice.domain.boundary.CustomerService;
import com.ceyloncab.tripmgtservice.domain.boundary.KafkaProducerService;
import com.ceyloncab.tripmgtservice.domain.boundary.MapService;
import com.ceyloncab.tripmgtservice.domain.entity.*;
import com.ceyloncab.tripmgtservice.domain.entity.dto.DistanceAndTime;
import com.ceyloncab.tripmgtservice.domain.entity.dto.kafka.*;
//import com.ceyloncab.tripmgtservice.domain.service.assembler.impl.ViewTripsAssembler;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.TripResponseWithTotal;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewCustomerDto;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.Trips.TripsViewDriverDto;
import com.ceyloncab.tripmgtservice.domain.exception.DomainException;
import com.ceyloncab.tripmgtservice.domain.service.assembler.impl.ViewCustomerTripsAssembler;
import com.ceyloncab.tripmgtservice.domain.service.assembler.impl.ViewDriverTripsAssembler;
import com.ceyloncab.tripmgtservice.domain.utils.Constants;
import com.ceyloncab.tripmgtservice.domain.utils.DriverStatus;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import com.ceyloncab.tripmgtservice.domain.utils.UtilityService;
import com.ceyloncab.tripmgtservice.external.repository.*;
import com.ceyloncab.tripmgtservice.domain.utils.Constants;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import com.ceyloncab.tripmgtservice.external.repository.CustomerRepository;
import com.ceyloncab.tripmgtservice.external.repository.DriverRepository;
import com.ceyloncab.tripmgtservice.external.repository.TripRepository;
import com.ceyloncab.tripmgtservice.external.repository.VehicleRepository;
import com.ceyloncab.tripmgtservice.external.serviceimpl.DriverServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class TripManagementService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private DriverServiceImpl driverService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private MapService mapService;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    UtilityService utilityService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    ViewCustomerTripsAssembler viewTripsAssembler;

    @Autowired
    ViewDriverTripsAssembler viewDriverTripsAssembler;

    @Autowired
    DriverStatusRepository driverStatusRepository;

    private final String DRIVER_ACCEPT = "DRIVER_ACCEPT";
    private final String CUSTOMER_CANCELLED = "CUSTOMER_CANCELLED";
    private final String DRIVER_CANCELLED = "DRIVER_CANCELLED";
    private final String START_TRIP = "STARTED";
    private final String END_TRIP = "END";

//    @Autowired
//    ViewTripsAssembler viewTripsAssembler;

    public void getTripPriceForAllVehicles(TripPriceForAllVehicles tripPriceForAllVehicles){

        if(Objects.isNull(tripPriceForAllVehicles.getTripData())){
            log.error("Trip Data is null. Cannot process request.UserId:{}|UUID:{}",tripPriceForAllVehicles.getUserId(),tripPriceForAllVehicles.getUuid());
            return;
        }

        TripDataObject tripDataObject = tripPriceForAllVehicles.getTripData().get(0);
        DistanceAndTime distanceAndTime = getDistanceTimeWithMiddlePoints(tripDataObject.getStart(),tripDataObject.getMidStops(),tripDataObject.getDestination());
        List<VehiclesEntity> vehicles = vehicleRepository.findAll();

        TripPriceVehiclePublishResponse response = new TripPriceVehiclePublishResponse();
        List<TripPriceForAllVehiclesPublish> data = new ArrayList<>();

        for (VehiclesEntity vehicle : vehicles) {
           /* List<DriverStatusEntity> drivers = driverService.getNearestDrivers(priceRequest.getTripData().get(0).getStart(), vehicle.getType(),1);*/
          /*  List<DriverStatusEntity> drivers = driverService.getNearestDrivers(priceRequest.getTripData().get(0).getStart(), vehicle.getType(),1);
            if(!drivers.isEmpty()){
                // crete response
                List<Double> coordinates = drivers.get(0).getLocation().getCoordinates();
                Location location = new Location("",String.valueOf(coordinates.get(0)),String.valueOf(coordinates.get(1)));
                DistanceAndTime time = mapService.getDistanceAndTime(location,priceRequest.getTripData().get(0).getStart());
                TripPriceForAllVehiclesPublish tripData = new TripPriceForAllVehiclesPublish(vehicle.getType(), time.getTimeTxt(),
                        utilityService.calculatePrice(priceRequest.getTripData().get(0).getStart(),
                        distanceAndTime, LocalDateTime.now(),vehicle));
                data.add(tripData);
            }*/

            List<DriverStatusEntity> drivers = driverService.getNearestDrivers(tripPriceForAllVehicles.getTripData().get(0).getStart(), vehicle.getType(),1);
                TripPriceForAllVehiclesPublish tripData = new TripPriceForAllVehiclesPublish(vehicle.getType(), "N/A",
                        utilityService.calculatePrice(tripPriceForAllVehicles.getTripData().get(0).getStart(),
                                distanceAndTime, LocalDateTime.now(),vehicle));
                data.add(tripData);
        }
        UserInfo userInfo = new UserInfo(Collections.singletonList(tripPriceForAllVehicles.getUserId()), tripPriceForAllVehicles.getChannel(), tripPriceForAllVehicles.getUuid());
        response.setUserInfo(userInfo);
        response.setData(data);
        response.setStatus("VEHICLE_TRIP_PRICE");
        response.setResponseHeader(new ResHeaderPublish("TMS1000","Success","200"));
        log.info("Submitted to kafka.UserId:{}|UUID:{}|Data:{}",tripPriceForAllVehicles.getUserId(),tripPriceForAllVehicles.getUuid(),response);
        //publish to KAFKA
        kafkaProducerService.publishMsgToTopic(Constants.TOPIC_VEHICLE_DATA, response);
    }

    public void searchDriversAndNotify(ConfirmPickup confirmPickup) {
//        GeoJsonPoint point = new GeoJsonPoint(1,1);
//        driverStatusRepository.save(new DriverStatusEntity(null,"IDLE","637fa67efe20a74a2ec29b18", VehicleType.CAR,point,2.0,new Date(),driverRepository.findOneByMsisdn("saman").get()));
        ConfirmPickupPublishResponse response = new ConfirmPickupPublishResponse();
        Optional<CustomerEntity> customerOptional = customerService.getCustomerProfile(confirmPickup.getUserId());
        if(!customerOptional.isPresent()){
            log.error("User not found.Cannot proceed the request.User:".concat(confirmPickup.getUserId()));
            return;
        }
        CustomerEntity customer = customerOptional.get();

        Optional<VehiclesEntity> vehicle = vehicleRepository.findOneByType(confirmPickup.getVehicleType().toString());
        if(!vehicle.isPresent()){
            log.error("VehicleType not found.Cannot proceed the request.VehicleType:{}",confirmPickup.getVehicleType().toString());
            return;
        }

        List<DriverStatusEntity> drivers = driverService.getNearestDrivers(confirmPickup.getStart(), confirmPickup.getVehicleType().toString(), 3);
        UserInfo userInfo;
        if(Objects.isNull(drivers) || drivers.isEmpty()){
           // drivers not found flow
            response.setResponseHeader(new ResHeaderPublish(Constants.ResponseData.NO_DRIVERS_FOUND.getCode(),
                    Constants.ResponseData.NO_DRIVERS_FOUND.getMessage(),Constants.ResponseData.NO_DRIVERS_FOUND.getResponseCode()));
            userInfo = new UserInfo(Collections.singletonList(confirmPickup.getUserId()), confirmPickup.getChannel(), confirmPickup.getUuid());
            response.setUserInfo(userInfo);
        }else {
            //sort drivers based on arrival time
            Map<String, Long> driversWithTime = mapService.getDriversSortWithTime(drivers, confirmPickup.getStart());
            DistanceAndTime distanceAndTime = getDistanceTimeWithMiddlePoints(confirmPickup.getStart(),confirmPickup.getMidStops(),confirmPickup.getDestination());

            ConfirmPickupData data = new ConfirmPickupData(customer.getFirstName() + " " + customer.getLastName(), customer.getMsisdn(),
                    String.valueOf(distanceAndTime.getDistance()), String.valueOf(distanceAndTime.getTime()), utilityService.calculatePrice(confirmPickup.getStart(),distanceAndTime,LocalDateTime.now(),vehicle.get()));
            data.setTripId(confirmPickup.getTripId());
            data.setUuid(confirmPickup.getUuid());
            data.setStart(confirmPickup.getStart());
            data.setDestination(confirmPickup.getDestination());
            data.setMidStops(confirmPickup.getMidStops());
            data.setNote(confirmPickup.getNote());
            data.setPaymentType(confirmPickup.getPaymentType());

            userInfo = new UserInfo(new ArrayList<>(driversWithTime.keySet()), confirmPickup.getChannel(), confirmPickup.getUuid());
            response.setUserInfo(userInfo);
            response.setData(data);
            response.setResponseHeader(new ResHeaderPublish("TMS1000","Success","200"));
            //save trip data
            initiateTripEntity(confirmPickup,customer,response.getData(),TripStatus.PENDING);
        }
        response.setStatus("CONFIRM_PICKUP");
        kafkaProducerService.publishMsgToTopic(Constants.TOPIC_TRIP_DATA, response);
    }

    public void updateTripStatus(UpDateStatusOnTrip updateTripStatus) {
        switch (updateTripStatus.getStatus()){
            case START_TRIP:
                performTripStart(updateTripStatus);
                break;
            case DRIVER_ACCEPT:
                performDriverAcceptTrip(updateTripStatus);
                break;
            case DRIVER_CANCELLED:
            case CUSTOMER_CANCELLED:
            default:
                performCancelTripByDriverOrCustomer(updateTripStatus);
        }
    }

    public CommonResponse<List<TripsViewCustomerDto>> getCompletedTrips(TripsRequest request) throws Exception {
        CommonResponse<List<TripsViewCustomerDto>> response = new CommonResponse<>();
        List<TripsViewCustomerDto> tripsList = new ArrayList<>();

        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        List<TripEntity> tripsEntities = customer.getTripEntityList();
        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());

        for ( TripEntity tripEntity1 : byStatus) {
            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public CommonResponse<List<TripsViewCustomerDto>> getOngoingTrips(TripsRequest request) throws Exception {
        CommonResponse<List<TripsViewCustomerDto>> response = new CommonResponse<>();
        List<TripsViewCustomerDto> tripsList = new ArrayList<>();
        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });
        List<TripEntity> tripsEntities = customer.getTripEntityList();
        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());

        for ( TripEntity tripEntity1 : byStatus) {
            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
        }

            response.setData(tripsList);
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
            return response;
        }

    public CommonResponse<List<TripsViewCustomerDto>> getCustomerCanceledTrips(TripsRequest request) throws Exception {
        CommonResponse<List<TripsViewCustomerDto>> response = new CommonResponse<>();
        List<TripsViewCustomerDto> tripsList = new ArrayList<>();
        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        List<TripEntity> tripsEntities = customer.getTripEntityList();
        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());

        for ( TripEntity tripEntity1 : byStatus) {
            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public CommonResponse<List<TripsViewDriverDto>> getDriverCanceledTrips(TripsRequest request) throws Exception {
        CommonResponse<List<TripsViewDriverDto>> response = new CommonResponse<>();
        List<TripsViewDriverDto> tripsList = new ArrayList<>();
        DriverEntity driver =driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });

        List<TripEntity> tripsEntities = driver.getTripEntityList();
        List<TripEntity> byStatus = tripRepository.findByDriverUserIdAndStatus(driver.getUserId(),request.getStatus());

        for ( TripEntity tripEntity1 : byStatus) {
            tripsList.add(viewDriverTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public TripResponseWithTotal<List<TripsViewDriverDto>> getDriverCompletedTripsByBetweenTimeBetweenDate(CompletedTripsByBetweenDateRequest request) throws Exception {
        TripResponseWithTotal<List<TripsViewDriverDto>> response = new TripResponseWithTotal<>();
        List<TripsViewDriverDto> tripsList = new ArrayList<>();
        DriverEntity driver =driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });

        Date startDate=request.getStartDate();
        Date endDate=request.getEndDate();
        TripStatus status=request.getStatus();

        List<TripEntity> tripsEntities = driver.getTripEntityList();
        List<TripEntity> byUpdatedTimeBetween = tripRepository.findByDriverUserIdAndUpdatedTimeBetweenAndStatus(driver.getUserId(), startDate, endDate, status );

       for ( TripEntity tripEntity1 : byUpdatedTimeBetween) {
                tripsList.add(viewDriverTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public TripResponseWithTotal<List<TripsViewDriverDto>> getDriverCompletedTripsByTimeDate(CompletedTripsByDateRequest request) throws Exception {
        TripResponseWithTotal<List<TripsViewDriverDto>> response = new TripResponseWithTotal<>();
        List<TripsViewDriverDto> tripsList = new ArrayList<>();
        DriverEntity driver =driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
                });

        Date startDate=request.getStartDate();
        TripStatus status=request.getStatus();


//    public CommonResponse<List<TripsViewDto>> getCompletedTrips(TripsRequest request) throws Exception {
//        CommonResponse<List<TripsViewDto>> response = new CommonResponse<>();
//        List<TripsViewDto> tripsList = new ArrayList<>();
//
//        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
//                .orElseThrow(() -> {
//                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
//                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
//                });
//
//        List<TripEntity> tripsEntities = customer.getTripEntityList();
//        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());
//
//        for ( TripEntity tripEntity1 : byStatus) {
//            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
//        }
//
//        response.setData(tripsList);
//        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
//        return response;
//    }
//
//    public CommonResponse<List<TripsViewDto>> getOngoingTrips(TripsRequest request) throws Exception {
//        CommonResponse<List<TripsViewDto>> response = new CommonResponse<>();
//        List<TripsViewDto> tripsList = new ArrayList<>();
//        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
//                .orElseThrow(() -> {
//                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
//                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
//                });
//        List<TripEntity> tripsEntities = customer.getTripEntityList();
//        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());
//
//        for ( TripEntity tripEntity1 : byStatus) {
//            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
//        }
//
//            response.setData(tripsList);
//            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
//            return response;
//        }
//
//    public CommonResponse<List<TripsViewDto>> getCustomerCanceledTrips(TripsRequest request) throws Exception {
//        CommonResponse<List<TripsViewDto>> response = new CommonResponse<>();
//        List<TripsViewDto> tripsList = new ArrayList<>();
//        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
//                .orElseThrow(() -> {
//                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
//                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
//                });
//
//        List<TripEntity> tripsEntities = customer.getTripEntityList();
//        List<TripEntity> byStatus = tripRepository.findByCustomerUserIdAndStatus(customer.getUserId(),request.getStatus());
//
//        for ( TripEntity tripEntity1 : byStatus) {
//            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
//        }
//
//        response.setData(tripsList);
//        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
//        return response;
//    }
//
//    public CommonResponse<List<TripsViewDto>> getDriverCanceledTrips(TripsRequest request) throws Exception {
//        CommonResponse<List<TripsViewDto>> response = new CommonResponse<>();
//        List<TripsViewDto> tripsList = new ArrayList<>();
//        DriverEntity driver =driverRepository.findById(MDC.get(AopConstants.MDC_USERID))
//                .orElseThrow(() -> {
//                    log.error("Driver does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
//                    return new DomainException(Constants.ResponseData.DRIVER_NOT_FOUND);
//                });
//
//        List<TripEntity> tripsEntities = driver.getTripEntityList();
//        List<TripEntity> byStatus = tripRepository.findByDriverUserIdAndStatus(driver.getUserId(),request.getStatus());
//
//        for ( TripEntity tripEntity1 : byStatus) {
//            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
//        }
//
//        response.setData(tripsList);
//        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
//        return response;
//    }

        List<TripEntity> tripsEntities = driver.getTripEntityList();
        List<TripEntity> byUpdatedTime = tripRepository.findByDriverUserIdAndUpdatedTimeAndStatus(driver.getUserId(), startDate, status );

        for ( TripEntity tripEntity1 : byUpdatedTime) {
            tripsList.add(viewDriverTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public CommonResponse<List<TripsViewCustomerDto>> getCustomerCompletedTripsByDate(CompletedTripsByBetweenDateRequest request) throws Exception {
        CommonResponse<List<TripsViewCustomerDto>> response = new CommonResponse<>();
        List<TripsViewCustomerDto> tripsList = new ArrayList<>();

        CustomerEntity customer = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", MDC.get(AopConstants.MDC_USERID));
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });

        Date startDate=request.getStartDate();
        Date endDate=request.getEndDate();
        TripStatus status=request.getStatus();

        List<TripEntity> tripsEntities = customer.getTripEntityList();
        List<TripEntity> byUpdatedTimeBetween = tripRepository.findByCustomerUserIdAndUpdatedTimeBetweenAndStatus(customer.getUserId(), startDate, endDate, status );

        for ( TripEntity tripEntity1 : byUpdatedTimeBetween) {
            tripsList.add(viewTripsAssembler.toDto(tripEntity1));
        }

        response.setData(tripsList);
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    private void performCancelTripByDriverOrCustomer(UpDateStatusOnTrip updateTripStatus){
        Optional<TripEntity> tripOptional = tripRepository.findOneByTripIdAndStatus(updateTripStatus.getTripId(),"ONGOING");
        if(tripOptional.isPresent()){
            TripEntity trip = tripOptional.get();
            UpdateTripPublishResponse response = new UpdateTripPublishResponse();
            UpdateTripData data = new UpdateTripData(CUSTOMER_CANCELLED.equalsIgnoreCase(updateTripStatus.getStatus())?"Cancelled by customer":"Cancelled by driver",trip.getTripId());
            UserInfo userInfo = new UserInfo(Collections.singletonList(CUSTOMER_CANCELLED.equalsIgnoreCase(updateTripStatus.getStatus())?trip.getDriverId():trip.getCustomerId()), updateTripStatus.getChannel(), updateTripStatus.getUuid());
            response.setUserInfo(userInfo);
            response.setData(data);
            response.setResponseHeader(new ResHeaderPublish("TMS1000","Success","200"));
            response.setStatus(updateTripStatus.getStatus());
            kafkaProducerService.publishMsgToTopic(Constants.TOPIC_TRIP_DATA, response);
            //update trip
            trip.setStatus(CUSTOMER_CANCELLED.equalsIgnoreCase(updateTripStatus.getStatus())?TripStatus.CUSTOMER_CANCELED:TripStatus.DRIVER_CANCELLED);
            //update driver status
            DriverStatusEntity driver = driverStatusRepository.findOneByDriverId(trip.getDriverId());
            driver.setStatus("IDLE");
            driverStatusRepository.save(driver);
            tripRepository.save(trip);
        }else {
            //send error(trip not found) message to requested user
            log.error("send error(trip not found) message to requested user");
        }
    }

    private void performDriverAcceptTrip(UpDateStatusOnTrip acceptTrip){
        Optional<TripEntity> tripOptional = tripRepository.findOneByTripIdAndStatus(acceptTrip.getTripId(),TripStatus.PENDING.toString());
        Optional<DriverEntity> driverOptional = driverRepository.findOneByUserId(acceptTrip.getUserId());
        if(driverOptional.isPresent() && tripOptional.isPresent()){
            TripEntity trip = tripOptional.get();
            DriverEntity driver = driverOptional.get();
            DriverStatusEntity driverStatus = driverStatusRepository.findOneByDriverId(driver.getUserId());
            trip.setDriverId(driver.getUserId());
            trip.setStatus(TripStatus.ONGOING);
            trip.setVehicleNumber(driver.getVehicleNumber());
            trip.setDriverName(driver.getFirstName().concat(" ").concat(driver.getLastName()));
            trip.setDriverNumber(driver.getMsisdn());
            tripRepository.save(trip);
            //updateDriver status
            driverStatus.setStatus("RUNNING");
            driverStatusRepository.save(driverStatus);

            AcceptTripPublishResponse response = new AcceptTripPublishResponse();
            AcceptTripData data = new AcceptTripData(trip.getDriverId(),trip.getDriverName(),trip.getDriverNumber(),
                    trip.getVehicleNumber(),driver.getImgProfile(),trip.getDraftPrice(),trip.getTripId());
            UserInfo userInfo = new UserInfo(Collections.singletonList(trip.getCustomerId()), acceptTrip.getChannel(), acceptTrip.getUuid());
            response.setUserInfo(userInfo);
            response.setData(data);
            response.setResponseHeader(new ResHeaderPublish("TMS1000","Success","200"));
            response.setStatus("DRIVER_ACCEPT");
            kafkaProducerService.publishMsgToTopic(Constants.TOPIC_TRIP_DATA, response);
        }else {
            log.error("send trip not found or driver not found error to requested user");
            //send trip not found or driver not found error to requested user
        }
    }

    private void performTripStart(UpDateStatusOnTrip updateTrip){
        Optional<TripEntity> tripOptional = tripRepository.findOneByTripIdAndStatus(updateTrip.getTripId(),TripStatus.ONGOING.toString());
        if(tripOptional.isPresent() && updateTrip.getUserId().equalsIgnoreCase(tripOptional.get().getDriverId())){
            TripEntity trip = tripOptional.get();
            trip.setTripBeginTime(LocalDateTime.now().toString());
            tripRepository.save(trip);
        }else {
            log.error("send trip not found error to requested user");
            //send trip not found or driver not found error to requested user
        }
    }

    public void performTripEnd(EndTrip endTrip){
        Optional<TripEntity> tripOptional = tripRepository.findOneByTripIdAndStatus(endTrip.getTripId(),TripStatus.ONGOING.toString());
        if(tripOptional.isPresent() && endTrip.getUserId().equalsIgnoreCase(tripOptional.get().getDriverId())){
            TripEntity trip = tripOptional.get();
            Optional<VehiclesEntity> vehicleOpt = vehicleRepository.findOneByType(trip.getVehicleType());
            if(vehicleOpt.isPresent()){
                Double tripPrice = utilityService.calculatePriceWithWaitingCharges(endTrip.getDistance(),
                        endTrip.getWaitingTime(),trip,vehicleOpt.get());
                trip.setTripEndTime(LocalDateTime.now().toString());
                trip.setTripTime(String.valueOf(ChronoUnit.MINUTES.between(LocalDateTime.parse(trip.getTripBeginTime()),LocalDateTime.now())).concat(" Minutes"));
                trip.setAvgSpeed(endTrip.getAvgSpeed());
                trip.setDriverPrice(tripPrice);
                trip.setNetPrice(tripPrice);
                trip.setStatus(TripStatus.COMPLETED);
                DriverStatusEntity driverStatus = driverStatusRepository.findOneByDriverId(trip.getDriverId());
                driverStatus.setStatus("IDLE");
                tripRepository.save(trip);
                driverStatusRepository.save(driverStatus);

                //publish to topic
                EndTripPublishResponse response = new EndTripPublishResponse();
                EndTripData data = new EndTripData();
                data.setCustomerId(trip.getCustomerId());
                data.setDriverId(trip.getDriverId());
                data.setTripId(trip.getTripId());
                data.setStatusReason("Trip Completed");
                data.setPrice(tripPrice);
                data.setPaymentType(trip.getPaymentType().toString());
                UserInfo userInfo = new UserInfo(Arrays.asList(data.getCustomerId(),data.getDriverId()), endTrip.getChannel(), endTrip.getUuid());
                response.setUserInfo(userInfo);
                response.setData(data);
                response.setResponseHeader(new ResHeaderPublish("TMS1000","Success","200"));
                response.setStatus(endTrip.getStatus());
                kafkaProducerService.publishMsgToTopic(Constants.TOPIC_TRIP_DATA, response);
            }else {
                log.error("send vehicle not found error to requested user");
            }
        }else {
            log.error("send trip not found error to requested user");
            //send trip not found or driver not found error to requested user
        }
    }

    private DistanceAndTime getDistanceTimeWithMiddlePoints(Location start,List<Location> midStops,Location destination){
        List<Location> locations = new ArrayList<>();
        locations.add(start);
        if(Objects.nonNull(midStops)){
            locations.addAll(midStops);
        }
        locations.add(destination);

        Long totalDistance = 0L;
        Long totalTime = 0L;
        for(int i =0;i<locations.size()-1;i++){
            DistanceAndTime data = mapService.getDistanceAndTime(locations.get(i),locations.get(i+1));
            totalDistance+=data.getDistance();
            totalTime+=data.getTime();
        }
        return new DistanceAndTime(totalDistance+"Meters",totalTime+"Seconds",totalDistance,totalTime);
    }

    /**
     * save the trip record by adding to trip entity
     * @param request
     * @param customer
     * @param response
     * @param status
     */
    private void initiateTripEntity(ConfirmPickup request,CustomerEntity customer,
                                    ConfirmPickupData response, TripStatus status){
        TripEntity trip = new TripEntity(request.getTripId(),
                request.getUserId(),status,customer.getFirstName().concat(" ").concat(customer.getLastName()),
                customer.getMsisdn(), request.getSecondaryNumber(),response.getPrice(), response.getPrice(), response.getDistance(),
                response.getEstimatedTime(),request.getUuid(),request.getStart(),request.getMidStops(),request.getDestination(),request.getVehicleType().toString(),
                request.getNote(), request.getPromoCode(),request.getPaymentType());
        tripRepository.save(trip);

    }

}
