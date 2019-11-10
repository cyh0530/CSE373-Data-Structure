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
        return nearest2(target, tree.root, tree.root.point, 0);
    }

    private Point nearest2(Point target, KDTree.KDTreeNode node, Point best,
                            int level) {
        if (node == null) {
            return best;
        }

        if (Math.sqrt(node.point.distanceSquaredTo(target)) < distance(best, target)) {
            best = node.point;
            //System.out.printf("Best point = %s, distance = %.4f\n", best, distance(best, target));
        }

        // compare x
        if (level % 2 == 0) {
            if (target.x() < node.point.x()) {
                //System.out.print("Level " + level + " --1 Going left looking for ");
                best = nearest2(target, node.left, best, level + 1);
                //System.out.println("Level " + level + " --1 best =  " + best);
                if (node.point.x() - target.x() <= distance(best, target)) {
                    //System.out.print("Level " + level + " --2 Going right looking for ");
                    best = nearest2(target, node.right, best, level + 1);
                    //System.out.println("Level " + level + " --2 best =  " + best);
                }
            } else {
                //System.out.print("Level " + level + " --3 Going right looking for ");
                best = nearest2(target, node.right, best, level + 1);
                //System.out.println("Level " + level + " --3 best =  " + best);
                if (target.x() - node.point.x() < distance(best, target)) {
                    //System.out.print("Level " + level + " --4 Going left looking for ");
                    best = nearest2(target, node.left, best, level + 1);
                    //System.out.println("Level " + level + " --4 best =  " + best);
                }
            }
        } else {
            if (target.y() < node.point.y()) {
                //System.out.print("Level " + level + " --5 Going down looking for ");
                best = nearest2(target, node.left, best, level + 1);
                //System.out.println("Level " + level + " --5 best =  " + best);

                if (node.point.y() - target.y() <= distance(best, target)) {
                    //System.out.print("Level " + level + " --6 Going up looking for ");
                    best = nearest2(target, node.right, best, level + 1);
                    //System.out.println("Level " + level + " --6 best =  " + best);

                }
            } else {
                //System.out.print("Level " + level + " --7 Going down looking for ");
                best = nearest2(target, node.right, best, level + 1);
                //System.out.println("Level " + level + " --7 best =  " + best);
                if (target.y() - node.point.y() < distance(best, target)) {
                    //System.out.print("Level " + level + " --8 Going up looking for ");
                    best = nearest2(target, node.left, best, level + 1);
                    //System.out.println("Level " + level + " --8 best =  " + best);
                }
            }
        }

        return best;
    }

    private double distance(Point a, Point b) {
        return Math.sqrt(a.distanceSquaredTo(b));
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
