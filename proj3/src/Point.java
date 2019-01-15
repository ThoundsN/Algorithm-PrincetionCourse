import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point


    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }                         // constructs the point (x, y)



    public Comparator<Point> slopeOrder(){
        return new SlopeOrder();
    }

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            double s1 = slopeTo(p1);
            double s2 = slopeTo(p2);

            if (s1 < s2) return -1;
            if (s1 > s2) return 1;
            return 0;
        }
    }
    public   void draw(){
        StdDraw.point(x, y);
    }                               // draws this point
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point that){
        if (this.y < that.y){
            return -1;
        }
        if (this.y == that.y){
            if (this.x < that.x) return -1;
            if (this.x == that.x) return 0;
        }
        return 1;

    }     // compare two points by y-coordinates, breaking ties by x-coordinates
    public double slopeTo(Point that) {
        if (that.x == this.x) {
            if (that.y == this.y) return Double.NEGATIVE_INFINITY;
            else return Double.POSITIVE_INFINITY;
        }
        if (that.y == this.y) return 0;
        return ((double) that.y - this.y) / (that.x - this.x);
    }
    // the slope between this point and that point


    }              // compare two points by slopes they make with this point
