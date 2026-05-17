package data.intensive.apps.benchmark;

import data.intensive.apps.ds.Graph;
import data.intensive.apps.inputDistribution.DataGeneration;

import java.util.Random;

public class BenchmarkRunner {
    private static final int WARMUP_RUNS = 3;
    private static final int MEASURE_RUNS = 5;
    private static final int TOTAL_RUNS = WARMUP_RUNS + MEASURE_RUNS;

    public static void main(String[] args){
        System.out.println("---Starting Benchmarking!---");
        System.out.println("\nGenerating graphs...");

        Graph sparseGraph = DataGeneration.sparseGraph();
        Graph denseGraph = DataGeneration.denseGraph();
        Graph completeGraph = DataGeneration.completedGraph();
        Graph directedAcyclicGraph = DataGeneration.directedAcyclicGraph();
        System.out.println("---Running: MST");
        runMST(sparseGraph, denseGraph, completeGraph);

        System.out.println("---Running: SSSP");
        runSSSP(sparseGraph, denseGraph, completeGraph, directedAcyclicGraph);
        // don't put try catch because this will the benchmark loop would instantly finish that iteration and long[] will hold 0ms which wrong answer.

    }
    public static void runMST(Graph sparseGraph, Graph denseGraph, Graph completedGraph){
        long[] primsSparsetime = new long[MEASURE_RUNS];
        long[] primsDenseTime = new long[MEASURE_RUNS];
        long[] primsCompletedTime = new long[MEASURE_RUNS];
        long[] kruskalSparseTime = new long[MEASURE_RUNS];
        long[] kruskalDenseTime = new long[MEASURE_RUNS];
        long[] kruskalCompletedTime = new long[MEASURE_RUNS];

        for (int i = 0; i < TOTAL_RUNS; i++) {
            System.gc(); // to start the benchmark at fresh memory
            if (i < WARMUP_RUNS){
                System.out.println("Warmup" + (i + 1) + "...");
            }
            long primsSparseTimeSt = System.nanoTime();
            sparseGraph.primMST();
            long primsSparseTimeE = System.nanoTime();

            long kruskalSparseTimeSt = System.nanoTime();
            sparseGraph.kruskalMST();
            long kruskalSparseTimeE = System.nanoTime();

            long primsDenseTimeSt = System.nanoTime();
            denseGraph.primMST();
            long primsDenseTimeE = System.nanoTime();

            long kruskalDenseTimeSt = System.nanoTime();
            denseGraph.kruskalMST();
            long kruskalDenseTimeE = System.nanoTime();

            long primsCompletedTimeSt = System.nanoTime();
            completedGraph.primMST();
            long primsCompletedtimeE = System.nanoTime();

            long kruskalCompletedTimeSt = System.nanoTime();
            completedGraph.kruskalMST();
            long kruskalCompletedTimeE = System.nanoTime();

            if (i >= WARMUP_RUNS){
                int idx = i - WARMUP_RUNS;
                primsSparsetime[idx] = primsSparseTimeE - primsSparseTimeSt;
                kruskalSparseTime[idx] = kruskalSparseTimeE - kruskalSparseTimeSt;
                primsDenseTime[idx] = primsDenseTimeE - primsDenseTimeSt;
                kruskalDenseTime[idx] = kruskalDenseTimeE - kruskalDenseTimeSt;
                primsCompletedTime[idx] = primsCompletedtimeE - primsCompletedTimeSt;
                kruskalCompletedTime[idx] = kruskalCompletedTimeE - kruskalCompletedTimeSt;
            }


        }
        System.out.println("MST Results: ");
        BenchmarkMetrics primsSparseMetrics = new BenchmarkMetrics(primsSparsetime);
        BenchmarkMetrics primsDenseMetrics = new BenchmarkMetrics(primsDenseTime);
        BenchmarkMetrics primsCompletedMetrics = new BenchmarkMetrics(primsCompletedTime);
        BenchmarkMetrics kruskalSparseMetrics = new BenchmarkMetrics(kruskalSparseTime);
        BenchmarkMetrics kruskalDenseMetrics = new BenchmarkMetrics(kruskalDenseTime);
        BenchmarkMetrics kruskalCompletedMetrics = new BenchmarkMetrics(kruskalCompletedTime);

        primsSparseMetrics.printReport("Prims", "Sparse");
        kruskalSparseMetrics.printReport("Kruskal", "Sparse");
        primsDenseMetrics.printReport("Prims", "Dense");
        kruskalDenseMetrics.printReport("Kruskal", "Dense");
        primsCompletedMetrics.printReport("Prims", "Completed");
        kruskalCompletedMetrics.printReport("Kruskal", "Completed");
    }

