import edu.princeton.cs.algs4.StdRandom;


import java.util.ArrayList;
import java.util.Arrays;



public class Board {
    private int dimension;
    private int zero;
    private int[] board;
    private int hamming;
    private int manhattan;


    public Board(int[][] blocks){
        dimension = blocks.length;
        board = new int[dimension*dimension+1];
        for (int i= 0; i< dimension; i++){
            for (int j = 0; j< dimension; j++){
             board[i*dimension+j+1] = blocks[i][j];
             if (board[i*dimension+j+1] == 0) zero = i*dimension+j+1;
            }
        }
        hamming = calcHamming(board);
        manhattan = calcManhattan(board);

    }           // construct a board from an n-by-n array of blocks

    private Board(int[] board, int zero, int hamming, int manhattan ){
        dimension = (int)Math.sqrt(board.length);
        this.board = board;
        this.zero = zero;
        this.hamming = hamming;
        this.manhattan = manhattan;
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension(){
        return dimension;
    }                 // board dimension n


    private int calcHamming(int t, int i) {
        if (t == 0) return 0;
        int h = 0;
        if (t != i) h = 1;
        return h;
    }

    private int calcHamming(int[] board) {
        int cnt = 0;
        for (int i = 1; i < board.length; ++i)
            cnt += calcHamming(board[i], i);
        return cnt;
    }
//    private int calhamming(int[] board){
//        int hamming = 0;
//        for(int i =1; i <  board.length; i++){
//            if (board[i] == 0) continue;
//            if (board[i] != i)hamming++;
//        }
//        return hamming;
//    }

    public int hamming(){
        return hamming;
    }                   // number of blocks out of place


    public int manhattan(){
        return manhattan;
    }                 // sum of Manhattan distances between blocks and goal

    private int calcManhattan(int[] board) {
        int sum = 0;
        for (int t = 1; t < board.length; ++t)
            sum += calcManhattan(board[t], t);
        return sum;
    }

    private int calcManhattan(int value, int t) {
        if (value == 0) return 0;
        int i, j, r, c;
        i = (t-1) / dimension;
        j = (t-1) % dimension;
        r = (value-1) / dimension;
        c = (value-1) % dimension;
        return Math.abs(r-i) + Math.abs(c-j);
    }


    public boolean isGoal(){
        return this.hamming == 0;
    }                // is this board the goal board?

    private int[] exch(int i, int j, int[] board){
        int temp = board[i];
        board[i] = board[j];
        board[j] = temp;
        return board;
    }

    public Board twin() {
        int[] newTiles = board.clone();
        for (int i = 1; i < newTiles.length; i++) {
            if ((i -1)% dimension == 0 || newTiles[i] * newTiles[i-1] == 0) continue;
            exch(i, i-1,newTiles);

            int h = hamming, m = manhattan;
            for (int j = i; j > i-2; --j) {
                m += calcManhattan(newTiles[j], j) - calcManhattan(newTiles[j], j);
                h += calcHamming(newTiles[j], j) - calcHamming(newTiles[j], j);
            }
            return new Board(newTiles, zero, h, m);
        }
        return null;
    }
//    public Board twin() {
//        int random1,random2;
//        int[] newboard = this.board.clone();
//        do {
//             random1 = StdRandom.uniform(1,dimension*dimension+1);
//             random2 = StdRandom.uniform(1,dimension*dimension+1);
//        }while (random1==random2||random1 == zero|| random2 ==zero);
//
//        newboard = exch(random1,random2,newboard);
//        int h = calcHamming(newboard);
//        int m = calcManhattan(newboard);
//        Board copy = new Board(newboard,zero,h,m);
//        return copy;
//    }                    // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y){
        if (y ==this) return true;
        if(y ==null) return false;
        if (y.getClass()!=this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.equals(this.board,that.board);
    }        // does this board equal y?

    private Board neighbor(int a)  {
        int[] copy =  exch( a, zero, board.clone());
        int h = hamming, m = manhattan;
        h += calcHamming(copy[zero], zero) - calcHamming(board[a], a);
        m += calcManhattan(copy[zero], zero) - calcManhattan(board[a], a);
        return new Board(copy, a , h, m);
    }

    public Iterable<Board> neighbors(){
        ArrayList<Board> al = new ArrayList<Board>(4);
        if ((zero-1) % dimension != 0) al.add(neighbor(zero-1));
        if ((zero-1)  % dimension != dimension-1) al.add(neighbor(zero +1));
        if ((zero-1)  / dimension != 0) al.add(neighbor(zero - dimension));
        if ((zero-1)  / dimension != dimension-1) al.add(neighbor(zero+ dimension));
        return al;

    }     // all neighboring boards

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", board[i*dimension+j+1]));
            }
            s.append("\n");
        }
        return s.toString();
    }               // string representation of this board (in the output format specified below)

//    public static void main(String[] args) // unit tests (not graded)
}