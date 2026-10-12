package com.airtribe.ridewise.strategy.fare;

import com.airtribe.ridewise.model.Ride;
import org.springframework.stereotype.Component;

@Component("peakHour")
public class PeakHourFareStrategy implements FareStrategy {

    private static final double BASE_FARE = 30.0;
    private static final double PER_KM_RATE = 12.0;
    private static final double PEAK_MULTIPLIER = 1.75;

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (ride.getDistance() * PER_KM_RATE);
        return baseFare * PEAK_MULTIPLIER;
    }

    @Override
    public String describe() {
        return "Peak Hour Fare (1.75x)";
    }
}
