package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverRepository {

    Driver save(Driver driver);

    Optional<Driver> findById(String id);

    List<Driver> findAll();

    List<Driver> findAvailable();
}
