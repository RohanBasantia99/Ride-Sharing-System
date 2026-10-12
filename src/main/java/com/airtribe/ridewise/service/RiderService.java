package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public interface RiderService {

    Rider registerRider(String name, Location location);

    Rider getRiderById(String id);

    List<Rider> getAllRiders();
}
