


import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SAP {

    private final int SIZE;
    private int vbegin,wbegin,vend,wend;
    private Bag<Integer>[] adj;
    private ArrayList<Integer> vmarked,wmarked;
    private HashMap<Integer,Integer> vsearch,wsearch;
    private HashMap<Integer,Integer> length,ancestor;




    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if (G == null) throw new IllegalArgumentException();
        SIZE = G.V();
        adj =  (Bag<Integer>[]) new Bag[SIZE];
        vmarked = new ArrayList<>();
        wmarked = new ArrayList<>();
        vsearch = new HashMap<>();
        wsearch = new HashMap<>();
        length = new HashMap<>();
        ancestor = new HashMap<>();
        for (int i = 0; i< SIZE; i++){
            adj[i] = new Bag<Integer>();
            for (int j: G.adj(i)){
                adj[i].add(j);
            }
        }
    }


    private void validate(int v){
        if (v < 0 || v >= SIZE) throw new IllegalArgumentException();
    }


    private void bfs(int pair){
        vbegin = 0;
        wbegin = 0;
        vend = vmarked.size();
        wend = wmarked.size();
        int step = 1;
        length.put(pair,SIZE);
        ancestor.put(pair,SIZE);

        while (vbegin != vend || wbegin != wend){
            if (step >= length.get(pair)) break;
            for (int i = vbegin; i< vend; i++){
                for (int a : adj[vmarked.get(i)]){
                if (vsearch.get(a)!= null) continue;
                if (wsearch.get(a) != null && length.get(pair) > step + wsearch.get(a)){
                    length.put(pair,step + wsearch.get(a));
                    ancestor.put(pair,a);
                }
                vmarked.add(a);
                vsearch.put(a,step);
            }
        }
               vbegin = vend;
               vend = vmarked.size();

            for (int i = wbegin; i< wend; i++){
                for (int a : adj[wmarked.get(i)]){
                    if (wsearch.get(a)!= null) continue;
                    if (vsearch.get(a) != null&& length.get(pair) > step + vsearch.get(a)){
                        length.put(pair,step + vsearch.get(a));
                        ancestor.put(pair,a);
                    }
                    wmarked.add(a);
                    wsearch.put(a,step);
                }
            }
               wbegin = wend;
               wend = wmarked.size();

               step++;
        }
        if (length.get(pair) == SIZE){
            length.put(pair,-1);
            ancestor.put(pair,-1);
        }

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        validate(v);
        validate(w);
        if (v == w) return 0;

        int pair;
        if (v > w) {
            pair = v;
            v = w;
            w = pair;
        }
        pair = (SIZE-1)*v-(v*v+v)/2+w;
        if (length.get(pair) != null) return length.get(pair);
        vmarked.clear();
        wmarked.clear();
        vsearch = new HashMap<>();
        wsearch = new HashMap<>();

        vsearch.put(v,0);
        wsearch.put(w,0);
        vmarked.add(v);
        wmarked.add(w);

        bfs(pair);
        return length.get(pair);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        validate(v);
        validate(w);
        if (v == w) return v;

        int pair;
        if (v > w) {
            pair = v;
            v = w;
            w = pair;
        }
        pair = (SIZE-1)*v-(v*v+v)/2+w;
        if (ancestor.get(pair) != null) return ancestor.get(pair);
        vmarked.clear();
        wmarked.clear();
        vsearch = new HashMap<>();
        wsearch = new HashMap<>();

        vsearch.put(v,0);
        wsearch.put(w,0);
        vmarked.add(v);
        wmarked.add(w);
        bfs(pair);

        return ancestor.get(pair);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null ){
            throw new IllegalArgumentException();
        }

        vmarked.clear();
        wmarked.clear();
        vsearch = new HashMap<>();
        wsearch = new HashMap<>();

//        Iterator<Integer> a = v.iterator();
//        Iterator<Integer> b = w.iterator();
//
//        while (a.hasNext()){
//            if (a.next() == null) throw new IllegalArgumentException();
//            int i = a.next();
//            vmarked.add(i);
//            vsearch.put(i,0);
//        }
//
//        while (b.hasNext()){
//            if (b.next() == null) throw new IllegalArgumentException();
//            int j = b.next();
//            if (vmarked.contains(j)){
//                return 0;
//            }
//            wmarked.add(j);
//            wsearch.put(j,0);
//        }
        for (int i : v){
            validate(i);
            vmarked.add(i);
            vsearch.put(i,0);
        }



        for (int j : w){
            validate(j);
            if (vmarked.contains(j)){
                return 0;
            }
            wmarked.add(j);
            wsearch.put(j,0);
        }

        bfs(0);
        return length.get(0);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null || w == null ){
            throw new IllegalArgumentException();
        }
        vmarked.clear();
        wmarked.clear();
        vsearch = new HashMap<>();
        wsearch = new HashMap<>();

//        Iterator<Integer> a = v.iterator();
//        Iterator<Integer> b = w.iterator();
//
//        while (a.hasNext()){
//            if (a.next() == null) throw new IllegalArgumentException();
//            int i = a.next();
//            vmarked.add(i);
//            vsearch.put(i,0);
//        }
//
//        while (b.hasNext()){
//            if (b.next() == null) throw new IllegalArgumentException();
//            int j = b.next();
//            if (vmarked.contains(j)){
//                return j;
//            }
//            wmarked.add(j);
//            wsearch.put(j,0);
//        }

        for (int i : v){
            validate(i);
            vmarked.add(i);
            vsearch.put(i,0);
        }

        for (int j : w){
            validate(j);
            if (vmarked.contains(j)){
                return j;
            };
            wmarked.add(j);
            wsearch.put(j,0);
        }

        bfs(0);
        return ancestor.get(0);
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
