package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.matching.RideMatchingStrategy;

import java.util.List;
import java.util.Map;

public interface RideService {

    Ride requestRide(String riderId, double distance, String matchingStrategyKey, String fareStrategyKey);


    Ride completeRide(String rideId);

    Ride cancelRide(String rideId);

    List<Ride> getAllRides();

    Ride getRideById(String rideId);

    Map<String, RideMatchingStrategy> getAvailableMatchingStrategies();

    Map<String, FareStrategy> getAvailableFareStrategies();
}
