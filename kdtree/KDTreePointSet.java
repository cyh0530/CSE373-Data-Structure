package kdtree;

import java.util.List;

public class KDTreePointSet implements PointSet {
    public KDTree tree = new KDTree();

    /**
     * Instantiates a new KDTree with the given points.
     * @param points a non-null, non-empty list of points to include
     *               (makes a defensive copy of points, so changes to the list
     *               after construction don't affect the point set)
     */
    public KDTreePointSet(List<Point> points) {
        for (Point p: points) {
            tree.add(p);
        }
    }

    /**
     * Returns the point in this set closest to (x, y) in (usually) O(log N) time,
     * where N is the number of points in this set.
     */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        return nearest(target, tree.root, tree.root.point, 0, 0);
    }

    private Point nearest(Point target, KDTree.KDTreeNode node, Point best, int level, int correctDirection) {

        if (node == null) {
            return best;
        }
        if (node.point.distanceSquaredTo(target) < best.distanceSquaredTo(target)) {
            best = node.point;
        }

        if (correctDirection > -3) {
            best = nearest(target, node.left, best, level + 1,
                    calculateDirection(target, node, node.left, correctDirection));
            best = nearest(target, node.right, best, level + 1,
                    calculateDirection(target, node, node.right, correctDirection));

        }



        return best;
    }

    private int calculateDirection(Point target, KDTree.KDTreeNode current,
                                   KDTree.KDTreeNode next, int correctDirection) {
        if (next != null) {
            // compare x
            if ((current.point.x()-target.x()) * (current.point.x() - next.point.x()) > 0) {
                correctDirection++;
            } else {
                correctDirection--;
            }

            // compare y
            if ((current.point.y()-target.y()) * (current.point.y() - next.point.y()) > 0) {
                correctDirection++;
            } else {
                correctDirection--;
            }
        }
        return correctDirection;
    }




    private class KDTree  {

        public KDTreeNode root;

        public KDTree() {
            root = null;
        }

        public void add(Point p) {
            root = this.add(p, root, 0);
        }

        private KDTreeNode add(Point p, KDTreeNode node, int level) {
            if (node == null) {
                return new KDTreeNode(new Point(p.x(), p.y()));
            } else {
                // equal coordinate will categorize to right node, which is top and right
                // first compare x then compare y
                if (level % 2 == 0) {
                    // compare x
                    if (p.x() < node.point.x()) {
                        node.left = add(p, node.left, level+1);
                    } else {
                        node.right = add(p, node.right, level+1);
                    }
                } else {
                    // compare y
                    if (p.y() < node.point.y()) {
                        node.left = add(p, node.left, level+1);
                    } else {
                        node.right = add(p, node.right, level+1);
                    }
                }
                return node;
            }
        }



        private class KDTreeNode {

            public Point point;
            public KDTreeNode left;
            public KDTreeNode right;

            public KDTreeNode(Point p) {
                point = p;
                left = null;
                right = null;
            }
        }
    }
}
