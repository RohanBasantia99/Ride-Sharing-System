package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.InvalidStrategyException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.repository.InMemoryRideRepository;
import com.airtribe.ridewise.repository.RideRepository;
import com.airtribe.ridewise.strategy.fare.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.fare.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.matching.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.matching.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.matching.RideMatchingStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RideServiceImplTest {

    private RideService rideService;
    private RiderService riderService;
    private DriverService driverService;

    @BeforeEach
    void setUp() {
        IdGenerator idGenerator = new IdGenerator();
        RideRepository rideRepository = new InMemoryRideRepository();

        riderService = new RiderServiceImpl(new com.airtribe.ridewise.repository.InMemoryRiderRepository(), idGenerator);
        driverService = new DriverServiceImpl(new com.airtribe.ridewise.repository.InMemoryDriverRepository(), idGenerator);

        Map<String, RideMatchingStrategy> matchingStrategies = Map.of(
                "nearest", new NearestDriverStrategy(),
                "leastActive", new LeastActiveDriverStrategy()
        );
        Map<String, FareStrategy> fareStrategies = Map.of(
                "default", new DefaultFareStrategy(),
                "peakHour", new PeakHourFareStrategy()
        );

        rideService = new RideServiceImpl(rideRepository, riderService, driverService, idGenerator,
                matchingStrategies, fareStrategies);
    }

    @Test
    void requestRide_assignsNearestDriver_andMarksUnavailable() {
        Rider rider = riderService.registerRider("Alice", new Location(0, 0));
        Driver near = driverService.registerDriver("Bob", VehicleType.CAR, new Location(1, 0));
        driverService.registerDriver("Carl", VehicleType.CAR, new Location(10, 0));

        Ride ride = rideService.requestRide(rider.getId(), 5.0, "nearest", "default");

        assertEquals(near.getId(), ride.getDriver().getId());
        assertEquals(RideStatus.ASSIGNED, ride.getStatus());
        assertTrue(driverService.getAvailableDrivers().stream().noneMatch(d -> d.getId().equals(near.getId())));
    }

    @Test
    void requestRide_withNoAvailableDrivers_throws() {
        Rider rider = riderService.registerRider("Alice", new Location(0, 0));

        assertThrows(NoDriverAvailableException.class,
                () -> rideService.requestRide(rider.getId(), 5.0, "nearest", "default"));
    }

    @Test
    void requestRide_withUnknownStrategyKey_throws() {
        Rider rider = riderService.registerRider("Alice", new Location(0, 0));
        driverService.registerDriver("Bob", VehicleType.CAR, new Location(1, 0));

        assertThrows(InvalidStrategyException.class,
                () -> rideService.requestRide(rider.getId(), 5.0, "doesNotExist", "default"));
    }

    @Test
    void completeRide_usesFareStrategyChosenAtRequestTime_andFreesDriver() {
        Rider rider = riderService.registerRider("Alice", new Location(0, 0));
        Driver driver = driverService.registerDriver("Bob", VehicleType.CAR, new Location(0, 0));

        Ride requested = rideService.requestRide(rider.getId(), 10.0, "nearest", "peakHour");
        Ride completed = rideService.completeRide(requested.getId());

        double expectedFare = new PeakHourFareStrategy().calculateFare(completed);
        assertEquals(RideStatus.COMPLETED, completed.getStatus());
        assertEquals(expectedFare, completed.getFareReceipt().getAmount(), 0.001);
        assertTrue(driverService.getAvailableDrivers().stream().anyMatch(d -> d.getId().equals(driver.getId())));
    }
}
