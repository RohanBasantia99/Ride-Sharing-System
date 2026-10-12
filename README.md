# RideWise

A simplified, console-based ride-sharing system demonstrating SOLID, the
Strategy pattern, composition over inheritance, and Spring-managed dependency
injection.

## Build & Run

```bash
./mvnw clean package
java -jar target/ridewise-1.0.0.jar
```

or run directly via the Spring Boot plugin:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` in place of `./mvnw`.

## Run tests

```bash
./mvnw test
```

See [docs/Requirements.md](docs/Requirements.md), [docs/Class_Model.md](docs/Class_Model.md),
[docs/SOLID_Reflection.md](docs/SOLID_Reflection.md) and
[docs/Object_Relationships.md](docs/Object_Relationships.md) for the design
writeup.

## Branching

`main` is the stable branch; changes are proposed through pull requests from `staging`.
