package com.airtribe.ridewise.strategy.fare;

import com.airtribe.ridewise.model.Ride;
import org.springframework.stereotype.Component;

@Component("default")
public class DefaultFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 30.0;
    private static final double PER_KM_RATE = 12.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (ride.getDistance() * PER_KM_RATE);
    }

    @Override
    public String describe() {
        return "Default Fare";
    }
}
