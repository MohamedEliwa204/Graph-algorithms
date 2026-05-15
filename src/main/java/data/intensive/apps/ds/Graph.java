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

    public List<Edge> primMST() {
        List<Edge> primsEdges = new ArrayList<>();
        // heap
        PriorityQueue<Node<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.value().compareTo(b.value()));
        Set<Integer> keysInPQ  = new HashSet<>();
        Map<Integer, Integer> minWeights = new HashMap<>();
        Map<Integer, Edge> parentEdge = new HashMap<>();

        // O(V)
        for (Integer k : adjList.keySet()) {
            //O(lg(V))
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
        //O(V)
        while (!pq.isEmpty()){
            //O(lg(V))
            Node<Integer, Integer> n = pq.poll();
            if (!keysInPQ.contains(n.key())){
                continue;
            }
            keysInPQ.remove(n.key());
            List<Edge> adj = adjList.get(n.key());
            // O(E) for all V not considered as nested complexity
            for (Edge e: adj){
                if (keysInPQ.contains(e.getV()) && e.getWeight() < minWeights.get(e.getV())){
                    //O(1)
                    minWeights.put(e.getV(), e.getWeight());
                    parentEdge.put(e.getV(), e);
                    //O(lg(V))
                    pq.offer(new Node<>(e.getV(), e.getWeight()));
                }
            }
        }
        //O(E)
        parentEdge.forEach((nodeId, parent) -> primsEdges.add(parent));
        return primsEdges;
    } // O(V*lg(V)) + O(V*lg(V)) + O(E*lg(V)) = O(E*lg(V))

    public List<Edge> kurskalMST(){

        List<Edge> kurskalEdges = new ArrayList<>();
        List<Integer> vertices = new ArrayList<>();
        vertices.addAll(adjList.keySet());
        DisjointSet set = new DisjointSet(vertices);

        List<Edge> edges = new ArrayList<>();
        for (List<Edge> adjacents : adjList.values()) {
            for (Edge e : adjacents) {
                // Only add one copy of the undirected edge
                if (e.getU() < e.getV()) {
                    edges.add(e);
                }
            }
        }
        edges.sort(Comparator.comparing(Edge::getWeight));

        for (Edge e: edges){
            int u = e.getU();
            int v = e.getV();
            if (!set.connected(u, v)){
                set.union(u, v);
                kurskalEdges.add(e);
                if (kurskalEdges.size() == vertices.size() - 1) {
                    break;
                }
            }
        }
        return kurskalEdges;
    }

}