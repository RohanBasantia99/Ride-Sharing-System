# RideWise — Requirements

## Functional Requirements

- Register riders (name, location).
- Register drivers (name, vehicle type, location).
- View available drivers.
- Request a ride for a rider, selecting:
  - a **ride matching strategy** (how the driver is picked), and
  - a **fare strategy** (how the fare is calculated at completion).
- Complete a ride, generating a `FareReceipt` using the fare strategy chosen at
  request time, and freeing the assigned driver.
- Cancel a ride, freeing the assigned driver.
- View all rides and their statuses.
- Track ride lifecycle: `REQUESTED → ASSIGNED → COMPLETED` or `CANCELLED`.

## Non-Functional Requirements

- **Extendable pricing**: new fare algorithms are added as a new `FareStrategy`
  implementation (`@Component`) with zero changes to `RideService` or the
  console menu.
- **Extendable matching**: same guarantee for `RideMatchingStrategy`.
- **Low coupling**: `RideService` depends only on repository and strategy
  interfaces, never concrete implementations (DIP). Riders/Drivers/Rides don't
  know about services or Spring.
- **Maintainable/readable**: single-purpose classes, constructor injection
  only, no field injection, no static mutable state.

## Interface

Console menu, backed by a Spring Boot application context
(`spring.main.web-application-type=none` — no embedded server). Dependency
injection is handled entirely by Spring beans (`@Component`, `@Service`,
`@Repository`) instead of manual wiring in `main()`.

## Out of Scope

- Persistence (data is in-memory only, lost on exit).
- Authentication, payments, real maps/geocoding.
- Concurrency beyond thread-safe in-memory storage (`ConcurrentHashMap`).
