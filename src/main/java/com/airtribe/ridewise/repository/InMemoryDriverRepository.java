package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Driver;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDriverRepository implements DriverRepository {

    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();

    @Override
    public Driver save(Driver driver) {
        drivers.put(driver.getId(), driver);
        return driver;
    }

    @Override
    public Optional<Driver> findById(String id) {
        return Optional.ofNullable(drivers.get(id));
    }

    @Override
    public List<Driver> findAll() {
        return List.copyOf(drivers.values());
    }

    @Override
    public List<Driver> findAvailable() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }
}
