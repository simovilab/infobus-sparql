#!/bin/bash

# InfoBus SPARQL Server Startup Script

echo "Building InfoBus SPARQL..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "Starting InfoBus SPARQL Server..."
    echo "Press Ctrl+C to stop the server"
    echo ""
    java -jar target/infobus-sparql-1.0.0-SNAPSHOT.jar
    echo "Server started successfully."
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi
