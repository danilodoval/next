package com.next.challenger.passenger.passenger;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends MongoRepository<Passenger, String> {

    Optional<Passenger> findById(String id);

    Page<Passenger> findAll(Pageable pageable);

}
