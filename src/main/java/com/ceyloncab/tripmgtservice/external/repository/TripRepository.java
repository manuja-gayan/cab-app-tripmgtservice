package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.TripEntity;
import com.ceyloncab.tripmgtservice.domain.utils.TripStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface TripRepository extends MongoRepository<TripEntity,String> {

//    @Query(value = "{updatedTime:{$gte:?0,$lte:?1}}")
//    List<TripsEntity> findByUpdatedTimeBetween(Date startDate, Date endDate);

//    @Query(value = "{'driver.userId':?0,updatedTime:{$gte:?1,$lte:?2}}")
//    List <TripsEntity> findByDriverUserIdAndUpdatedTimeBetween( String userId ,Date startDate, Date endDate);

    @Query(value = "{'driver.userId':?0,updatedTime:{$gte:?1,$lte:?2},status:?3}")
    List <TripEntity> findByDriverUserIdAndUpdatedTimeBetweenAndStatus(String userId , Date startDate, Date endDate, TripStatus status);

//    @Query(value = "{'driver.userId':?0,updatedTime:{$gte:?1,$lte:?2},status:?3}")
    @Query(value = "{'driver.userId':?0,updatedTime:?1,status:?2}")
    List <TripEntity> findByDriverUserIdAndUpdatedTimeAndStatus(String userId , Date startDate, TripStatus status);


    @Query(value = "{'customer.userId':?0,status:?1}")
    List<TripEntity> findByCustomerUserIdAndStatus(String userId, TripStatus status);


    @Query(value = "{'customer.userId':?0,updatedTime:{$gte:?1,$lte:?2},status:?3}")
    List <TripEntity> findByCustomerUserIdAndUpdatedTimeBetweenAndStatus(String userId , Date startDate, Date endDate, TripStatus status);


    @Query(value = "{'driver.userId':?0,status:?1}")
    List<TripEntity> findByDriverUserIdAndStatus(String userId, TripStatus status);

    @Query(value = "{updatedTime:{$gte:?0,$lte:?1},status:?2,vehicleType:?3}")
    List <TripEntity> findByUpdatedTimeBetweenAndStatusAndVehicleType(Date startDate, Date endDate, TripStatus status,String vehicleType );

    @Query(value = "{updatedTime:{$gte:?0,$lte:?1},status:?2}")
    List <TripEntity> findByUpdatedTimeBetweenAndStatus(Date startDate, Date endDate, TripStatus status );

    List <TripEntity> findByUpdatedTimeAndVehicleTypeAndStatus(Date updatedTime,String vehicleType,TripStatus status );

    List <TripEntity> findByUpdatedTimeAndStatus(Date updatedTime,TripStatus status );

    Optional<TripEntity> findOneByTripIdAndStatus(String tripId, String status);
}


