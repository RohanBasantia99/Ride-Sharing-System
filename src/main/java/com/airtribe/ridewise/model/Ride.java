package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class Ride {

    private final String id;
    private final Rider rider;
    private Driver driver;
    private final double distance;
    private RideStatus status;
    private FareReceipt fareReceipt;
    private final LocalDateTime requestedAt;
    private final String fareStrategyKey;

    public Ride(String id, Rider rider, double distance, String fareStrategyKey) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
        this.status = RideStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
        this.fareStrategyKey = fareStrategyKey;
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getFareStrategyKey() {
        return fareStrategyKey;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.ASSIGNED;
    }

    public void complete(FareReceipt fareReceipt) {
        this.fareReceipt = fareReceipt;
        this.status = RideStatus.COMPLETED;
    }

    public void cancel() {
        this.status = RideStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "Ride{id='" + id + "', rider='" + rider.getName() + "', driver="
                + (driver == null ? "none" : driver.getName()) + ", distance=" + distance
                + ", status=" + status + "}";
    }
}
