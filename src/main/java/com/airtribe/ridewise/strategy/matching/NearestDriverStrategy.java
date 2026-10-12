package com.airtribe.ridewise.strategy.matching;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("nearest")
public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .min(Comparator.comparingDouble(driver ->
                        driver.getCurrentLocation().distanceTo(rider.getLocation())))
                .orElseThrow(() -> new NoDriverAvailableException("No available driver found near rider " + rider.getId()));
    }

    @Override
    public String describe() {
        return "Nearest Driver";
    }
}
