import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        for (int i = 0; i< n; i++){
            rq.enqueue(StdIn.readString());
        }
        for (int i = 0; i< n; i++){
            StdOut.println(rq.dequeue());
        }
    }
}
