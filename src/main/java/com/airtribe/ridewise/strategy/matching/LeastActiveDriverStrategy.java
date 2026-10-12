package com.airtribe.ridewise.strategy.matching;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("leastActive")
public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .min(Comparator.comparingInt(Driver::getActiveRideCount))
                .orElseThrow(() -> new NoDriverAvailableException("No available driver found for rider " + rider.getId()));
    }

    @Override
    public String describe() {
        return "Least Active Driver";
    }
}
