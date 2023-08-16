package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity,String> {

}
