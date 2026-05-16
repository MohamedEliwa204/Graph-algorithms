package data.intensive.apps.inputDistribution;

import data.intensive.apps.ds.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DataGeneration {

    public static Graph sparseGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        int V = 5000;
        // prevent parallel edges.
        Set<String> existingEdges = new HashSet<>();
        // generate connected graph first
        for (int i = 1; i < V; i++) {
            int parent = random.nextInt(i);
            int weight = random.nextInt(1000) + 1;
            graph.addEdge(i, parent, weight);
            existingEdges.add(parent + "-" + i);
        }
        int remainingEdges = 25000 - (V - 1);

        for (int i = 0; i < remainingEdges; i++) {
            int u = random.nextInt(V);
            int destination = random.nextInt(V);

            if (u == destination){ // prevent self loops
                i--;
                continue;
            }
            int min = Math.min(u, destination);
            int max = Math.max(u, destination);
            String edgeKey = min + "-" + max;
            if (!existingEdges.contains(edgeKey)){
                existingEdges.add(edgeKey);
                int weight = random.nextInt(1000) + 1;
                graph.addEdge(u, destination, weight);
            }else{
                i--;

            }
        }
        return graph;
    }

    public static Graph denseGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<String> existingEdges = new HashSet<>();
        int V = 5000;
        for (int u = 0; u < V; u++) {
            for (int destination = u + 1; destination < V; destination++) {
                // give each edge a 25% chance of actually being created
                if (random.nextDouble() < 0.25) { // law of large numbers guarantee that roughly 25% of overall number of edges.
                    int weight = random.nextInt(1000) + 1;
                    graph.addEdge(u, destination, weight);
                }
            }
        }
        return graph;
    }

    public static Graph completedGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        int V = 5000;
        for (int u = 0; u < V; u++) {
            for (int destination = u + 1; destination < V; destination++) {
                int weight = random.nextInt(1000) + 1;
                graph.addEdge(u, destination, weight);
            }
        }
        return graph;

    }

    public static Graph directedAcyclicGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<String> existingEdges = new HashSet<>();
        int V = 5000;
        for (int i = 1; i < 5000; i++) {
            int parent = random.nextInt(i);
            int weight = random.nextInt(1000) + 1;
            graph.addDirectedEdge(parent, i, weight);
            existingEdges.add(parent + "->" + i);
        }

        int remainingEdges = 25000 - (V - 1);
        for (int i = 0; i < remainingEdges; i++) {
            int u = random.nextInt(V - 1); // last edge in DAG has no out edges
            int destination = random.nextInt(V - u - 1) + u + 1; // generate edges forward to prevent cycles

            String edgeKey = u + "->" + destination;
            if (!existingEdges.contains(edgeKey)){
                existingEdges.add(edgeKey);
                int weight = random.nextInt(1000) + 1;
                graph.addDirectedEdge(u, destination, weight);
            }else{
                i--;
            }
        }
        return graph;
    }

}
