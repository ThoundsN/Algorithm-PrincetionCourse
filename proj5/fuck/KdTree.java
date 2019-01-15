
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    
    private Node root;
    private RectHV unit;
    private int cnt;
    private double min;
    private Point2D nearest;
    
    private static class Node {
        private Point2D p;
        private boolean odd;
        private Node left, right;
        private RectHV rect;
        
        Node(Point2D p, boolean odd, RectHV rect) {
            this.p = p;
            this.odd = odd;
            this.rect = rect;
        }
        
        public int compareTo(Point2D that) {
            if (odd) {
                if (this.p.x() < that.x()) return -1;
                if (this.p.x() > that.x()) return +1;
            } else {
                if (this.p.y() < that.y()) return -1;
                if (this.p.y() > that.y()) return +1;
            }
            return 0;
        }
    }
    
    // construct an empty set of points 
    public KdTree() {
        root = null;
        cnt = 0;
        unit = new RectHV(0, 0, 1, 1);
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return root == null;
    }
    
    // number of points in the set 
    public int size() {
        return cnt;
    }
    
    private RectHV childRect(Node n, boolean left) {
        RectHV rect, pr = n.rect;
        if (left) {
            if (n.left != null) return n.left.rect;
            if (n.odd) rect = new RectHV(pr.xmin(), pr.ymin(), n.p.x(), pr.ymax());
            else rect = new RectHV(pr.xmin(), pr.ymin(), pr.xmax(), n.p.y());
        } else {
            if (n.right != null) return n.right.rect;
            if (n.odd) rect = new RectHV(n.p.x(), pr.ymin(), pr.xmax(), pr.ymax());
            else rect = new RectHV(pr.xmin(), n.p.y(), pr.xmax(), pr.ymax());
        }
        return rect;
    }
    
    private Node put(Point2D p, Node n, boolean odd, RectHV rect) {
        if (n == null) {
            cnt++;
            return new Node(p, odd, rect);
        }
        if (n.p.equals(p)) return n;
        int cmp = n.compareTo(p);
        if (cmp > 0) n.left = put(p, n.left, !odd, childRect(n, true));
        else n.right = put(p, n.right, !odd, childRect(n, false));
        return n;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("point can't be null");
        root = put(p, root, true, unit);
    }
    
    private Node get(Point2D p, Node n) {
        if (n == null) return null;
        if (n.p.equals(p)) return n;
        int cmp = n.compareTo(p);
        if (cmp > 0) return get(p, n.left);
        else return get(p, n.right);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("point can't be null");
        return get(p, root) != null;
    }
    
    private void draw(Node n) {
        if (n == null) return;
        draw(n.left);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        n.p.draw();
        StdDraw.setPenRadius();
        if (n.odd) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.right);
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root);
    }
    
    private void range(SET<Point2D> in, RectHV rect, Node n) {
        if (n == null || !n.rect.intersects(rect)) return;
        boolean left = n.odd && rect.xmin() < n.p.x() 
            || !n.odd && rect.ymin() < n.p.y();
        boolean right = n.odd && rect.xmax() >= n.p.x() 
            || !n.odd && rect.ymax() >= n.p.y();
        if (left) range(in, rect, n.left);
        if (rect.contains(n.p)) in.add(n.p);
        if (right) range(in, rect, n.right);
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("rect can't be null");
        SET<Point2D> in = new SET<Point2D>();
        range(in, rect, root);
        return in;
    }
    
    private void nearest(Point2D p, Node n) {
        if (n == null || n.rect.distanceSquaredTo(p) >= min) return;
        double disSq = n.p.distanceSquaredTo(p);
        if (disSq < min) {
            nearest = n.p;
            min = disSq;
        }
        int cmp = n.compareTo(p);
        if (cmp > 0) {
            nearest(p, n.left);
            nearest(p, n.right);
        } else {
            nearest(p, n.right);
            nearest(p, n.left);
        }
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("point can't be null");
        min = Double.POSITIVE_INFINITY;
        nearest = null;
        nearest(p, root);
        return nearest;
    }
    
    // unit testing of the methods
    public static void main(String[] args) {
//        KdTree kt = new KdTree();
//        for (double i = 0; i < 10; ++i)
//            kt.insert(new Point2D(i/10, i*i/100));
//        for (Point2D p : kt.range(new RectHV(0, 0, 1, 1)))
//            StdOut.print(p + " ");
//        StdOut.println("\n" + kt.nearest(new Point2D(.5, .5)));
//        StdOut.println(kt.contains(new Point2D(.5, .25)));
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        StdDraw.setPenRadius(.1);
        kdtree.nearest(new Point2D(.81, .30)).draw();
        StdOut.println(kdtree.size());
    }
}