# InfoBus SPARQL - Project Summary

## Overview
This project provides a complete Apache Jena Fuseki triplestore setup for managing and querying GTFS (General Transit Feed Specification) transit data using SPARQL.

## Key Components

### 1. Build System
- **pom.xml**: Maven configuration with Apache Jena Fuseki dependencies
- Java 11 compatible
- Creates executable uber-jar with all dependencies

### 2. Source Code
- **FusekiServer.java**: Main server class that:
  - Starts Fuseki on port 3030
  - Automatically loads all .ttl files from ontology directory
  - Uses TDB2 for persistent RDF storage
  - Exposes SPARQL query, update, and data endpoints

### 3. GTFS Ontologies (8 files)
Comprehensive semantic models covering all GTFS specification aspects:

1. **gtfs.ttl** (7.8KB)
   - Core ontology with base classes and properties
   - Agency, Stop, Route, Trip, StopTime, Calendar classes
   - Route types (Bus, Subway, Rail, Ferry, etc.)
   - 30+ classes and properties

2. **stops.ttl** (5.1KB)
   - Stop location types (platform, station, entrance, etc.)
   - Geographical properties (lat/lon)
   - Accessibility features
   - Amenities (shelters, benches, elevators, etc.)

3. **trips.ttl** (6.6KB)
   - Trip scheduling and operations
   - Stop time properties
   - Pickup/drop-off types
   - Trip status (scheduled, delayed, canceled)
   - Direction and accessibility information

4. **routes.ttl** (5.4KB)
   - Route properties (colors, names, descriptions)
   - Extended route types (trolleybus, monorail)
   - Route classifications (local, express, rapid transit)
   - Route patterns

5. **calendar.ttl** (6.2KB)
   - Service schedules by day of week
   - Date ranges and exceptions
   - Service patterns (weekday, weekend, daily)
   - Frequency-based service

6. **agency.ttl** (4.8KB)
   - Agency contact information
   - Service areas
   - Agency relationships (parent/subsidiary)
   - Uses FOAF ontology extensions

7. **shapes.ttl** (3.9KB)
   - Route shapes and geographic paths
   - Shape points with coordinates
   - Distance calculations
   - GeoJSON and WKT representations

8. **fares.ttl** (6.1KB)
   - Fare attributes and pricing
   - Payment methods (on-board, before boarding)
   - Fare rules and zones
   - Discount types (senior, youth, student, disability)
   - Fare products (single ride, day pass, weekly/monthly pass)

### 4. Configuration
- **config/fuseki-config.ttl**: Fuseki server configuration
  - TDB2 dataset configuration
  - Service endpoint definitions
  - Union default graph enabled

### 5. Examples
- **examples/sample-data.ttl**: Sample GTFS data including:
  - Transit agency
  - Route with 3 stops
  - Trip with stop times
  - Calendar service
  - Fare information

- **examples/queries.md**: 10+ example SPARQL queries:
  - List classes and properties
  - Query agencies, routes, stops
  - Find wheelchair accessible stops
  - Get trip schedules
  - Query fares
  - Count entities by type

### 6. Utilities
- **start-server.sh**: Convenience script to build and start server
- **.gitignore**: Excludes build artifacts and data directory

## Technical Specifications

### Storage
- **TDB2**: High-performance persistent RDF storage
- Data stored in `data/tdb2/` directory
- Supports transactions and concurrent access

### Endpoints (default: http://localhost:3030)
- `/infobus/sparql` - SPARQL query endpoint
- `/infobus/update` - SPARQL update endpoint
- `/infobus/data` - Graph store HTTP protocol

### Namespaces
- `gtfs:` = http://vocab.gtfs.org/terms#
- `geo:` = http://www.w3.org/2003/01/geo/wgs84_pos#
- Standard RDF, RDFS, OWL, XSD namespaces

## Usage

### Building
```bash
mvn clean package
```

### Running
```bash
# Option 1: Direct Java
java -jar target/infobus-sparql-1.0.0-SNAPSHOT.jar

# Option 2: Using script
./start-server.sh

# Option 3: Maven exec
mvn exec:java -Dexec.mainClass="com.simovilab.infobus.FusekiServer"
```

### Querying
```bash
curl -X POST http://localhost:3030/infobus/sparql \
  --data-urlencode 'query=SELECT * WHERE { ?s ?p ?o } LIMIT 10' \
  -H "Accept: application/sparql-results+json"
```

## Statistics
- **Total ontology size**: ~50KB (8 files)
- **Classes defined**: 30+
- **Properties defined**: 100+
- **Build artifact size**: ~27MB (includes all dependencies)
- **Startup time**: ~1-2 seconds
- **Memory usage**: Minimal (scales with data size)

## Project Structure
```
infobus-sparql/
├── src/main/java/com/simovilab/infobus/
│   └── FusekiServer.java          # Main server class
├── ontology/                       # GTFS ontology files
│   ├── gtfs.ttl                   # Core ontology
│   ├── stops.ttl                  # Stops & facilities
│   ├── trips.ttl                  # Trips & schedules
│   ├── routes.ttl                 # Routes
│   ├── calendar.ttl               # Service calendars
│   ├── agency.ttl                 # Transit agencies
│   ├── shapes.ttl                 # Route shapes
│   └── fares.ttl                  # Fare information
├── config/
│   └── fuseki-config.ttl          # Server configuration
├── examples/
│   ├── sample-data.ttl            # Example transit data
│   └── queries.md                 # SPARQL query examples
├── data/tdb2/                     # TDB2 storage (auto-created)
├── pom.xml                        # Maven configuration
├── start-server.sh                # Startup script
├── .gitignore                     # Git ignore rules
└── README.md                      # Documentation
```

## Future Enhancements

Possible extensions:
1. GTFS Realtime support (vehicle positions, alerts)
2. Web UI for data exploration
3. REST API for common queries
4. Data import tools from GTFS CSV files
5. Geospatial queries with GeoSPARQL
6. Integration with mapping libraries
7. Authentication and authorization
8. Data validation rules with SHACL
9. Performance optimizations for large datasets
10. Docker containerization

## License
Apache License 2.0

## References
- GTFS Specification: https://gtfs.org/reference/static
- Apache Jena: https://jena.apache.org/
- SPARQL 1.1: https://www.w3.org/TR/sparql11-query/
- RDF Turtle: https://www.w3.org/TR/turtle/
