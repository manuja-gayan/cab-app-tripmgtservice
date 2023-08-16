package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.tripmgtservice.domain.entity.DriverEntity;
import com.ceyloncab.tripmgtservice.domain.utils.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DriverRepository extends MongoRepository<DriverEntity, String> {
    Optional<DriverEntity> findOneByMsisdn(String msisdn);
    Optional<DriverEntity> findOneByUserId(String userId);
    List<DriverEntity> findByStatus(Status status);
}
