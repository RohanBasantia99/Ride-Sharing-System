package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.repository.RiderRepository;
import com.airtribe.ridewise.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
    private final IdGenerator idGenerator;

    public RiderServiceImpl(RiderRepository riderRepository, IdGenerator idGenerator) {
        this.riderRepository = riderRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Rider registerRider(String name, Location location) {
        Rider rider = new Rider(idGenerator.nextId("RIDER"), name, location);
        return riderRepository.save(rider);
    }

    @Override
    public Rider getRiderById(String id) {
        return riderRepository.findById(id)
                .orElseThrow(() -> new RiderNotFoundException("No rider found with id " + id));
    }

    @Override
    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }
}
