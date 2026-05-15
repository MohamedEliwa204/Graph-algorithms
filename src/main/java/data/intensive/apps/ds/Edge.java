package data.intensive.apps.ds;

public class Edge {
    int u, v, weight;


    public Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;

    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getWeight() {
        return weight;
    }
}
