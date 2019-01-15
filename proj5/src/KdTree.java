import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private int size;
    private Node root;
    private RectHV unit;
    private Point2D nearest;
    private double min;


    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean odd;


        Node(Point2D p, RectHV rect, Node lb, Node rt, boolean odd){
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.odd = odd;
        }

        Node(Point2D p,boolean odd,RectHV unit){
            this(p,unit,null,null,odd);
        }


        public int compareTo(Point2D that){
            if (odd){
                if (this.p.x() < that.x()) return -1;
                if (this.p.x() > that.x()) return +1;
            }
            else {
                if (this.p.y() < that.y()) return -1;
                if (this.p.y() > that.y()) return 1;
            }
            return 0;
        }
    }

    // construct an empty set of points
    public         KdTree(){
        this.size = 0;
        this.root = null;
        this.unit = new RectHV(0.0,0.0,1.0,1.0);
    }

    private RectHV childRect(Node n, boolean leftbottom){
        RectHV child,ori = n.rect;
        double xmin,xmax,ymin,ymax;
        if (leftbottom){
            if(n.lb!= null) return n.lb.rect;
            xmin = ori.xmin();
             ymin = ori.ymin();
            if (n.odd){
                 xmax = n.p.x();
                 ymax = ori.ymax();
                child = new RectHV(xmin,ymin,xmax,ymax);
            }
            else {
                 xmax = ori.xmax();
                 ymax = n.p.y();
                child = new RectHV(xmin,ymin,xmax,ymax);
            }
        }
        else {
            if(n.rt!= null) return n.rt.rect;
             xmax = ori.xmax();
             ymax = ori.ymax();
             if (n.odd){
                 xmin = n.p.x();
                 ymin = ori.ymin();
                 child = new RectHV(xmin,ymin,xmax,ymax);
             }
             else {
                 xmin = ori.xmin();
                 ymin = n.p.y();
                 child = new RectHV(xmin,ymin,xmax,ymax);
             }
        }
        return child;
    }

    public           boolean isEmpty() {
        return size == 0;
    }// is the set empty?
    public               int size(){
        return size;
    }                         // number of points in the set


    public              void insert(Point2D p){
        if (p == null) throw new NullPointerException("point can't be null");
        root = insert(root,p,true,unit);
    }              // add the point to the set (if it is not already in the set)


    private Node insert(Node n, Point2D p,boolean odd,RectHV unit) {
        if (n== null) {
            size ++;
           return new Node(p, odd,unit);
        }
        if (n.p.equals(p)) return n;
        int cmp = n.compareTo(p);
        if (cmp <= 0)
            n.rt = insert(n.rt, p, !odd,childRect(n,false));
        else
             n.lb = insert(n.lb, p, !odd,childRect(n,true));
        return n;
    }


    public           boolean contains(Point2D p){
        if (p == null) throw new NullPointerException("point can't be null");
        return get(root,p)!= null;
    }            // does the set contain point p?

    private Node get(Node n, Point2D p){
        if (n == null) return null;
        if (n.p.equals(p)) return n;
        int cmp = n.compareTo(p);
        if (cmp > 0)  return get(n.lb,p);
        else return get(n.rt,p);

    }

    public              void draw(){ draw(root);}                         // draw all points to standard draw

    private void draw(Node n){
        if (n== null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
        if (n.odd){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.p.x(),n.rect.ymin(),n.p.x(),n.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rect.xmin(),n.p.y(),n.rect.xmax(),n.p.y());
        }
        draw(n.lb);
        draw(n.rt);
    }

    public Iterable<Point2D> range(RectHV rect){
        if (rect == null) throw new NullPointerException("rect can't be null");
        SET<Point2D> al = new SET<>();
        range(al,root,rect);
        return al;
    }             // all points that are inside the rectangle (or on the boundary)

    private void range(SET<Point2D> al,  Node n ,RectHV rect){
        if (n == null) return;
        if (n.lb != null &&rect.intersects(n.lb.rect)) range(al,n.lb,rect);
        if (rect.contains(n.p)) al.add(n.p);
        if (n.rt != null &&rect.intersects(n.rt.rect)) range(al,n.rt,rect);
    }


    public           Point2D nearest(Point2D p){
        if (p == null) throw new NullPointerException("point can't be null");
        nearest = null;
        min = Double.POSITIVE_INFINITY;
        nearest(p,root);
        return nearest;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private void nearest(Point2D p, Node n) {
        if (n == null || n.rect.distanceSquaredTo(p) >= min) return;
        double dsq = n.p.distanceSquaredTo(p);
        if (dsq < min) {
            min = dsq;
            nearest = n.p;
        }
        int cmp = n.compareTo(p);
        if (cmp > 0) {
            nearest(p, n.lb);
            nearest(p, n.rt);
        } else {
            nearest(p, n.rt);
            nearest(p, n.lb);
        }
    }
//    public static void main(String[] args)    {
//        KdTree tree = new KdTree();
//        Point2D p1 = new Point2D(1,2);
//        Point2D p2 = new Point2D(1,10);
//        Point2D p3 = new Point2D(3,2);
//
//        Point2D p4 = new Point2D(1,8);
//
//        tree.insert(p1);
//        StdOut.println(tree.contains(p1));
//        tree.insert(p3);
//        StdOut.println(tree.contains(p3));
//
//        tree.insert(p2);
//        tree.insert(p4);
//        StdOut.println(tree.contains(p4));
//    }              // unit testing of the methods (optional)

}
