package com.airtribe.ridewise.strategy.fare;

import com.airtribe.ridewise.model.Ride;

public interface FareStrategy {

    double calculateFare(Ride ride);

    String describe();
}
