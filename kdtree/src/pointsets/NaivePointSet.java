package pointsets;

// import java.nio.file.NotLinkException;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive nearest-neighbor implementation using a linear scan.
 */
public class NaivePointSet<T extends Point> implements PointSet<T> {
    private ArrayList<Point> NPS;

    /**
     * Instantiates a new NaivePointSet with the given points.
     * @param points a non-null, non-empty list of points to include
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public NaivePointSet(List<T> points) {
        // replace this with your code
        NPS = new ArrayList<>();
        NPS.addAll(points);
    }
    /**
     * Returns the point in this set closest to the given point in O(N) time, where N is the number
     * of points in this set.
     */
    @Override
    public T nearest(Point target) {
        // replace this with your code
        double minDistance = NPS.get(0).distanceSquaredTo(target);
        int minIdx = 0;
        int cnt = 0;
        for (Point pt : NPS) {
            double currDistance = pt.distanceSquaredTo(target);
            if (currDistance < minDistance) {
                minDistance = currDistance;
                minIdx = cnt;
            }
            cnt++;
        }
        return (T) NPS.get(minIdx);
    }

    @Override
    public List<T> allPoints() {
        // replace this with your code
        return (List<T>) NPS;
    }
}