    public static void runSSSP(Graph sparseGraph, Graph denseGraph, Graph completedGraph, Graph directedAcyclicGraph){
        long[] dijkstraSparseTime = new long[MEASURE_RUNS];
        long[] dijkstraDenseTime = new long[MEASURE_RUNS];
        long[] dijkstraCompletedTime = new long[MEASURE_RUNS];
        long[] dijkstraDAGTime = new long[MEASURE_RUNS];
        long[] DAGsTime = new long[MEASURE_RUNS];
        Random random = new Random(42);

        int source  = random.nextInt(5000);
        for (int i = 0; i < TOTAL_RUNS; i++) {
            if (i < WARMUP_RUNS){
                System.out.println("Warmup" + (i + 1) + "...");
            }
            long dijkstraSparseTimeSt = System.nanoTime();
            sparseGraph.dijkstra(source);
            long dijkstraSparseTimeE = System.nanoTime();

            long dijkstraDenseTimeSt = System.nanoTime();
            denseGraph.dijkstra(source);
            long dijkstraDenseTimeE = System.nanoTime();

            long dijkstraCompletedTimeSt = System.nanoTime();
            completedGraph.dijkstra(source);
            long dijkstraCompletedTimeE = System.nanoTime();

            long dijkstraDAGTimeSt = System.nanoTime();
            directedAcyclicGraph.dijkstra(source);
            long dijkstraDAGTimeE = System.nanoTime();

            long DAGsTimeSt = System.nanoTime();
            directedAcyclicGraph.dagShortestPath(source);
            long DAGsTimeE = System.nanoTime();

            if (i >= WARMUP_RUNS){
                int idx = i - WARMUP_RUNS;
                dijkstraSparseTime[idx] = dijkstraSparseTimeE - dijkstraSparseTimeSt;
                dijkstraDenseTime[idx] = dijkstraDenseTimeE - dijkstraDenseTimeSt;
                dijkstraCompletedTime[idx] = dijkstraCompletedTimeE - dijkstraCompletedTimeSt;
                dijkstraDAGTime[idx] = dijkstraDAGTimeE - dijkstraDAGTimeSt;
                DAGsTime[idx] = DAGsTimeE - DAGsTimeSt;
            }
        }
        System.out.println("SSSP Results: ");
        BenchmarkMetrics dijkstraSparseMetrics = new BenchmarkMetrics(dijkstraSparseTime);
        BenchmarkMetrics dijkstraDenseMetrics = new BenchmarkMetrics(dijkstraDenseTime);
        BenchmarkMetrics dijkstraCompletedMetrics = new BenchmarkMetrics(dijkstraCompletedTime);
        BenchmarkMetrics dijkstraDAGMetrics = new BenchmarkMetrics(dijkstraDAGTime);
        BenchmarkMetrics DAGsMetrics = new BenchmarkMetrics(DAGsTime);

        dijkstraSparseMetrics.printReport("Dijkstra", "Sparse");
        dijkstraDenseMetrics.printReport("Dijkstra", "Dense");
        dijkstraCompletedMetrics.printReport("Dijkstra", "Completed");
        dijkstraDAGMetrics.printReport("Dijkstra", "DAG");
        DAGsMetrics.printReport("DAG topology", "DAG");
        double dijkstraVsDAG = dijkstraDAGMetrics.getMean() / DAGsMetrics.getMean();
        System.out.printf("DAG Topology speedup over Dijkstra: %.2fx\n", dijkstraVsDAG);

    }
}
