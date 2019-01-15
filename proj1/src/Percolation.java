
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid ;
    private int size;
    private WeightedQuickUnionUF uf;
    private  int top;
    private int bottom;

    public Percolation(int n){
        if (n <= 0){
            throw new IllegalArgumentException();
        }
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n*n+2);
        size = n;
        top = size * size;
        bottom = size * size + 1;
        for (int i =0; i< n; i++){
            for (int j = 0 ;j<n; j++){
                grid[i][j] = false;
            }
        }
    }                // create n-by-n grid, with all sites blocked
    public    void open(int row, int col){
        if (row > size || col > size ||row < 1|| col < 1 ){
            throw new IllegalArgumentException();
        }
        if(grid[row -1 ][col -1 ]){
            return;
        }
        grid[row-1][col-1] = true;
        if (row == 1){
            uf.union(top, findufindex(row, col));
        }
        if (row == size){
            uf.union(bottom, findufindex(row,col));
        }
        if (row +1 <= size && isOpen(row +1, col )){
            uf.union(findufindex(row,col), findufindex(row +1 ,col));
        }
        if ( col + 1 <= size && isOpen(row , col +1)){
            uf.union(findufindex(row,col), findufindex(row  ,col+1));
        }
        if ( row -1 >= 1 && isOpen(row - 1, col )){
            uf.union(findufindex(row,col), findufindex(row - 1 ,col));
        }
        if (col -1 >= 1 && isOpen(row , col -1)){
            uf.union(findufindex(row,col), findufindex(row  ,col-1));
        }

    }   // open site (row, col) if it is not open already
    public boolean isOpen(int row, int col){
        if (row > size || col > size||row <1 || col < 1){
            throw new IllegalArgumentException();
        }
        return  grid[row-1][col-1];
    } // is site (row, col) open?
    public boolean isFull(int row, int col){
        if (row > size || col > size||row <1 || col < 1){
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            return false;
        }
        if (row == 1) {  // if it is open, it is full. for the first row.
            return true;
        }
        int index = size * (row - 1) + (col - 1);
        return uf.connected(top, index);
    }  // is site (row, col) full?
    public     int numberOfOpenSites() {
        int n = 0 ;
        for (int i =0; i< grid.length; i++){
            for (int j = 0 ;j<grid[0].length; j++){
                if (grid[i][j]){
                    n++;
            }
        }
    }
    return n;
}      // number of open sites
    public boolean percolates(){
        if(size ==1 ){
            return grid[0][0];
        }
        return uf.connected(top,bottom);

    }              // does the system percolate?
    private int findufindex(int row, int col){
        int index = (row -1 )  * size + col - 1 ;
        return index;
    }
 // test client (optional)
}
