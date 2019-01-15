
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confidenceLo; // low endpoint of 95% confidence interval
    private double confidenceHi;
    private double[] threshold;
    private Percolation pr;
    private int experiement;


    public PercolationStats(int n, int trials) {
        if ( n <= 0 && trials <= 0 ){
            throw new IllegalArgumentException();
        }

        threshold = new double[trials];
        for (int i = 0 ; i < trials; i++){
            pr = new Percolation(n);
            while (!pr.percolates() ){
                int row = StdRandom.uniform(1, n + 1 );
                int col = StdRandom.uniform(1, n + 1);
                if (pr.isOpen(row,col)){
                    continue;
                }
                pr.open(row, col);
            }
            int opensites = pr.numberOfOpenSites();
            threshold[i] = opensites/(double)(n*n);
        }

        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        confidenceLo = mean - (1.96*stddev) / Math.sqrt(trials);
        confidenceHi = mean + (1.96*stddev) / Math.sqrt(trials);
    }   // perform trials independent experiments on an n-by-n grid
    public double mean(){

        return mean;
    }                          // sample mean of percolation threshold
    public double stddev(){

        return stddev;
    }                        // sample standard deviation of percolation threshold
    public double confidenceLo(){
        return confidenceLo;
    }                  // low  endpoint of 95% confidence interval
    public double confidenceHi(){
        return confidenceHi;

    }                  // high endpoint of 95% confidence interval

    public static void main(String[] args){
        int n = Integer.parseInt( args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n,trials);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);

    }        // test client (described below)
}