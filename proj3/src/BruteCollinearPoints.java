import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private int size;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points){
        if (points == null) throw  new java.lang.IllegalArgumentException();
        double s1;
        double s2;
        double s3;
        Point[] copy = points.clone();

        Arrays.sort(copy);
        ArrayList<LineSegment> al = new ArrayList<>();
     for (int i = 0; i< copy.length; i++){
         for (int j = i+ 1; j < copy.length; j++){
             for (int k = j + 1; k < copy.length; k++ ){
                 for (int f = k +1; f < copy.length; f++){
                     Point[] temp = new Point[4];
                     s1 = copy[i].slopeTo(copy[j]);
                     s2 = copy[i].slopeTo(copy[k]);
                     if (s1 != s2 ) continue;
                     s3 = copy[i].slopeTo(copy[f]);
                     if(s1 == s3){
                         temp[0] = copy[i];
                         temp[1] = copy[j];
                         temp[2] = copy[k];
                         temp[3] = copy[f];
                         Arrays.sort(temp);
                         LineSegment ls = new LineSegment(temp[0], temp[3]);
                         al.add(ls);
                     }
                 }
             }
         }
     }
        size = al.size();
        segments = new LineSegment[size];
        for (int i = 0 ; i<size; i ++){
            segments[i] = al.get(i);
        }

    }    // finds all line segments containing 4 points
    public           int numberOfSegments(){return size;}        // the number of line segments
    public LineSegment[] segments(){return segments;}                // the line segments

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
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}