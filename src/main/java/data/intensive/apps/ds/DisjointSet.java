package data.intensive.apps.ds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisjointSet {
    // <child, parent>
    int[] parent;


    public DisjointSet(int numVertices){
        parent = new int[numVertices + 1];
        Arrays.fill(parent, -1);
    }

    public int find(int i){
        if (parent[i] < 0) {
            return i;
        }
        parent[i] = find(parent[i]);
        return parent[i];
    }

    public void union(int i, int j){
        int rooti = find(i);
        int rootj = find(j);
        if (rooti == rootj) {
            return;
        }
        int numberOfElementInSeti = parent[rooti];
        int numberOfElementInSetj = parent[rootj];
        if (numberOfElementInSeti <= numberOfElementInSetj){ // more negative has more elements.
            parent[rooti]  = numberOfElementInSeti + numberOfElementInSetj;
            parent[rootj] =  rooti;
        }else {
            parent[rootj] =  numberOfElementInSeti + numberOfElementInSetj;
            parent[rooti] =  rootj;
        }
    }

    public boolean connected(int i, int j){
        return find(i) == find(j);
    }

}
