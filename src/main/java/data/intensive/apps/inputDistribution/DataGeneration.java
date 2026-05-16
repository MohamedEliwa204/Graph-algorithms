package data.intensive.apps.inputDistribution;

import data.intensive.apps.ds.Edge;
import data.intensive.apps.ds.Graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DataGeneration {

    public static Graph sparseGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<Integer> connected = new HashSet<>();

        for (int i = 0; i < 5000; i++) {
            int vertex = i;
            int numberOfEdges = random.nextInt(10) + 1;
            boolean[] visited = new boolean[5000];
            visited[vertex] = true;
            for (int j = 0; j < numberOfEdges; j++) {
                int distenation;
                do {
                    distenation = random.nextInt(5000);
                } while (visited[distenation] || (connected.contains(distenation) && connected.size() < 4999)); // to prevent self loops and parallel edges
                visited[distenation] = true;
                connected.add(distenation);
                int weight = random.nextInt(1000) + 1;
                graph.addEdge(vertex, distenation, weight);

            }
        }
        return graph;
    }

    public static Graph denseGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<Integer> connected = new HashSet<>();
        for (int i = 0; i < 5000; i++) {
            int vertex = i;
            int numberOfEdges = 1250;
            boolean[] visited = new boolean[5000];
            visited[vertex] = true;
            for (int j = 0; j < numberOfEdges; j++) {
                int distenation;
                do {
                    distenation = random.nextInt(5000);
                } while (visited[distenation] || (connected.contains(distenation) && connected.size() < 4999)); // to prevent self loops and parallel edges
                visited[distenation] = true;
                connected.add(distenation);
                int weight = random.nextInt(1000) + 1;
                graph.addEdge(vertex, distenation, weight);
            }
        }
        return graph;
    }

    public static Graph completedGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<Integer> connected = new HashSet<>();
        for (int i = 0; i < 5000; i++) {
            int vertex = i;
            int numberOfEdges = 4999;
            boolean[] visited = new boolean[4999];
            visited[vertex] = true;
            for (int j = 0; j < numberOfEdges; j++) {
                int distenation;
                do {
                    distenation = random.nextInt(5000);
                } while (visited[distenation] || (connected.contains(distenation) && connected.size() < 4999)); // to prevent self loops and parallel edges
                visited[distenation] = true;
                connected.add(distenation);
                int weight = random.nextInt(1000) + 1;
                graph.addEdge(vertex, distenation, weight);
            }
        }
        return graph;
    }

    public static Graph directedAcyclicGraph(){
        Random random = new Random(42);
        Graph graph = new Graph();
        Set<Integer> connected = new HashSet<>();
        for (int i = 0; i < 4999; i++) { // last vertex has no edges out of it in DAG

            int vertex = i;
            int numberOfEdges;
            if (i < 4900){
                numberOfEdges = random.nextInt(10) + 1;
            }else{
                numberOfEdges = random.nextInt(5000 - i) + 1;
            }
            boolean[] visited = new boolean[5000];
            visited[vertex] = true;
            for (int j = 0; j < numberOfEdges; j++) {
                int distenation;
                do {
                    distenation = random.nextInt(5000 - i) + i; // to prevent cycles, make edges forward.
                } while (visited[distenation] || (connected.contains(distenation) && connected.size() < 4999)); // to prevent self loops and parallel edges
                visited[distenation] = true;
                connected.add(distenation);
                int weight = random.nextInt(1000) + 1;
                graph.addDirectedEdge(vertex, distenation, weight);
            }
        }
        return graph;
    }

}
