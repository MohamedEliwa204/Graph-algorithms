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
        boolean[] inMST = new boolean[adjList.size()];
        int[] minWeights = new int[adjList.size()];
        Arrays.fill(minWeights, Integer.MAX_VALUE);
        Edge[] parentEdge = new Edge[adjList.size()];

        int startNode = 0;
        pq.offer(new Node<>(startNode, 0));
        minWeights[startNode] = 0;
        //O(V)
        while (!pq.isEmpty()){
            //O(lg(V))
            Node<Integer, Integer> n = pq.poll();
            int currentVertex = n.key();
            if (inMST[currentVertex]) {
                continue;
            }
            inMST[currentVertex] = true;
            if (parentEdge[currentVertex] != null) {
                primsEdges.add(parentEdge[currentVertex]);
            }
            List<Edge> adj = adjList.get(n.key());
            // O(E) for all V not considered as nested complexity
            for (Edge e: adj){
                if (!inMST[e.getV()] && e.getWeight() < minWeights[e.getV()]){
                    //O(1)
                    minWeights[e.getV()] =  e.getWeight();
                    parentEdge[e.getV()] = e;
                    //O(lg(V))
                    pq.offer(new Node<>(e.getV(), e.getWeight()));
                }
            }
        }

        return primsEdges;
    } // O(V*lg(V)) + O(V*lg(V)) + O(E*lg(V)) = O(E*lg(V))

    public List<Edge> kruskalMST(){

        List<Edge> kruskalEdges = new ArrayList<>();
        int V = adjList.size();;
        DisjointSet set = new DisjointSet(V);

        List<Edge> edges = new ArrayList<>();
        //O(E)
        for (List<Edge> adjacents : adjList.values()) {
            for (Edge e : adjacents) {
                // Only add one copy of the undirected edge
                if (e.getU() < e.getV()) {
                    edges.add(e);
                }
            }
        }
        //O(E*lg(E))
        edges.sort(Comparator.comparing(Edge::getWeight));
        //O(E)
        for (Edge e: edges){
            int u = e.getU();
            int v = e.getV();
            // O(1) with path compression
            if (!set.connected(u, v)){
                set.union(u, v);
                kruskalEdges.add(e);
                if (kruskalEdges.size() == V - 1) {
                    break;
                }
            }
        }
        return kruskalEdges;
    }

    public int[] dijkstra(int source){
        int[] distances = new int[adjList.size()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        PriorityQueue<Node<Integer, Integer>> pq = new PriorityQueue<>((a, b) -> a.value().compareTo(b.value()));
        boolean[] visited = new boolean[adjList.size()];


        pq.offer(new Node<>(source, 0));
        //O(V)
        while (!pq.isEmpty()){
            //O(lg(V))
            Node<Integer, Integer> n = pq.poll();
            int currentvertex = n.key();
            int currentDistance = n.value();
            if (visited[currentvertex]){
                continue;
            }
            visited[currentvertex] = true;
            // O(E) for all V not considered as nested complexity
            for (Edge e : adjList.get(n.key())){
                int possibleMinWeight = e.getWeight() + distances[currentvertex];
                if (!visited[e.getV()] && distances[e.getV()] > possibleMinWeight){

                    distances[e.getV()] = possibleMinWeight;
                    //O(lg(V))
                    pq.offer(new Node<>(e.getV(), possibleMinWeight));
                }
            }
        }
        return distances;
    }


    public int[] dagShortestPath(int source){
        int[] visited = new int[adjList.size()];

        Stack<Integer> st = new Stack<>();
        topSort(source, st, visited);
        int[] distances = new int[adjList.size()];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        while (!st.isEmpty()){
            int node = st.pop();
            if (distances[node] == Integer.MAX_VALUE){ // unreachable node
                continue;
            }

            for (Edge e : adjList.get(node)){
                int destination = e.getV();
                int possibleMinDist = distances[node] + e.getWeight();
                if (distances[destination] > possibleMinDist){
                    distances[destination] = possibleMinDist;
                }
            }
        }
        return distances;
    }


    private void topSort(int node, Stack<Integer> st, int[] visited){
        visited[node] = 1;
        for (Edge e : adjList.get(node)){
            if (visited[e.getV()] == 1){
                throw new IllegalStateException("Graph is not a DAG: Cycle detected!");
            }
            if (visited[e.getV()] == 0) {
                topSort(e.getV(), st, visited);
            }
        }
        visited[node] = 2;
        st.push(node);
    }
}