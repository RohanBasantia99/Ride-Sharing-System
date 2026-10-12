package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidStrategyException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.exception.RideNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.repository.RideRepository;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.matching.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RiderService riderService;
    private final DriverService driverService;
    private final IdGenerator idGenerator;
    private final Map<String, RideMatchingStrategy> matchingStrategies;
    private final Map<String, FareStrategy> fareStrategies;

    public RideServiceImpl(RideRepository rideRepository,
                            RiderService riderService,
                            DriverService driverService,
                            IdGenerator idGenerator,
                            Map<String, RideMatchingStrategy> matchingStrategies,
                            Map<String, FareStrategy> fareStrategies) {
        this.rideRepository = rideRepository;
        this.riderService = riderService;
        this.driverService = driverService;
        this.idGenerator = idGenerator;
        this.matchingStrategies = matchingStrategies;
        this.fareStrategies = fareStrategies;
    }

    @Override
    public Ride requestRide(String riderId, double distance, String matchingStrategyKey, String fareStrategyKey) {
        Rider rider = riderService.getRiderById(riderId);
        RideMatchingStrategy matchingStrategy = resolveMatchingStrategy(matchingStrategyKey);
        resolveFareStrategy(fareStrategyKey);

        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No drivers are currently available");
        }

        Driver assignedDriver = matchingStrategy.findDriver(rider, availableDrivers);

        Ride ride = new Ride(idGenerator.nextId("RIDE"), rider, distance, fareStrategyKey);
        ride.assignDriver(assignedDriver);
        driverService.updateAvailability(assignedDriver.getId(), false);

        rideRepository.save(ride);
        return ride;
    }

    @Override
    public Ride completeRide(String rideId) {
        Ride ride = getRideById(rideId);
        FareStrategy fareStrategy = resolveFareStrategy(ride.getFareStrategyKey());

        double amount = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(ride.getId(), amount, LocalDateTime.now());
        ride.complete(receipt);

        Driver driver = ride.getDriver();
        if (driver != null) {
            driverService.updateAvailability(driver.getId(), true);
        }

        rideRepository.save(ride);
        return ride;
    }

    @Override
    public Ride cancelRide(String rideId) {
        Ride ride = getRideById(rideId);
        ride.cancel();

        Driver driver = ride.getDriver();
        if (driver != null) {
            driverService.updateAvailability(driver.getId(), true);
        }

        rideRepository.save(ride);
        return ride;
    }

    @Override
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public Ride getRideById(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("No ride found with id " + rideId));
    }

    @Override
    public Map<String, RideMatchingStrategy> getAvailableMatchingStrategies() {
        return matchingStrategies;
    }

    @Override
    public Map<String, FareStrategy> getAvailableFareStrategies() {
        return fareStrategies;
    }

    private RideMatchingStrategy resolveMatchingStrategy(String key) {
        RideMatchingStrategy strategy = matchingStrategies.get(key);
        if (strategy == null) {
            throw new InvalidStrategyException("Unknown ride matching strategy: " + key);
        }
        return strategy;
    }

    private FareStrategy resolveFareStrategy(String key) {
        FareStrategy strategy = fareStrategies.get(key);
        if (strategy == null) {
            throw new InvalidStrategyException("Unknown fare strategy: " + key);
        }
        return strategy;
    }
}
