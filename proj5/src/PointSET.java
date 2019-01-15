import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> tree;

    public         PointSET(){
        tree = new TreeSet<>();
    }                               // construct an empty set of points

    public           boolean isEmpty(){
        return tree.isEmpty();
    }                      // is the set empty?


    public               int size(){
        return tree.size();
    }                         // number of points in the set

    public              void insert(Point2D p){
        if (p == null) throw new java.lang.IllegalArgumentException("input invalid");
        if (contains(p)) return;
        tree.add(p);
    }              // add the point to the set (if it is not already in the set)

    public           boolean contains(Point2D p){
        if (p == null) throw new java.lang.IllegalArgumentException("input invalid");
        return tree.contains(p);
    }            // does the set contain point p?

    public              void draw(){
        for (Point2D p : tree){
            p.draw();
        }
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new java.lang.IllegalArgumentException("input invalid");
        ArrayList<Point2D> al = new ArrayList<>();
        for (Point2D p : tree){
            if (rect.contains(p)) al.add(p);
        }
        return al;
    }             // all points that are inside the rectangle (or on the boundary)
    public           Point2D nearest(Point2D p){
        if (p == null) throw new java.lang.IllegalArgumentException("input invalid");
        if (this.isEmpty()) return null;
        Point2D nearest = tree.first();
        double d = nearest.distanceTo(p);
        for (Point2D q: tree){
            double t =q.distanceTo(p);
           if ( t < d ) {
               d = t;
               nearest = q;
           }
        }
        return nearest;

    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args){

    }                  // unit testing of the methods (optional)
}