package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Ride;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRideRepository implements RideRepository {

    private final Map<String, Ride> rides = new ConcurrentHashMap<>();

    @Override
    public Ride save(Ride ride) {
        rides.put(ride.getId(), ride);
        return ride;
    }

    @Override
    public Optional<Ride> findById(String id) {
        return Optional.ofNullable(rides.get(id));
    }

    @Override
    public List<Ride> findAll() {
        return List.copyOf(rides.values());
    }
}
