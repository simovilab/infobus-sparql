# InfoBus SPARQL

SPARQL endpoint for transit data using Apache Jena Fuseki and GTFS ontologies.

## Overview

This project provides a SPARQL endpoint for querying transit data based on the General Transit Feed Specification (GTFS). It uses Apache Jena Fuseki as the triplestore and includes a comprehensive set of GTFS ontologies.

## Features

- **Apache Jena Fuseki Server**: High-performance RDF triplestore with SPARQL endpoint
- **GTFS Ontologies**: Comprehensive ontologies covering all GTFS data elements:
  - `gtfs.ttl` - Core GTFS ontology with base classes and properties
  - `stops.ttl` - Ontology for stop locations, stations, and facilities
  - `trips.ttl` - Ontology for trip scheduling and operations
  - `routes.ttl` - Ontology for transit routes
  - `calendar.ttl` - Ontology for service schedules and calendars
  - `agency.ttl` - Ontology for transit agencies
  - `shapes.ttl` - Ontology for route shapes and geographic paths
  - `fares.ttl` - Ontology for fare information and pricing rules
- **TDB2 Storage**: Efficient persistent storage using Apache Jena TDB2
- **Auto-loading**: Automatically loads ontologies from the `ontology/` directory on startup

## Project Structure

```
infobus-sparql/
├── ontology/           # GTFS ontology files (.ttl)
│   ├── gtfs.ttl
│   ├── stops.ttl
│   ├── trips.ttl
│   ├── routes.ttl
│   ├── calendar.ttl
│   ├── agency.ttl
│   ├── shapes.ttl
│   └── fares.ttl
├── data/
│   └── tdb2/          # TDB2 triplestore data (auto-created)
├── config/
│   └── fuseki-config.ttl  # Fuseki server configuration
├── src/
│   └── main/java/
│       └── com/simovilab/infobus/
│           └── FusekiServer.java
└── pom.xml            # Maven project configuration
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Build the Project

```bash
mvn clean package
```

This will compile the code and create an executable JAR file in the `target/` directory.

### 2. Run the Server

```bash
java -jar target/infobus-sparql-1.0.0-SNAPSHOT.jar
```

Or run directly with Maven:

```bash
mvn exec:java -Dexec.mainClass="com.simovilab.infobus.FusekiServer"
```

### 3. Access the Endpoints

Once the server is running, the following endpoints are available:

- **SPARQL Query**: `http://localhost:3030/infobus/sparql`
- **SPARQL Update**: `http://localhost:3030/infobus/update`
- **Graph Store**: `http://localhost:3030/infobus/data`
- **Fuseki UI**: `http://localhost:3030/` (if using full Fuseki server)

## Using the SPARQL Endpoint

### Example Query

Query all GTFS classes defined in the ontologies:

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?class ?label ?comment
WHERE {
  ?class rdf:type owl:Class .
  ?class rdfs:label ?label .
  OPTIONAL { ?class rdfs:comment ?comment }
  FILTER(STRSTARTS(STR(?class), "http://vocab.gtfs.org/terms#"))
}
ORDER BY ?label
```

### Inserting Data

You can insert transit data using SPARQL UPDATE:

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
PREFIX ex: <http://example.org/transit/>

INSERT DATA {
  ex:agency1 a gtfs:Agency ;
    gtfs:agencyId "METRO" ;
    gtfs:agencyName "Metro Transit" ;
    gtfs:agencyUrl <http://metro.example.org> ;
    gtfs:agencyTimezone "America/New_York" .
}
```

## Ontology Documentation

### Core GTFS Ontology (gtfs.ttl)

Defines the base classes and properties for GTFS data:
- Classes: Agency, Stop, Route, Trip, StopTime, Calendar, etc.
- Properties: Identifiers, names, relationships
- Route types: Bus, Subway, Rail, Ferry, etc.

### Stops Ontology (stops.ttl)

Extended ontology for stop locations:
- Stop location types (platform, station, entrance, etc.)
- Geographical properties
- Accessibility features
- Amenities (shelters, benches, ticket machines, etc.)

### Trips Ontology (trips.ttl)

Extended ontology for trip operations:
- Trip properties (headsign, direction, block ID)
- Stop time properties (pickup/dropoff types)
- Trip status (scheduled, delayed, canceled)
- Accessibility information

### Routes Ontology (routes.ttl)

Extended ontology for transit routes:
- Route properties (colors, URLs, descriptions)
- Route classifications (local, express, rapid transit)
- Route patterns

### Calendar Ontology (calendar.ttl)

Extended ontology for service schedules:
- Service availability by day of week
- Date ranges for service
- Calendar exceptions (holidays, special events)
- Frequency-based service

### Agency Ontology (agency.ttl)

Extended ontology for transit agencies:
- Agency contact information
- Service areas
- Agency relationships (parent/subsidiary)

### Shapes Ontology (shapes.ttl)

Extended ontology for route shapes:
- Shape points and sequences
- Geographic coordinates
- Distance calculations
- GeoJSON and WKT representations

### Fares Ontology (fares.ttl)

Extended ontology for fare information:
- Fare attributes and pricing
- Payment methods
- Fare rules and zones
- Discount types and fare products

## Development

### Adding More Ontologies

1. Create a new `.ttl` file in the `ontology/` directory
2. Define your RDF classes and properties using Turtle syntax
3. Restart the server - ontologies are auto-loaded

### Custom Configuration

Edit `config/fuseki-config.ttl` to customize:
- Dataset location
- Service endpoints
- Security settings
- Query timeouts

## License

Apache License 2.0 - See LICENSE file for details

## Contributing

Contributions are welcome! Please submit pull requests or open issues for bugs and feature requests.

## Resources

- [GTFS Reference](https://gtfs.org/reference/static)
- [Apache Jena Documentation](https://jena.apache.org/documentation/)
- [SPARQL 1.1 Query Language](https://www.w3.org/TR/sparql11-query/)
- [RDF 1.1 Turtle](https://www.w3.org/TR/turtle/)
