package com.ceyloncab.tripmgtservice.domain.service;
import com.ceyloncab.tripmgtservice.application.aop.AopConstants;
import com.ceyloncab.tripmgtservice.application.transport.request.RevenueHistoryRequest;
import com.ceyloncab.tripmgtservice.domain.entity.AdminEntity;
import com.ceyloncab.tripmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.tripmgtservice.domain.entity.DriverEntity;
import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
import com.ceyloncab.tripmgtservice.application.transport.request.CommissionHistoryRequest;
import com.ceyloncab.tripmgtservice.domain.entity.dto.CommissionHistoryResponseDTO;
import com.ceyloncab.tripmgtservice.domain.entity.dto.HistoryTripDTO;
import com.ceyloncab.tripmgtservice.domain.entity.dto.SummeryResponseDTO;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.tripmgtservice.domain.exception.DomainException;
import com.ceyloncab.tripmgtservice.domain.utils.Constants;
import com.ceyloncab.tripmgtservice.domain.utils.Status;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import com.ceyloncab.tripmgtservice.domain.utils.VehicleCount;
import com.ceyloncab.tripmgtservice.external.repository.AdminRepository;
import com.ceyloncab.tripmgtservice.external.repository.CustomerRepository;
import com.ceyloncab.tripmgtservice.external.repository.DriverRepository;
import com.ceyloncab.tripmgtservice.external.repository.TripRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class DashBoardManagementService {

    MongoOperations mongoOps;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    DriverRepository driverRepository;

    public CommonResponse<SummeryResponseDTO>getSummery(){
        CommonResponse<SummeryResponseDTO> response = new CommonResponse<>();

        AdminEntity adminEntity = adminRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Admin does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.ADMIN_NOT_FOUND);
                });
        try {
            SummeryResponseDTO summeryResponseDTO = new SummeryResponseDTO();
            List<VehicleCount> vehicles = new ArrayList<>();
            VehicleCount carCount = new VehicleCount();
            carCount.setType("CAR");
            carCount.setTripCount(carCount());
            vehicles.add(carCount);

            VehicleCount tukCount = new VehicleCount();
            tukCount.setType("TUK");
            tukCount.setTripCount(tukCount());
            vehicles.add(tukCount);

            VehicleCount vanCount = new VehicleCount();
            vanCount.setType("VAN");
            vanCount.setTripCount(vanCount());
            vehicles.add(vanCount);

            VehicleCount miniVanCount = new VehicleCount();
            miniVanCount.setType("MINI-VAN");
            miniVanCount.setTripCount(miniVanCount());
            vehicles.add(vanCount);

            summeryResponseDTO.setVehicles(vehicles);
            summeryResponseDTO.setCustomers(customerCount());
            summeryResponseDTO.setPendingDrivers(pendingDriversCount());
            summeryResponseDTO.setActiveDrivers(activeDriversCount());
            summeryResponseDTO.setOngoingTrips(onGoingsCount());
            summeryResponseDTO.setCompletedTrips(completedTripsCount());
            summeryResponseDTO.setCancelledTrips(cancelledTripsCount());
            summeryResponseDTO.setRevenue(totalRevenue());
            summeryResponseDTO.setCommission(totalCommission());

            response.setData(summeryResponseDTO);
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
            return response;

        }catch (Exception ex) {
            log.error("Error occurred in createAdmin.Error:{}", ex.getMessage());
        }
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;

    }

    public CommonResponse getRevenueHistory(RevenueHistoryRequest revenueHistoryRequest) {

        CommonResponse<Object> response = new CommonResponse<>();

        Double totalRevenue = 0.0;

        AdminEntity adminEntity = adminRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Admin does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.ADMIN_NOT_FOUND);
                });
        try {
            if (revenueHistoryRequest.getQueryType().equals("ALL")) {
                List<TripEntity> byUpdatedTimeBetweenAndStatus = tripRepository.findByUpdatedTimeBetweenAndStatus(revenueHistoryRequest.getStartDate(), revenueHistoryRequest.getEndDate(), TripStatus.COMPLETED);
                for (TripEntity tripEntity : byUpdatedTimeBetweenAndStatus) {
                    totalRevenue += tripEntity.getNetPrice();
                }
                response.setData(totalRevenue);
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
                return response;
            } else {
                List<TripEntity> byUpdatedTimeBetweenAndStatusAndVehicleType = tripRepository.findByUpdatedTimeBetweenAndStatusAndVehicleType(revenueHistoryRequest.getStartDate(), revenueHistoryRequest.getEndDate(), TripStatus.COMPLETED, revenueHistoryRequest.getQueryType());
                for (TripEntity tripEntity : byUpdatedTimeBetweenAndStatusAndVehicleType) {
                    totalRevenue += tripEntity.getNetPrice();
                }

                response.setData(totalRevenue);
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
                return response;
            }

        } catch (Exception ex) {
            log.error("Error occurred in createAdmin.Error:{}", ex.getMessage());
        }

        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public CommonResponse<List<CommissionHistoryResponseDTO>>  getCommissionHistory(CommissionHistoryRequest commissionHistoryRequest){
        CommonResponse<List<CommissionHistoryResponseDTO>> response = new CommonResponse<>();
        List<CommissionHistoryResponseDTO> tripsList = new ArrayList<>();

        AdminEntity adminEntity = adminRepository.findById(MDC.get(AopConstants.MDC_USERID))
                .orElseThrow(() -> {
                    log.error("Admin does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.ADMIN_NOT_FOUND);
                });
        try {
            if (commissionHistoryRequest.getQueryType().equals("TRIP")){
                List<TripEntity> byUpdatedTimeBetweenAndStatus = tripRepository.findByUpdatedTimeBetweenAndStatus(commissionHistoryRequest.getStartDate(), commissionHistoryRequest.getEndDate(), TripStatus.COMPLETED);


                for (TripEntity tripEntity :byUpdatedTimeBetweenAndStatus){
                    CommissionHistoryResponseDTO commissionHistoryResponseDTO = new CommissionHistoryResponseDTO();
                    commissionHistoryResponseDTO.setTripId(tripEntity.getTripId());
                    System.out.println(tripEntity.getTripId());
                    commissionHistoryResponseDTO.setCustomerId(tripEntity.getCustomerId());
                    commissionHistoryResponseDTO.setDriverId(tripEntity.getDriverId());
                    commissionHistoryResponseDTO.setDriverName(tripEntity.getDriverName());
                    commissionHistoryResponseDTO.setRevenue(tripEntity.getNetPrice());
                    commissionHistoryResponseDTO.setCommission((tripEntity.getNetPrice()-tripEntity.getDriverPrice()));
                    tripsList.add(commissionHistoryResponseDTO);
                }
                response.setData(tripsList);
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
                return response;
            } else {
                List<TripEntity> byUpdatedTimeBetweenAndStatus = tripRepository.findByUpdatedTimeBetweenAndStatus(commissionHistoryRequest.getStartDate(), commissionHistoryRequest.getEndDate(), TripStatus.COMPLETED);
                 HashMap<String,HistoryTripDTO> hm=new HashMap<>();
                 HistoryTripDTO history ;


                for (TripEntity tripEntity :byUpdatedTimeBetweenAndStatus){

                      if (hm.containsKey(tripEntity.getDriverId())){

                          history = hm.get(tripEntity.getDriverId());
                          history.setRevenue(history.getRevenue()+tripEntity.getNetPrice());
                          history.setCommission(history.getCommission()+(tripEntity.getNetPrice()-tripEntity.getDriverPrice()));
                      }else {
                         history=new HistoryTripDTO();
                          history.setRevenue(tripEntity.getNetPrice());
                          history.setDriverName(tripEntity.getDriverName());
                          history.setCommission(tripEntity.getNetPrice()-tripEntity.getDriverPrice());
                          hm.put(tripEntity.getDriverId(),history);
                      }


                }

                CommissionHistoryResponseDTO commissionHistoryResponseDTO;
                for (String key : hm.keySet()){
                    history= hm.get(key);
                    System.out.println("histort "+history.getRevenue());
                    System.out.println("key "+key);

                    commissionHistoryResponseDTO = new CommissionHistoryResponseDTO();

                    commissionHistoryResponseDTO.setDriverId(key);
                    commissionHistoryResponseDTO.setDriverName(history.getDriverName());
                    commissionHistoryResponseDTO.setRevenue(history.getRevenue());
                    commissionHistoryResponseDTO.setCommission(history.getCommission());
                    tripsList.add(commissionHistoryResponseDTO);

                }

                response.setData(tripsList);
                response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
                return response;
            }
        } catch (Exception ex) {
            log.error("Error occurred in createAdmin.Error:{}", ex.getMessage());
        }
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.QUERY_SUCCESS));
        return response;
    }

    public Integer carCount(){
        Date currentDate = new Date();
        List<TripEntity> car = tripRepository.findByUpdatedTimeAndVehicleTypeAndStatus(currentDate, "CAR", TripStatus.COMPLETED);
        return car.size();
    }

    public Integer tukCount(){
        Date currentDate = new Date();
        List<TripEntity> car = tripRepository.findByUpdatedTimeAndVehicleTypeAndStatus(currentDate, "TUK", TripStatus.COMPLETED);
        return car.size();
    }

    public Integer vanCount(){
        Date currentDate = new Date();
        List<TripEntity> car = tripRepository.findByUpdatedTimeAndVehicleTypeAndStatus(currentDate, "VAN", TripStatus.COMPLETED);
        return car.size();
    }

    public Integer miniVanCount(){
        Date currentDate = new Date();
        List<TripEntity> car = tripRepository.findByUpdatedTimeAndVehicleTypeAndStatus(currentDate, "MINI-VAN", TripStatus.COMPLETED);
        return car.size();
    }

    public Integer customerCount(){
        String date= String.valueOf(LocalDate.now());
        List<CustomerEntity> all = customerRepository.findAll();
        return all.size();
    }

    public Integer pendingDriversCount(){
        String date= String.valueOf(LocalDate.now());
        List<DriverEntity> byStatus = driverRepository.findByStatus(Status.PENDING);
        return byStatus.size();
    }

    public Integer activeDriversCount(){
        String date= String.valueOf(LocalDate.now());
        List<DriverEntity> byStatus = driverRepository.findByStatus(Status.ACTIVE);
        return byStatus.size();
    }

    public Integer onGoingsCount(){
        Date currentDate = new Date();
        List<TripEntity> byUpdatedTimeAndStatus = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.PENDING);
        return byUpdatedTimeAndStatus.size();
    }

    public Integer completedTripsCount(){
        Date currentDate = new Date();
        List<TripEntity> byUpdatedTimeAndStatus = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.COMPLETED);
        return byUpdatedTimeAndStatus.size();
    }

    public Integer cancelledTripsCount(){
        Date currentDate = new Date();
        int customerCancelledCount;
        int driverCancelledCount;
        int totalCancelledCount;
        List<TripEntity> byUpdatedTimeAndStatus = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.CUSTOMER_CANCELED);
        customerCancelledCount=byUpdatedTimeAndStatus.size();
        List<TripEntity> byUpdatedTimeAndStatus1 = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.DRIVER_CANCELLED);
        driverCancelledCount=byUpdatedTimeAndStatus1.size();
        totalCancelledCount=customerCancelledCount+driverCancelledCount;
        return totalCancelledCount;
    }

    public Double totalRevenue(){
        Double totalRevenue = 0.0;
        Date currentDate = new Date();
        List<TripEntity> byUpdatedTimeAndStatus = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.COMPLETED);
      for (TripEntity tripEntity :byUpdatedTimeAndStatus){
          totalRevenue += tripEntity.getNetPrice();
      }
      return totalRevenue;
    }

    public Double totalCommission(){
        Double totalRevenue = 0.0;
        Double totalCommission = 0.0;
        Double driverPrice = 0.0;

        Date currentDate = new Date();
        List<TripEntity> byUpdatedTimeAndStatus = tripRepository.findByUpdatedTimeAndStatus(currentDate, TripStatus.COMPLETED);
        for (TripEntity tripEntity :byUpdatedTimeAndStatus){
            totalRevenue += tripEntity.getNetPrice();
            driverPrice +=tripEntity.getDriverPrice();
        }
        return totalRevenue-driverPrice;
    }

}
