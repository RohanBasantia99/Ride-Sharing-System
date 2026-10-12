package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;

import java.util.List;

public interface DriverService {

    Driver registerDriver(String name, VehicleType vehicleType, Location location);

    Driver getDriverById(String id);

    List<Driver> getAllDrivers();

    List<Driver> getAvailableDrivers();

    void updateAvailability(String driverId, boolean available);
}
