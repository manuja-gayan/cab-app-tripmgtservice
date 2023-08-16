package com.ceyloncab.tripmgtservice.external.repository;

import com.ceyloncab.tripmgtservice.domain.entity.AdminEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<AdminEntity,String> {
    Optional<AdminEntity> findByEmail(String email);
    List<AdminEntity> findByIsDeleteFalse();
    List<AdminEntity> findByIsDeleteTrue();

}
