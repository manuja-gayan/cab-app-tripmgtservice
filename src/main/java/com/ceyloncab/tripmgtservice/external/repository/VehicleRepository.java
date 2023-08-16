package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.VehiclesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VehicleRepository extends MongoRepository<VehiclesEntity, String> {
    Optional<VehiclesEntity> findOneByType(String type);
}
