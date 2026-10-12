package com.airtribe.ridewise.console;

import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.exception.InvalidStrategyException;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.exception.RideNotFoundException;
import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.strategy.fare.FareStrategy;
import com.airtribe.ridewise.strategy.matching.RideMatchingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class ConsoleMenu {

    private final RiderService riderService;
    private final DriverService driverService;
    private final RideService rideService;
    private final Scanner scanner;

    public ConsoleMenu(RiderService riderService, DriverService driverService, RideService rideService) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.rideService = rideService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Welcome to RideWise ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addRider();
                    case 2 -> addDriver();
                    case 3 -> viewAvailableDrivers();
                    case 4 -> requestRide();
                    case 5 -> completeRide();
                    case 6 -> viewRides();
                    case 7 -> running = false;
                    default -> System.out.println("Invalid option. Please choose between 1 and 7.");
                }
            } catch (RiderNotFoundException | DriverNotFoundException | RideNotFoundException
                     | NoDriverAvailableException | InvalidStrategyException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Thank you for using RideWise. Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Exit");
    }

    private void addRider() {
        String name = readLine("Rider name: ");
        double x = readDouble("Location X: ");
        double y = readDouble("Location Y: ");
        Rider rider = riderService.registerRider(name, new Location(x, y));
        System.out.println("Registered: " + rider);
    }

    private void addDriver() {
        String name = readLine("Driver name: ");
        VehicleType vehicleType = readVehicleType();
        double x = readDouble("Location X: ");
        double y = readDouble("Location Y: ");
        Driver driver = driverService.registerDriver(name, vehicleType, new Location(x, y));
        System.out.println("Registered: " + driver);
    }

    private void viewAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No drivers available right now.");
            return;
        }
        drivers.forEach(System.out::println);
    }

    private void requestRide() {
        String riderId = readLine("Rider ID: ");
        double distance = readDouble("Trip distance (km): ");
        String matchingKey = chooseMatchingStrategy();
        String fareKey = chooseFareStrategy();

        Ride ride = rideService.requestRide(riderId, distance, matchingKey, fareKey);
        System.out.println("Ride requested and assigned: " + ride);
    }

    private void completeRide() {
        String rideId = readLine("Ride ID: ");
        Ride ride = rideService.completeRide(rideId);
        System.out.println("Ride completed: " + ride);
        System.out.println("Receipt: " + ride.getFareReceipt());
    }

    private void viewRides() {
        List<Ride> rides = rideService.getAllRides();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
            return;
        }
        rides.forEach(System.out::println);
    }

    private String chooseMatchingStrategy() {
        Map<String, RideMatchingStrategy> strategies = rideService.getAvailableMatchingStrategies();
        System.out.println("Available ride matching strategies:");
        strategies.forEach((key, strategy) -> System.out.println("  " + key + " -> " + strategy.describe()));
        return readLine("Choose matching strategy key: ");
    }

    private String chooseFareStrategy() {
        Map<String, FareStrategy> strategies = rideService.getAvailableFareStrategies();
        System.out.println("Available fare strategies:");
        strategies.forEach((key, strategy) -> System.out.println("  " + key + " -> " + strategy.describe()));
        return readLine("Choose fare strategy key: ");
    }

    private VehicleType readVehicleType() {
        while (true) {
            String input = readLine("Vehicle type (BIKE, AUTO, CAR): ");
            try {
                return VehicleType.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid vehicle type. Please try again.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
