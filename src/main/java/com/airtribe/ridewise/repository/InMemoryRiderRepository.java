package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Rider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRiderRepository implements RiderRepository {

    private final Map<String, Rider> riders = new ConcurrentHashMap<>();

    @Override
    public Rider save(Rider rider) {
        riders.put(rider.getId(), rider);
        return rider;
    }

    @Override
    public Optional<Rider> findById(String id) {
        return Optional.ofNullable(riders.get(id));
    }

    @Override
    public List<Rider> findAll() {
        return List.copyOf(riders.values());
    }
}
