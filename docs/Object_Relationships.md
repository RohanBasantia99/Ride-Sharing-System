# RideWise — Object Relationships & Design Principles

## Composition over Inheritance

There are no inheritance hierarchies in the domain model — `Rider` and `Driver`
are independent classes rather than subclasses of a shared `AbstractUser`,
because they don't share behavior worth forcing into a common supertype
(a `Rider` has no vehicle, a `Driver` has no ride-request behavior). Where
behavior needs to vary (ride matching, fare calculation), it's composed in via
the Strategy pattern rather than expressed as subclassing:

- `Ride` **has-a** `FareReceipt` (composition) — the receipt is created only
  as part of completing a ride and has no independent lifecycle.
- `RideService` **has-a** collection of `RideMatchingStrategy` /
  `FareStrategy` beans (composition, Spring-managed) — behavior is injected,
  not inherited via an abstract base `RideService` with overridden methods.

## Association vs. Composition, applied

| Relationship | Type | Lifecycle reasoning |
|---|---|---|
| `Rider` ↔ `Ride` | Association | A `Rider` is registered once and can have many rides over time; a `Ride` references a rider but doesn't own it. |
| `Driver` ↔ `Ride` | Association | Same: a driver outlives any individual ride. |
| `Ride` → `FareReceipt` | Composition | A receipt belongs to exactly one ride and is destroyed with it conceptually — it's never shared or independently queried. |
| `RideService` → Strategies/Repositories | Composition | Injected once by Spring at construction and held for the service's entire lifetime. |

## Law of Demeter

`RideService` talks directly to its immediate collaborators
(`RiderService`, `DriverService`, `RideRepository`, the strategy maps) and
never reaches through an object graph to get what it needs — e.g. it does not
do `ride.getDriver().getCurrentLocation().distanceTo(...)` from inside
`RideServiceImpl`. Instead:

- Distance/location math lives inside `NearestDriverStrategy`, which is handed
  the `Rider` and candidate `Driver`s directly as method parameters.
- `Ride` exposes behavior methods (`assignDriver`, `complete`, `cancel`) so
  callers tell a `Ride` what happened rather than reaching into its fields and
  mutating them externally.

## DRY / KISS / YAGNI

- **DRY**: driver-matching and fare logic each live in exactly one place per
  algorithm; `RideService` has one orchestration path for all strategies
  rather than duplicated `if (strategyName.equals(...))` branches.
- **KISS**: entities are plain data + behavior, no speculative fields (e.g. no
  `rating`, `paymentMethod`, etc. not required by the brief).
- **YAGNI**: no persistence layer, no REST API, no authentication — only what
  the console-based MVP needs. Spring is used for DI only
  (`web-application-type=none`), not as a springboard for unrequested web
  features.
