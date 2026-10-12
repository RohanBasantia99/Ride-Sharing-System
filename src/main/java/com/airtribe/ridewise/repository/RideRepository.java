package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Ride;

import java.util.List;
import java.util.Optional;

public interface RideRepository {

    Ride save(Ride ride);

    Optional<Ride> findById(String id);

    List<Ride> findAll();
}
