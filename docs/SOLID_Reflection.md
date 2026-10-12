# RideWise — SOLID Reflection

## Single Responsibility Principle

Each class has exactly one reason to change:
- `RiderService` / `DriverService` / `RideService` — one per aggregate's
  business logic.
- `*Repository` — storage only, no business rules.
- `IdGenerator` — id generation only.
- `ConsoleMenu` — I/O and dispatch only, delegates all logic to services.
- Each strategy class — one algorithm, nothing else.

## Open/Closed Principle

Adding a new ride-matching or fare algorithm requires **zero changes** to
`RideService` or `ConsoleMenu`: create a new class implementing
`RideMatchingStrategy` or `FareStrategy`, annotate it `@Component("key")`, and
it automatically appears in the console menu's strategy list (because the menu
reads the live `Map<String, Strategy>` injected into `RideService`) and
becomes selectable immediately. Core orchestration code in `RideServiceImpl`
never needs to be touched or recompiled against the new strategy.

## Liskov Substitution Principle

Any `RideMatchingStrategy` implementation can replace another without breaking
`RideService`: all take `(Rider, List<Driver>)` and return a `Driver` (or throw
`NoDriverAvailableException` if none can be matched — the same exception type
for every implementation, so callers don't need to know which strategy threw
it). Same guarantee for `FareStrategy`: every implementation takes a `Ride` and
returns a non-negative `double`, with no strategy narrowing the contract or
throwing an undeclared exception type.

## Interface Segregation Principle

`RideMatchingStrategy` and `FareStrategy` are each a single-method interface
(plus a `describe()` label for the console). No implementer is forced to
implement unrelated methods — contrast with a hypothetical fat `Strategy`
interface covering both matching and pricing, which would force a
fare-calculation class to also implement driver-matching logic it doesn't
need.

## Dependency Inversion Principle

`RideServiceImpl` depends only on:
- `RiderService` / `DriverService` interfaces (not their `Impl` classes),
- `RideRepository` interface (not `InMemoryRideRepository`),
- `Map<String, RideMatchingStrategy>` / `Map<String, FareStrategy>` (interface
  types, never concrete strategy classes).

Spring's `ApplicationContext` is the sole place concrete implementations are
selected and wired — by component scanning, not by any code inside the service
or console layers calling `new SomeConcreteClass()`. This is also why swapping
`InMemoryRideRepository` for a future database-backed repository would need no
changes to `RideService` at all.
