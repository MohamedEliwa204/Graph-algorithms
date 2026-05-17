package data.intensive.apps.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DisjointSetTest {
    private DisjointSet ds;

    @BeforeEach
    void setUp() {
        // Initialize a DisjointSet with vertices 1 through 5
        ds = new DisjointSet(5);
    }

    @Test
    void testInitialState() {
        // Initially, every vertex is its own root with a size of 1 (represented as -1)
        assertEquals(-1, ds.parent[1], "Vertex 1 should be a root with size 1 (-1)");
        assertEquals(-1, ds.parent[5], "Vertex 5 should be a root with size 1 (-1)");

        // Distinct vertices should not be connected
        assertFalse(ds.connected(1, 2), "1 and 2 should not be connected initially");
        assertTrue(ds.connected(3, 3), "A vertex should always be connected to itself");
    }

    @Test
    void testBasicUnionAndTransitiveConnectivity() {
        ds.union(1, 2);
        assertTrue(ds.connected(1, 2), "1 and 2 should be connected after union");

        ds.union(2, 3);
        assertTrue(ds.connected(1, 3), "1 and 3 should be transitively connected");

        assertFalse(ds.connected(1, 4), "1 and 4 should still be disconnected");
    }

    @Test
    void testUnionBySizeLogic() {

        ds.union(1, 2);
        int rootOfSetA = ds.find(1);
        assertEquals(-2, ds.parent[rootOfSetA], "Root size should be -2");

        // Vertex 3 is currently its own set of size 1.
        // When we union (2, 3), the smaller set (3) must attach to the larger set (1,2).
        ds.union(2, 3);

        assertEquals(rootOfSetA, ds.find(3), "3 should be attached to the existing larger root");
        assertEquals(-3, ds.parent[rootOfSetA], "The new combined root size should be -3");
    }

    @Test
    void testPathCompression() {

        ds.union(1, 2);
        ds.union(3, 4);

        ds.union(1, 3);

        assertEquals(3, ds.parent[4], "Before path compression, 4 should point to 3");

        // Call find() on 4. This must trigger path compression!
        int rootOf4 = ds.find(4);

        assertEquals(1, rootOf4, "The ultimate root of 4 should be 1");
        assertEquals(1, ds.parent[4], "Path compression failed: 4 should now point directly to 1");
    }

    @Test
    void testRedundantUnion() {
        ds.union(1, 2);
        int root = ds.find(1);
        int sizeBefore = ds.parent[root];
        ds.union(1, 2);

        assertEquals(sizeBefore, ds.parent[root], "Redundant union should not alter the set size");
    }
}