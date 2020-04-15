package pointsets;

//import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fast nearest-neighbor implementation using a k-d tree.
 */
public class KDTreePointSet<T extends Point> implements PointSet<T> {
    private Node root;
    private Node best;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;
    private List<T> aP;


    /**
     * Instantiates a new KDTreePointSet with a shuffled version of the given points.
     *
     * Randomizing the point order decreases likeliness of ending up with a spindly tree if the
     * points are sorted somehow.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public static <T extends Point> KDTreePointSet<T> createAfterShuffling(List<T> points) {
        Collections.shuffle(points);
        return new KDTreePointSet<T>(points);
    }

    /**
     * Instantiates a new KDTreePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public KDTreePointSet(List<T> points) {
        // replace this with your code
        aP = points;
        root = new Node(points.get(0), 0, null, null);
        for (int i = 1; i < points.size(); i++) {
            addNode(root, points.get(i));
        }
        // throw new UnsupportedOperationException("Not implemented yet.");
    }

    private void addNode(Node n, Point p) {
        if (n.belowOrLeftOf(p)) {
            if (n.right == null) {
                if (n.orientation == 1) {
                    n.right = new Node(p, 0, null, null);
                } else {
                    n.right = new Node(p, 1, null, null);
                }
            } else {
                addNode(n.right, p);
            }
        } else {
            if (n.left == null) {
                if (n.orientation == 1) {
                    n.left = new Node(p, 0, null, null);
                } else {
                    n.left = new Node(p, 1, null, null);
                }
            } else {
                addNode(n.left, p);
            }
        }
    }

    /**
     * Returns the point in this set closest to the given point in (usually) O(log N) time, where
     * N is the number of points in this set.
     */
    @Override
    public T nearest(Point target) {
        // replace this with your code
        // double minDistance = root.p.distanceSquaredTo(target);
        best = getNearest(root, root, target);
        return (T) best.p;
        // return (T) best;
    }

    // log(n) implementation
    private Node getNearest(Node node, Node bestN, Point target) {
        if (node == null) {
            return bestN;
        }
        double currDistance = node.p.distanceSquaredTo(target);
        double bestDistance = bestN.p.distanceSquaredTo(target);
        if (currDistance < bestDistance) {
            bestN = node;
        }

        if (node.belowOrLeftOf(target)) {
            bestN = getNearest(node.right, bestN, target);
            if (node.orientation == 0) {
                if ((Math.abs(node.p.x() - target.x())) < Math.sqrt(bestDistance)) {
                    bestN = getNearest(node.left, bestN, target);
                }
            } else {
                if ((Math.abs(node.p.y() - target.y())) < Math.sqrt(bestDistance)) {
                    bestN = getNearest(node.left, bestN, target);
                }
            }
        } else {
            bestN = getNearest(node.left, bestN, target);
            if (node.orientation == 0) {
                if ((Math.abs(node.p.x() - target.x())) < Math.sqrt(bestDistance)) {
                    bestN = getNearest(node.right, bestN, target);
                }
            } else {
                if ((Math.abs(node.p.y() - target.y())) < Math.sqrt(bestDistance)) {
                    bestN = getNearest(node.right, bestN, target);
                }
            }
        }
        return bestN;
    }
    // 0(N) implementation
    // private Node getNearest(Node node, Node bestN, Point target) {
    //     if (node == null) {
    //         return bestN;
    //     }
    //
    //     if (node.p.distanceSquaredTo(target) < bestN.p.distanceSquaredTo(target)) {
    //         bestN = node;
    //     }
    //
    //     bestN = getNearest(node.left, bestN, target);
    //     bestN = getNearest(node.right, bestN, target);
    //     return bestN;
    // }

    @Override
    public List<T> allPoints() {
        // replace this with your code
        return aP;

    }

    // Node class
    private class Node {
        private Point p;
        private Node left;
        private Node right;
        private int orientation; // 0 = vertical x, 1 = horizontal y

        Node(Point p, int orientation, Node L, Node R) {
            this.p = new Point(p.x(), p.y());
            this.orientation = orientation;
            this.left = L;
            this.right = R;
        }

        boolean belowOrLeftOf(Point pnt) {
            if (orientation == 0) {
                return (pnt.x() >= p.x());
            } else {
                return (pnt.y() >= p.y());
            }
        }


    }
}
