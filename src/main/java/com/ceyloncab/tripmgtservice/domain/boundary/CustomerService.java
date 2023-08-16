package com.ceyloncab.tripmgtservice.domain.boundary;

import com.ceyloncab.tripmgtservice.domain.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerService {
    Optional<CustomerEntity> getCustomerProfile(String userId);
}
