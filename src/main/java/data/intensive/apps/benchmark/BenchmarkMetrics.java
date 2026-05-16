package data.intensive.apps.benchmark;

import java.util.Arrays;

public class BenchmarkMetrics {
    private final double[] timeInMs;
    private final double mean;
    private final double median;
    private final double stdv;

    public BenchmarkMetrics(long[] timeInNs){
        this.timeInMs = new double[timeInNs.length];
        for (int i = 0; i < timeInNs.length; i++) {
            timeInMs[i] = timeInNs[i] / 1000000.0;
        }
        Arrays.sort(timeInMs); // for median calculations
        double sum = 0;
        for (double t: timeInMs){
            sum += t;
        }
        this.mean = sum / timeInMs.length;

        int mid = timeInMs.length / 2;
        if (timeInMs.length % 2 == 0){
            this.median = (timeInMs[mid - 1] + timeInMs[mid]) / 2.0;
        }else {
            this.median = timeInMs[mid];
        }


        double sumVariance = 0;
        for (double t : timeInMs){
            sumVariance += Math.pow(t - this.mean, 2);
        }
        this.stdv = Math.pow(sumVariance / timeInMs.length, 0.5);
    }
    public void printReport(String algorithm, String graphType){
        System.out.printf("%-20s | %-5s | Mean: %6.2f ms | Median: %6.2f ms | SD: %6.2f ms\n", algorithm, graphType, mean, median, stdv);
    }

    public double getMean() {
        return mean;
    }
}
