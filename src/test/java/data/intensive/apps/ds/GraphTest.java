package data.intensive.apps.ds;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private Graph undirectedGraph;
    private Graph directedAcyclicGraph;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        undirectedGraph = new Graph();
        directedAcyclicGraph = new Graph();
    }

    @Test
    public void testMinimumSpanningTrees(){
        undirectedGraph.addEdge(0, 1, 1);
        undirectedGraph.addEdge(1, 2, 2);
        undirectedGraph.addEdge(2, 3, 3);
        undirectedGraph.addEdge(0, 3, 4);
        undirectedGraph.addEdge(0, 2, 5);

        List<Edge> primEdges = undirectedGraph.primMST();
        List<Edge> kruskalEdges = undirectedGraph.kruskalMST();

        assertEquals(3, primEdges.size(), "Prim's should return V-1 edges");
        assertEquals(3, kruskalEdges.size(), "Kruskal's should return V-1 edges");

        int primTotalWeight = primEdges.stream().mapToInt(Edge::getWeight).sum();
        int kruskalTotalWeight = kruskalEdges.stream().mapToInt(Edge::getWeight).sum();
        assertEquals(6, primTotalWeight, "Prim's total weight should be 6");
        assertEquals(6, kruskalTotalWeight, "Kruskal's total weight should be 6");
    }

    @Test
    public void testDijkstraShortestPath(){
        undirectedGraph.addEdge(0, 1, 1);
        undirectedGraph.addEdge(0, 2, 4);
        undirectedGraph.addEdge(1, 2, 2);
        undirectedGraph.addEdge(1, 3, 6);
        undirectedGraph.addEdge(2, 3, 3);

        int[] distances = undirectedGraph.dijkstra(0);
        assertEquals(0, distances[0]);
        assertEquals(1, distances[1]);
        assertEquals(3, distances[2]);
        assertEquals(6, distances[3]);
    }

    @Test
    public void testDagShortestPath(){
        directedAcyclicGraph.addDirectedEdge(0, 1, 5);
        directedAcyclicGraph.addDirectedEdge(0, 2, 3);
        directedAcyclicGraph.addDirectedEdge(1, 3, 6);
        directedAcyclicGraph.addDirectedEdge(2, 1, 2); // 0->2->1 = 5
        directedAcyclicGraph.addDirectedEdge(2, 3, 7);
        directedAcyclicGraph.addDirectedEdge(2, 4, 4);
        directedAcyclicGraph.addDirectedEdge(3, 4, -1); // DAGs can handle negative weights! and in this specific case dijkstra will not fail.
        directedAcyclicGraph.addDirectedEdge(4, 5, -2);

        int[] expectedDistances = {0, 5, 3, 10, 7, 5};
        int[] dagDistances = directedAcyclicGraph.dagShortestPath(0);
        int[] dijkstraDistances = directedAcyclicGraph.dijkstra(0);

        assertArrayEquals(expectedDistances, dagDistances, "DAG algorithm failed to find correct shortest paths");
        assertArrayEquals(expectedDistances, dijkstraDistances, "Dijkstra failed on this specific DAG");
    }


    @Test
    public void testDagCycleDetection(){
        directedAcyclicGraph.addDirectedEdge(0, 1, 1);
        directedAcyclicGraph.addDirectedEdge(1, 2, 1);
        directedAcyclicGraph.addDirectedEdge(2, 0, 1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            directedAcyclicGraph.dagShortestPath(0);
        });

        assertEquals("Graph is not a DAG: Cycle detected!", exception.getMessage());
    }

    @Test
    public void testDijkstraFailsOnNegativeWeights(){
        directedAcyclicGraph.addDirectedEdge(0, 1, 3);
        directedAcyclicGraph.addDirectedEdge(0, 2, 4);
        directedAcyclicGraph.addDirectedEdge(1, 3, 4);
        directedAcyclicGraph.addDirectedEdge(2, 1, -2);

        int[] dijkstraDistances = directedAcyclicGraph.dijkstra(0);
        int[] dagDistances = directedAcyclicGraph.dagShortestPath(0);

        // Prove Dijkstra gets it WRONG (returns 7 for node 3)
        assertEquals(7, dijkstraDistances[3], "Dijkstra is tricked by the negative edge");
        assertEquals(3, dijkstraDistances[1], "Dijkstra wrongly finalizes node 1 too early");

        // Prove DAG algorithm gets it RIGHT (returns 6 for node 3)
        assertEquals(6, dagDistances[3], "DAG algorithm correctly uses the negative edge");
        assertEquals(2, dagDistances[1], "DAG algorithm correctly updates node 1");
    }

    @Test
    public void testDisconnectedGraphMST(){
        undirectedGraph.addEdge(0, 1, 1);
        undirectedGraph.addEdge(2, 3, 2);

        List<Edge> primEdges = undirectedGraph.primMST();
        List<Edge> kruskalEdges = undirectedGraph.kruskalMST();

        // Total vertices = 4, so a valid MST needs 3 edges.
        assertTrue(primEdges.size() < 3, "Prim's cannot form a full MST on disconnected graph");
        assertTrue(kruskalEdges.size() < 3, "Kruskal's cannot form a full MST on disconnected graph");

        // Prim's only explores the component it starts in
        assertEquals(1, primEdges.size(), "Prim's only maps the connected component of the start node");
        // Kruskal's evaluates globally, creating a forest
        assertEquals(2, kruskalEdges.size(), "Kruskal's builds a forest across all components");
    }

    @Test
    public void testMstWithIdenticalWeights(){
        undirectedGraph.addEdge(0, 1, 1);
        undirectedGraph.addEdge(1, 2, 1);
        undirectedGraph.addEdge(2, 3, 1);
        undirectedGraph.addEdge(3, 0, 1);

        List<Edge> primEdges = undirectedGraph.primMST();
        List<Edge> kruskalEdges = undirectedGraph.kruskalMST();

        assertEquals(3, primEdges.size());
        assertEquals(3, kruskalEdges.size());

        int primTotal = primEdges.stream().mapToInt(Edge::getWeight).sum();
        int kruskalTotal = kruskalEdges.stream().mapToInt(Edge::getWeight).sum();

        assertEquals(3, primTotal, "Prim's should handle identical weights without cycles");
        assertEquals(3, kruskalTotal, "Kruskal's should handle identical weights without cycles");
    }

    @Test
    public void testUnreachableNodesInSSSP() {

        directedAcyclicGraph.addDirectedEdge(0, 1, 5);
        directedAcyclicGraph.addDirectedEdge(2, 3, 10); // Isolated edge

        int[] dijkstraDistances = directedAcyclicGraph.dijkstra(0);
        int[] dagDistances = directedAcyclicGraph.dagShortestPath(0);

        // Distance to unreachable node 2 should remain Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE, dijkstraDistances[2], "Dijkstra should leave unreachable nodes at MAX_VALUE");
        assertEquals(Integer.MAX_VALUE, dagDistances[2], "DAG algorithm should leave unreachable nodes at MAX_VALUE");
    }
}


