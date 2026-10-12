package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.repository.DriverRepository;
import com.airtribe.ridewise.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final IdGenerator idGenerator;

    public DriverServiceImpl(DriverRepository driverRepository, IdGenerator idGenerator) {
        this.driverRepository = driverRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Driver registerDriver(String name, VehicleType vehicleType, Location location) {
        Driver driver = new Driver(idGenerator.nextId("DRIVER"), name, vehicleType, location);
        return driverRepository.save(driver);
    }

    @Override
    public Driver getDriverById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("No driver found with id " + id));
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findAvailable();
    }

    @Override
    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (available) {
            driver.markAvailable();
        } else {
            driver.markAssigned();
        }
    }
}
