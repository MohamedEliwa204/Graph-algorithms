package data.intensive.apps.ds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisjointSet {
    // <child, parent>
    Map<Integer, Integer> parent = new HashMap<>();


    public DisjointSet(List<Integer> vertices){
        for (Integer v : vertices){
            parent.put(v, -1);
        }
    }

    public int find(int i){
        int p = parent.get(i);
        if (p < 0){
            return i;
        }
        int root = find(p);
        // path Compression; to make it close to O(1)!
        parent.put(i, root);
        return root;
    }

    public void union(int i, int j){
        int rooti = find(i);
        int rootj = find(j);
        if (rooti == rootj) {
            return;
        }
        int numberOfElementInSeti = parent.get(rooti);
        int numberOfElementInSetj = parent.get(rootj);
        if (numberOfElementInSeti <= numberOfElementInSetj){ // more negative has more elements.
            parent.put(rooti, numberOfElementInSeti + numberOfElementInSetj);
            parent.put(rootj, rooti);
        }else {
            parent.put(rootj, numberOfElementInSeti + numberOfElementInSetj);
            parent.put(rooti, rootj);
        }
    }

    public boolean connected(int i, int j){
        return find(i) == find(j);
    }

}
