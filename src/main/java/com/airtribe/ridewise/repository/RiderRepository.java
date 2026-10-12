package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Rider;

import java.util.List;
import java.util.Optional;

public interface RiderRepository {

    Rider save(Rider rider);

    Optional<Rider> findById(String id);

    List<Rider> findAll();
}
