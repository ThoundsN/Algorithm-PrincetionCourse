import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int size;
    private LineSegment[] ls;

    public FastCollinearPoints(Point[] points){
        if (points == null) throw new java.lang.IllegalArgumentException();
        Point[] copy = points.clone();
        int n = copy.length;
        Arrays.sort(copy);
        ArrayList<LineSegment> al = new ArrayList<>();

        for (int i = 0; i < copy.length; i++ ){
            Point[] p = copy.clone();
            Arrays.sort(p, p[i].slopeOrder());

            int j = 1;
            while (j < n -2 ){
                int k = j;
                double s1 = p[0].slopeTo(p[k++]);
                double s2 = p[0].slopeTo(p[k++]);
                if (s1 != s2){
                    j++;
                    continue;
                }
                double s;
                do {
                    if (k == n) {
                        k++;
                        break;
                    }
                    s = p[0].slopeTo(p[k++]);
                }while (s == s1);
                if (k - j < 4){
                    j++;
                    continue;
                }
                int len = k-j;
                Point[] line = new Point[len];
                line[0] = p[0];
                for (int a =1; a < line.length; a ++){
                    line[a] = p[j+a-1];
                }
                Arrays.sort(line);
                if (p[0] == line[0]){
                    al.add(new LineSegment(line[0], line[len-1]));
                }
                j = --k;
            }
        }
        size = al.size();
        ls = new LineSegment[size];
        for (int i = 0; i < size; i++){
            ls[i] = al.get(i);
        }
    }     // finds all line segments containing 4 or more points
    public           int numberOfSegments()    {
        return size;
    }    // the number of line segments


    public LineSegment[] segments()    {
        return ls.clone();
    }            // the line segments


    public static void main(String[] args) {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}