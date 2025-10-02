# InfoBus SPARQL - Quick Start Guide

## Prerequisites
- Java 11+
- Maven 3.6+

## Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/simovilab/infobus-sparql.git
cd infobus-sparql
```

### 2. Build the project
```bash
mvn clean package
```

### 3. Start the server
```bash
# Option A: Using the start script (recommended)
./start-server.sh

# Option B: Direct Java execution
java -jar target/infobus-sparql-1.0.0-SNAPSHOT.jar
```

The server will start on **http://localhost:3030**

## Quick Commands

### Query the ontology
```bash
# Count all GTFS classes
curl -X POST http://localhost:3030/infobus/sparql \
  --data-urlencode 'query=PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
SELECT (COUNT(?class) as ?count) WHERE { ?class rdf:type owl:Class }' \
  -H "Accept: application/sparql-results+json"
```

### Insert sample data
```bash
# Load the example transit data
curl -X POST http://localhost:3030/infobus/update \
  --data-urlencode "update=$(cat examples/sample-data.ttl)" \
  -H "Content-Type: application/sparql-update"
```

### Query sample data
```bash
# List all agencies
curl -X POST http://localhost:3030/infobus/sparql \
  --data-urlencode 'query=PREFIX gtfs: <http://vocab.gtfs.org/terms#>
SELECT ?agency ?name WHERE { 
  ?agency a gtfs:Agency . 
  ?agency gtfs:agencyName ?name 
}' \
  -H "Accept: application/sparql-results+json"
```

## Available Endpoints

| Endpoint | Purpose | Method |
|----------|---------|--------|
| `/infobus/sparql` | SPARQL queries | GET/POST |
| `/infobus/update` | SPARQL updates | POST |
| `/infobus/data` | Graph store | GET/POST/PUT/DELETE |

## Ontology Files

| File | Size | Description |
|------|------|-------------|
| `gtfs.ttl` | 7.8KB | Core GTFS classes & properties |
| `stops.ttl` | 5.1KB | Stop locations & facilities |
| `trips.ttl` | 6.6KB | Trip scheduling & operations |
| `routes.ttl` | 5.4KB | Transit routes |
| `calendar.ttl` | 6.2KB | Service schedules |
| `agency.ttl` | 4.8KB | Transit agencies |
| `shapes.ttl` | 3.9KB | Route shapes & paths |
| `fares.ttl` | 6.1KB | Fare information |

**Total: 8 files, ~50KB, 1,600+ lines**

## Common SPARQL Queries

### 1. List all stops
```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
SELECT ?stop ?name ?lat ?lon WHERE {
  ?stop a gtfs:Stop .
  ?stop gtfs:name ?name .
  OPTIONAL { ?stop gtfs:latitude ?lat }
  OPTIONAL { ?stop gtfs:longitude ?lon }
}
```

### 2. Find wheelchair accessible stops
```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
SELECT ?stop ?name WHERE {
  ?stop a gtfs:Stop .
  ?stop gtfs:name ?name .
  ?stop gtfs:wheelchairBoarding 1 .
}
```

### 3. Get trip schedule
```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
SELECT ?stop ?stopName ?arrival ?departure ?seq WHERE {
  ?stopTime a gtfs:StopTime .
  ?stopTime gtfs:trip <http://example.org/transit/trip1> .
  ?stopTime gtfs:stop ?stop .
  ?stopTime gtfs:arrivalTime ?arrival .
  ?stopTime gtfs:departureTime ?departure .
  ?stopTime gtfs:stopSequence ?seq .
  OPTIONAL { ?stop gtfs:name ?stopName }
} ORDER BY ?seq
```

### 4. List all routes by agency
```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
SELECT ?agency ?agencyName ?route ?routeName WHERE {
  ?route a gtfs:Route .
  ?route gtfs:agency ?agency .
  ?route gtfs:routeLongName ?routeName .
  ?agency gtfs:agencyName ?agencyName .
}
```

## Project Structure
```
infobus-sparql/
├── ontology/          # 8 GTFS ontology files
├── examples/          # Sample data & queries
├── config/            # Fuseki configuration
├── src/               # Java source code
├── README.md          # Full documentation
├── SUMMARY.md         # Project summary
└── start-server.sh    # Quick start script
```

## Need Help?

- **Full documentation**: See [README.md](README.md)
- **Project summary**: See [SUMMARY.md](SUMMARY.md)
- **Example queries**: See [examples/queries.md](examples/queries.md)
- **Sample data**: See [examples/sample-data.ttl](examples/sample-data.ttl)

## References
- GTFS Spec: https://gtfs.org/reference/static
- Apache Jena: https://jena.apache.org/
- SPARQL: https://www.w3.org/TR/sparql11-query/

## License
Apache License 2.0
