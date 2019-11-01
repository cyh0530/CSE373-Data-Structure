package kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Naive nearest neighbor implementation using a linear scan.
 */
public class NaivePointSet implements PointSet {
    List<Point> pointSet;

    /**
     * Instantiates a new NaivePointSet with the given points.
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public NaivePointSet(List<Point> points) {
        pointSet = new ArrayList<>();
        for (Point p: points) {
            Point newPoint = new Point(p.x(), p.y());
            pointSet.add(newPoint);
        }
    }

    /**
     * Returns the point in this set closest to (x, y) in O(N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        if (pointSet.size() > 0) {
            Point closest = pointSet.get(0);
            double min = closest.distanceSquaredTo(x, y);
            for (int i = 1; i < pointSet.size(); i++) {
                Point current = pointSet.get(i);
                double distant = current.distanceSquaredTo(x, y);
                if (distant < min) {
                    min = distant;
                    closest = current;
                }
            }
            return closest;
        }

        return null;

    }
}
