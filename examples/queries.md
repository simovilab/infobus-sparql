# Example SPARQL Queries for InfoBus SPARQL

## Query 1: List all GTFS classes defined in ontologies

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

## Query 2: List all properties for Stops

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?property ?label ?comment ?range
WHERE {
  ?property a ?propType .
  FILTER(?propType IN (owl:ObjectProperty, owl:DatatypeProperty))
  ?property rdfs:label ?label .
  OPTIONAL { ?property rdfs:comment ?comment }
  OPTIONAL { ?property rdfs:range ?range }
  {
    ?property rdfs:domain gtfs:Stop
  }
  UNION
  {
    ?property rdfs:label ?label .
    FILTER(CONTAINS(LCASE(?label), "stop"))
  }
}
ORDER BY ?label
```

## Query 3: List all agencies (sample data)

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT ?agency ?name ?url ?timezone
WHERE {
  ?agency a gtfs:Agency .
  ?agency gtfs:agencyName ?name .
  OPTIONAL { ?agency gtfs:agencyUrl ?url }
  OPTIONAL { ?agency gtfs:agencyTimezone ?timezone }
}
```

## Query 4: Find all routes and their operators

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?route ?shortName ?longName ?agency ?agencyName
WHERE {
  ?route a gtfs:Route .
  OPTIONAL { ?route gtfs:routeShortName ?shortName }
  OPTIONAL { ?route gtfs:routeLongName ?longName }
  OPTIONAL { 
    ?route gtfs:agency ?agency .
    ?agency gtfs:agencyName ?agencyName
  }
}
```

## Query 5: Find all stops with wheelchair accessibility

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?stop ?name ?lat ?lon
WHERE {
  ?stop a gtfs:Stop .
  ?stop gtfs:name ?name .
  ?stop gtfs:wheelchairBoarding 1 .
  ?stop gtfs:latitude ?lat .
  ?stop gtfs:longitude ?lon .
}
```

## Query 6: List trips on a specific route

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
PREFIX ex: <http://example.org/transit/>

SELECT ?trip ?headsign ?direction
WHERE {
  ?trip a gtfs:Trip .
  ?trip gtfs:route ex:route1 .
  OPTIONAL { ?trip gtfs:tripHeadsign ?headsign }
  OPTIONAL { ?trip gtfs:directionId ?direction }
}
```

## Query 7: Get stop times for a specific trip

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
PREFIX ex: <http://example.org/transit/>

SELECT ?stop ?stopName ?arrivalTime ?departureTime ?sequence
WHERE {
  ?stopTime a gtfs:StopTime .
  ?stopTime gtfs:trip ex:trip1 .
  ?stopTime gtfs:stop ?stop .
  ?stopTime gtfs:arrivalTime ?arrivalTime .
  ?stopTime gtfs:departureTime ?departureTime .
  ?stopTime gtfs:stopSequence ?sequence .
  OPTIONAL { ?stop gtfs:name ?stopName }
}
ORDER BY ?sequence
```

## Query 8: Find all fare information

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?fare ?fareId ?price ?currency ?transfers
WHERE {
  ?fare a gtfs:FareAttribute .
  ?fare gtfs:fareId ?fareId .
  ?fare gtfs:price ?price .
  ?fare gtfs:currencyType ?currency .
  OPTIONAL { ?fare gtfs:transfers ?transfers }
}
```

## Query 9: Find services operating on specific days

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?service ?serviceId ?startDate ?endDate
WHERE {
  ?service a gtfs:Calendar .
  ?service gtfs:serviceId ?serviceId .
  ?service gtfs:monday true .
  ?service gtfs:friday true .
  ?service gtfs:startDate ?startDate .
  ?service gtfs:endDate ?endDate .
}
```

## Query 10: Count entities by type

```sparql
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX gtfs: <http://vocab.gtfs.org/terms#>

SELECT ?type (COUNT(?entity) as ?count)
WHERE {
  ?entity rdf:type ?type .
  FILTER(STRSTARTS(STR(?type), "http://vocab.gtfs.org/terms#"))
  FILTER(?type != <http://www.w3.org/2002/07/owl#Class>)
}
GROUP BY ?type
ORDER BY DESC(?count)
```

## Using with curl

To execute a query via curl:

```bash
curl -X POST http://localhost:3030/infobus/sparql \
  --data-urlencode 'query=YOUR_SPARQL_QUERY_HERE' \
  -H "Accept: application/sparql-results+json"
```

## Inserting Data

To insert the sample data:

```bash
curl -X POST http://localhost:3030/infobus/update \
  --data-urlencode "update=@examples/sample-data.ttl"
```

Or use SPARQL UPDATE:

```sparql
PREFIX gtfs: <http://vocab.gtfs.org/terms#>
PREFIX ex: <http://example.org/transit/>

INSERT DATA {
  ex:newstop a gtfs:Stop ;
    gtfs:id "STOP4" ;
    gtfs:name "City Hall" ;
    gtfs:latitude 40.7128 ;
    gtfs:longitude -74.0060 .
}
```
