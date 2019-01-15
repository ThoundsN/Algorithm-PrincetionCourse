import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver {
    
    private MinPQ<Node> pq, qp;
    private int cnt = 0;
    private Comparator<Node> MANHATTAN_PRIORITY = new ManhattanPriority();
    
    private class Node {
        
        private Board board;
        private int moves;
        private Node prev;
        
        Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    }
    
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
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException("Board can't be null");
        pq = new MinPQ<Node>(MANHATTAN_PRIORITY);
        qp = new MinPQ<Node>(MANHATTAN_PRIORITY);
        pq.insert(new Node(initial, cnt, null));
        qp.insert(new Node(initial.twin(), cnt, null));
        Node cur, kur;
        while (!pq.min().board.isGoal() && !qp.min().board.isGoal()) {
            cur = pq.delMin();
            kur = qp.delMin();
            for (Board nb : cur.board.neighbors()) {
                if (cur.prev != null && nb.equals(cur.prev.board)) continue;
                pq.insert(new Node(nb, cur.moves+1, cur));
            }
            for (Board nb : kur.board.neighbors()) {
                if (kur.prev != null && nb.equals(kur.prev.board)) continue;
                qp.insert(new Node(nb, kur.moves+1, kur));
            }
        }
        if (!pq.min().board.isGoal()) cnt = -1;
        else cnt = pq.min().moves;
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return cnt != -1;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return cnt;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> sln = new Stack<Board>();
        Node path = pq.min();
        while (path != null) {
            sln.push(path.board);
            path = path.prev;
        }
        return sln;
    }
    
    // solve a slider puzzle 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Stopwatch sw = new Stopwatch();
        Solver solver = new Solver(initial);
        StdOut.println(sw.elapsedTime());
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
        }
    }
}
