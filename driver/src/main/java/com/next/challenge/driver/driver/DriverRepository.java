package com.next.challenge.driver.driver;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {

    Optional<Driver> findById(String id);

    Page<Driver> findAll(Pageable pageable);

}
