package com.airtribe.ridewise.model;

public class Driver {

    private final String id;
    private final String name;
    private final VehicleType vehicleType;
    private Location currentLocation;
    private boolean available;
    private int activeRideCount;

    public Driver(String id, String name, VehicleType vehicleType, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.currentLocation = currentLocation;
        this.available = true;
        this.activeRideCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getActiveRideCount() {
        return activeRideCount;
    }

    public void relocate(Location newLocation) {
        this.currentLocation = newLocation;
    }

    public void markAssigned() {
        this.available = false;
        this.activeRideCount++;
    }

    public void markAvailable() {
        this.available = true;
    }

    @Override
    public String toString() {
        return "Driver{id='" + id + "', name='" + name + "', vehicleType=" + vehicleType
                + ", location=" + currentLocation + ", available=" + available + "}";
    }
}
