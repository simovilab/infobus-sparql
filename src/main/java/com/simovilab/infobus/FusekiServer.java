package com.simovilab.infobus;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb2.TDB2Factory;

import java.io.File;

/**
 * Main class to start Apache Jena Fuseki server with GTFS ontology
 */
public class FusekiServer {
    
    private static final int PORT = 3030;
    private static final String DATASET_PATH = "data/tdb2";
    private static final String ONTOLOGY_PATH = "ontology";
    
    public static void main(String[] args) {
        System.out.println("Starting InfoBus SPARQL Server...");
        
        // Create or open TDB2 dataset
        Dataset dataset = TDB2Factory.connectDataset(DATASET_PATH);
        
        // Load ontologies if they exist
        loadOntologies(dataset);
        
        // Build and start Fuseki server
        org.apache.jena.fuseki.main.FusekiServer server = org.apache.jena.fuseki.main.FusekiServer.create()
            .port(PORT)
            .add("/infobus", dataset)
            .build();
        
        System.out.println("Server starting on port " + PORT);
        System.out.println("SPARQL endpoint: http://localhost:" + PORT + "/infobus/sparql");
        System.out.println("SPARQL update: http://localhost:" + PORT + "/infobus/update");
        System.out.println("Dataset: http://localhost:" + PORT + "/infobus/data");
        
        server.start();
        server.join();
    }
    
    private static void loadOntologies(Dataset dataset) {
        File ontologyDir = new File(ONTOLOGY_PATH);
        
        if (!ontologyDir.exists() || !ontologyDir.isDirectory()) {
            System.out.println("Ontology directory not found: " + ONTOLOGY_PATH);
            return;
        }
        
        File[] ontologyFiles = ontologyDir.listFiles((dir, name) -> name.endsWith(".ttl"));
        
        if (ontologyFiles == null || ontologyFiles.length == 0) {
            System.out.println("No ontology files found in: " + ONTOLOGY_PATH);
            return;
        }
        
        System.out.println("Loading ontologies...");
        
        dataset.begin(org.apache.jena.query.ReadWrite.WRITE);
        try {
            Model defaultModel = dataset.getDefaultModel();
            
            for (File ontologyFile : ontologyFiles) {
                System.out.println("  Loading: " + ontologyFile.getName());
                Model ontologyModel = ModelFactory.createDefaultModel();
                RDFDataMgr.read(ontologyModel, ontologyFile.getAbsolutePath());
                defaultModel.add(ontologyModel);
            }
            
            dataset.commit();
            System.out.println("Ontologies loaded successfully");
        } catch (Exception e) {
            dataset.abort();
            System.err.println("Error loading ontologies: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataset.end();
        }
    }
}
