# RideWise — Class Model

## Domain Model (`model/`)

| Class         | Responsibility                                                         |
|---------------|--------------------------------------------------------------------------|
| `Location`    | Immutable x/y coordinate value object; knows how to compute distance to another `Location`. |
| `Rider`       | A rider's identity and current location.                               |
| `Driver`      | A driver's identity, vehicle type, location, availability and active-ride count. |
| `Ride`        | A single ride's lifecycle: rider, assigned driver, distance, status, chosen fare strategy key, and (once completed) its `FareReceipt`. Exposes behavior (`assignDriver`, `complete`, `cancel`) instead of raw setters. |
| `FareReceipt` | Immutable record of a completed ride's fare: ride id, amount, timestamp. |
| `RideStatus`  | Enum: `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`.                |
| `VehicleType` | Enum: `BIKE`, `AUTO`, `CAR`.                                            |

## Strategy Layer (`strategy/`)

| Interface             | Implementations                              | Contract |
|------------------------|----------------------------------------------|----------|
| `RideMatchingStrategy` | `NearestDriverStrategy`, `LeastActiveDriverStrategy` | `Driver findDriver(Rider, List<Driver>)` |
| `FareStrategy`         | `DefaultFareStrategy`, `PeakHourFareStrategy` | `double calculateFare(Ride)` |

Each implementation is a Spring `@Component` with an explicit bean name
(`"nearest"`, `"leastActive"`, `"default"`, `"peakHour"`). `RideService` injects
`Map<String, RideMatchingStrategy>` and `Map<String, FareStrategy>` — Spring
auto-populates these maps keyed by bean name, giving `RideService` a live
strategy registry with no manual bean lookups and no `switch`/`instanceof`
dispatch.

## Repository Layer (`repository/`)

In-memory storage, one repository interface + `ConcurrentHashMap`-backed impl
per aggregate: `RiderRepository`, `DriverRepository`, `RideRepository`. Kept
separate from the service layer so services hold only business logic, not
storage mechanics (SRP).

## Service Layer (`service/`)

| Service        | Responsibility |
|----------------|----------------|
| `RiderService` | Register riders, look up by id, list all. |
| `DriverService`| Register drivers, look up by id, list all/available, toggle availability. |
| `RideService`  | Orchestrates the ride lifecycle: resolves a matching strategy to assign a driver, resolves a fare strategy at completion, delegates to `RiderService`/`DriverService` for cross-entity lookups (never reaches through `Ride.getRider().getX()` chains — Law of Demeter). |

## Console Layer (`console/`)

`ConsoleMenu` owns the `Scanner` and the menu loop. It depends only on the
three service interfaces — never on repositories or strategy classes directly.
All input parsing is wrapped so invalid input (non-numeric menu choice, unknown
strategy key, unknown id) is caught and reported without crashing the loop.

## Relationships

| Relationship                  | Type        | Why |
|--------------------------------|-------------|-----|
| `Rider` → `Ride`               | Association | A ride references a rider, but riders exist independently of any one ride. |
| `Driver` → `Ride`               | Association | Same reasoning for drivers. |
| `Ride` → `FareReceipt`          | Composition | A receipt is created by and belongs to exactly one ride's lifecycle; it has no existence outside it. |
| `RideService` → Strategies      | Composition (Spring-managed) | `RideService` holds the strategy maps for its whole lifetime; Spring constructs and injects them instead of `RideService` calling `new`. |
| `RideService` → Repositories    | Composition (Spring-managed) | Same reasoning — injected collaborators owned for the service's lifetime. |
