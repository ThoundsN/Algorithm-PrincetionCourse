import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class WordNet {
    private Set<String> nouns;
    private ArrayList<LinkedList<String>> synset;
    private HashMap<String,LinkedList<Integer>> ids;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        readsynsets(synsets);
        builddigraph(hypernyms);
    }

    private void readsynsets(String synsets){
        if (synsets == null) throw new IllegalArgumentException();
        synset = new ArrayList<LinkedList<String>>();
        ids = new HashMap<>();
        In in = new In(synsets);
        while (in.hasNextLine()){
            StringTokenizer st = new StringTokenizer(in.readLine(), ",");
            int id = Integer.parseInt(st.nextToken());
            StringTokenizer st2 = new StringTokenizer(st.nextToken());
            LinkedList<String> set = new LinkedList<>();
            while (st2.hasMoreTokens()){
                String noun = st2.nextToken();
                set.add(noun);
                if (ids.get(noun) == null){
                    ids.put(noun,new LinkedList<>());
                }
                ids.get(noun).add(id);
            }
            synset.add(set);
        }
        nouns = ids.keySet();
    }

    private void builddigraph(String hypernyms){
        if (hypernyms == null) throw new IllegalArgumentException();
        Digraph graph = new Digraph(synset.size());
        In in = new In(hypernyms);
        while (in.hasNextLine()){
            StringTokenizer st = new StringTokenizer(in.readLine(), ",");
            int id = Integer.parseInt(st.nextToken());
            while (st.hasMoreTokens()){
                int hyper = Integer.parseInt(st.nextToken());
                graph.addEdge(id,hyper);
            }
        }

        int cnt = 0;
        for (int i =0 ; i< graph.V(); i++){
            if (!graph.adj(i).iterator().hasNext()) cnt++;
        }
        if (cnt != 1){
            throw new IllegalArgumentException("Not a rooted graph");
        }
        sap = new SAP(graph);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if (word == null )  throw new IllegalArgumentException();
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)

    private LinkedList<Integer> getID(String word) {

        return ids.get(word);
    }


    public int distance(String nounA, String nounB){
        if (nounA == null || nounB == null){
            throw new IllegalArgumentException();
        }
        LinkedList<Integer> a = getID(nounA);
        LinkedList<Integer> b = getID(nounB);
        if ( a == null || b == null){
            throw new IllegalArgumentException("Noun not found");
        }
        if (a.size()==1 && b.size()==1){
            return sap.length(a.get(0),b.get(0));

        }
        return sap.length(a,b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (nounA == null || nounB == null){
            throw new IllegalArgumentException();
        }
        LinkedList<Integer> a = getID(nounA);
        LinkedList<Integer> b = getID(nounB);
        if ( a == null || b == null){
            throw new IllegalArgumentException("Noun not found");
        }
        int ancestor;
        if (a.size()==1 && b.size()==1){
             ancestor = sap.ancestor(a.get(0),b.get(0));
        }
        else {
             ancestor = sap.ancestor(a, b);
        }
        StringBuilder sb = new StringBuilder();
        for (String s : synset.get(ancestor)){
            sb.append(s+" ");
        }
        return sb.substring(0,sb.length()-1);
    }

    // do unit testing of this class
//    public static void main(String[] args)
}