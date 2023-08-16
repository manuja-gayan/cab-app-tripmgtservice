package com.ceyloncab.tripmgtservice.external.serviceimpl;

import com.ceyloncab.tripmgtservice.domain.boundary.CustomerService;
import com.ceyloncab.tripmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.tripmgtservice.external.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<CustomerEntity> getCustomerProfile(String userId){
        return customerRepository.findById(userId);
    }
}
