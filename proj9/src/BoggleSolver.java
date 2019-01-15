import edu.princeton.cs.algs4.Bag;
import java.util.Stack;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;



public class BoggleSolver
{
    private Trie<Integer> dic;
    private char[] board;
    private boolean[] marked;
    private StringBuilder sb;
    private int boardsize, W,H;
    private Stack<String> wordstack;
    private Bag<Integer>[] adj;
    private Stack<String> unmatch;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        dic = new Trie<>();
        for (int i = 0; i< dictionary.length; i++){
            int len = dictionary[i].length(), score;
            if (len < 3) score = 0;
            else if (len < 5) score = 1;
            else if (len < 6) score = 2;
            else if (len < 7) score = 3;
            else if (len < 8) score = 5;
            else score = 11;
            dic.put(dictionary[i],score);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        wordstack = new Stack<>();
        unmatch = new Stack<>();
        H = board.rows();
        W = board.cols();
        this.boardsize=  board.cols()*board.rows();
        this.board = new char[boardsize];
        adj = new Bag[boardsize];


        for (int i = 0; i < H; i++) {
            boolean u = i > 0, d = i < H-1;
            int idx = i*W;
            for (int j = 0; j < W; j++, idx++) {
                boolean l = j > 0, r = j < W-1;
                adj[idx] = new Bag<Integer>();
                this.board[idx] = board.getLetter(i,j);
                if (u && l) adj[idx].add(idx-W-1);
                if (u)      adj[idx].add(idx-W);
                if (u && r) adj[idx].add(idx-W+1);
                if (l)      adj[idx].add(idx-1);
                if (r)      adj[idx].add(idx+1);
                if (d && l) adj[idx].add(idx+W-1);
                if (d)      adj[idx].add(idx+W);
                if (d && r) adj[idx].add(idx+W+1);
            }
        }
        for (int i = 0; i< boardsize; i++){
            marked = new boolean[boardsize];
            sb = new StringBuilder();
            dfs(i);
        }
        return wordstack;
    }


    private void dfs(int i){

        marked[i] = true;
        if (this.board[i] == 'Q'){
            sb.append("QU");
        }
        else  sb.append(this.board[i]);

        if (check(sb.toString())<0){
            sb.deleteCharAt(sb.length()-1);
            marked[i] = false;
            if (sb.length() > 0 && sb.charAt(sb.length()-1) == 'Q')
                sb.deleteCharAt(sb.length()-1);
            return;
        }

        if (sb.length()>2){
            if (dic.contains(sb.toString())&&!wordstack.contains(sb.toString())) wordstack.push(sb.toString());
        }
        for (int w : adj[i]){
            if (!marked[w]){
                dfs(w);
            }
        }
        marked[i] = false;
        sb.deleteCharAt(sb.length()-1);
        if (sb.length() > 0 && sb.charAt(sb.length()-1) == 'Q')
            sb.deleteCharAt(sb.length()-1);
    }

    private int check(String s){
//        System.out.println(s + "  check");
        if (unmatch.contains(s))return -1;
        if (!dic.PrefixQuery(s)) {
            unmatch.push(s);
            return -1;
        }
        return 1;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (dic.get(word) == null) return 0;
        return dic.get(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        Iterable<String> a= solver.getAllValidWords(board);
//        for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
        for (String word : a) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
