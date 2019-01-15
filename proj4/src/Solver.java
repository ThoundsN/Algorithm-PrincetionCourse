import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private MinPQ<Node> pq1,pq2;
    private int cnt = 0;
    private Comparator<Node> MANHATTAN_PRIORITY = new ManhattanPriority();

    private class Node{
        private Node prev;
        private Board board;
        private int moves;

         Node(Node prev, Board board, int moves){
            this.prev = prev;
            this.board =board;
            this.moves = moves;
        }
    }

//    private class ManhattonPrority implements Comparator<Node>{
//        public int compare(Node n1, Node n2 ){
//            int p1 = n1.board.manhattan() + n1.moves;
//            int p2 = n2.board.manhattan() + n2.moves;
//            if (p1 > p2 ) return 1;
//            if (p1 == p2){
//                if (n1.board.hamming() > n2.board.hamming()) return 1;
//                if (n1.board.hamming() == n2.board.hamming()) return 0;
//                return -1;
//            }
//            return -1;
//        }
//    }
private class ManhattanPriority implements Comparator<Node> {
    public int compare(Node n1, Node n2) {
        int f1 = n1.board.manhattan() + n1.moves;
        int f2 = n2.board.manhattan() + n2.moves;
        if (f1 == f2) {
            if (n1.board.hamming() == n2.board.hamming()) return 0;
            if (n1.board.hamming() < n2.board.hamming()) return -1;
            return 1;
        }
        if (f1 < f2) return -1;
        return +1;
    }
}

    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Board can't be null");
        pq1 = new MinPQ<Node>(MANHATTAN_PRIORITY);
        pq2 = new MinPQ<Node>(MANHATTAN_PRIORITY);
        pq1.insert(new Node(null, initial, cnt));
        pq2.insert(new Node(null, initial.twin(), cnt));
        Node pq1m, pq2m;
        while (!pq1.min().board.isGoal() && !pq2.min().board.isGoal()){
            pq1m = pq1.delMin();
            pq2m = pq2.delMin();
            for (Board nb : pq1m.board.neighbors()){
                if (pq1m.prev != null && nb.equals(pq1m.prev.board))continue;
                pq1.insert(new Node(pq1m,nb,pq1m.moves+1));
            }
            for (Board nb : pq2m.board.neighbors()){
                if (pq2m.prev != null && nb.equals(pq2m.prev.board))continue;
                pq2.insert(new Node(pq2m,nb,pq2m.moves+1));
            }
        }
        if (!pq1.min().board.isGoal()) cnt = -1;
        else cnt = pq1.min().moves;

    }           // find a solution to the initial board (using the A* algorithm)
    public boolean isSolvable() {
        return cnt != -1;
    }           // is the initial board solvable?
    public int moves(){
        return cnt;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution(){
        if (!isSolvable()) return  null;
        Stack<Board> stack = new Stack<>();
        Node path = pq1.min();
        while (path != null){
            stack.push(path.board);
            path = path.prev;
        }
        return stack;
    }      // sequence of boards in a shortest solution; null if unsolvable
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }// solve a slider puzzle (given below)
}