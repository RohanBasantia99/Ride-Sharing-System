package com.airtribe.ridewise.model;

public class Rider {

    private final String id;
    private final String name;
    private Location location;

    public Rider(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void relocate(Location newLocation) {
        this.location = newLocation;
    }

    @Override
    public String toString() {
        return "Rider{id='" + id + "', name='" + name + "', location=" + location + "}";
    }
}
