package data.intensive.apps.ds;

import java.util.*;

public class Graph {
    private Map<Integer, List<Edge>> adjList;

    public Graph() {
        adjList = new HashMap<>();
    }

    public void addEdge(int u, int v, int weight) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(u, v, weight));
        adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(v, u, weight));
    }

    public void addDirectedEdge(int u, int v, int weight) {
        adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(u, v, weight));
        adjList.putIfAbsent(v, new ArrayList<>()); // to ensure the list have all vertices.
    }
    private record Node<K, V extends Comparable<V>>(K key, V value) {}

    public List<Edge> prims() {
        List<Edge> primsEdges = new ArrayList<>();

        PriorityQueue<Node<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.value().compareTo(b.value()));
        Set<Integer> keysInPQ  = new HashSet<>();
        Map<Integer, Integer> minWeights = new HashMap<>();
        Map<Integer, Edge> parentEdge = new HashMap<>();

        for (Integer k : adjList.keySet()) {
            pq.offer(new Node<>(k, Integer.MAX_VALUE));
            keysInPQ.add(k);
            minWeights.put(k, Integer.MAX_VALUE);
        }

        Node<Integer,  Integer> firstNode = pq.poll();
        if (firstNode == null){
            throw new RuntimeException("Graph is empty!");
        }

        Node<Integer, Integer> node = new Node<>(firstNode.key(), 0);
        pq.offer(node);
        while (!pq.isEmpty()){
            Node<Integer, Integer> n = pq.poll();
            if (!keysInPQ.contains(n.key())){
                continue;
            }
            keysInPQ.remove(n.key());
            List<Edge> adj = adjList.get(n.key());

            for (Edge e: adj){
                if (keysInPQ.contains(e.getV()) && e.getWeight() < minWeights.get(e.getV())){
                    minWeights.put(e.getV(), e.getWeight());
                    parentEdge.put(e.getV(), e);
                    pq.offer(new Node<>(e.getV(), e.getWeight()));
                }
            }
        }
        parentEdge.forEach((nodeId, parent) -> primsEdges.add(parent));
        return primsEdges;
    }
}