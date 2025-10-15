# --- Build stage: compile shaded JAR ---
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /workspace

# Pre-fetch dependencies (improves build cache)
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy sources and resources
COPY src ./src
COPY ontology ./ontology

# Build shaded JAR
RUN mvn -B -DskipTests package

# --- Runtime stage: slim JRE image ---
FROM openjdk:11-jre-slim

ENV APP_HOME=/app \
	JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -XX:+UseContainerSupport"

WORKDIR ${APP_HOME}

# Install curl for healthchecks
RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl ca-certificates \
	&& rm -rf /var/lib/apt/lists/*

# Copy application JAR and ontologies
COPY --from=build /workspace/target/infobus-sparql-1.0.0-SNAPSHOT.jar ${APP_HOME}/infobus-sparql.jar
COPY --from=build /workspace/ontology ${APP_HOME}/ontology

# Create data directory for TDB2 persistence (matches code path: data/tdb2)
RUN mkdir -p ${APP_HOME}/data/tdb2

EXPOSE 3030

# Mark data dir as a volume for persistence by default
VOLUME ["/app/data"]

# Default command
CMD ["java", "-jar", "infobus-sparql.jar"]

