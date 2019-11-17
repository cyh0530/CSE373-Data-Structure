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
        return tree.closest(x, y).point;
    }

    private Point nearest(Point target, KDTree.KDTreeNode node, Point best,
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
            //System.out.printf("Target = " + target);
            //System.out.printf("Current = " + node.point);
            if (target.x() < node.point.x()) {
                //System.out.print("Level " + level + " --1 Going left looking for ");
                best = nearest(target, node.left, best, level + 1);
                //System.out.println("Level " + level + " --1 best =  " + best);
                if (node.point.x() - target.x() < distance(best, target)) {
                    //System.out.print("Level " + level + " --2 Going right looking for ");
                    best = nearest(target, node.right, best, level + 1);
                    //System.out.println("Level " + level + " --2 best =  " + best);
                }
            } else {
                //System.out.print("Level " + level + " --3 Going right looking for ");
                best = nearest(target, node.right, best, level + 1);
                //System.out.println("Level " + level + " --3 best =  " + best);
                if (target.x() - node.point.x() < distance(best, target)) {
                    //System.out.print("Level " + level + " --4 Going left looking for ");
                    best = nearest(target, node.left, best, level + 1);
                    //System.out.println("Level " + level + " --4 best =  " + best);
                }
            }
        } else {
            if (target.y() < node.point.y()) {
                //System.out.print("Level " + level + " --5 Going down looking for ");
                best = nearest(target, node.left, best, level + 1);
                //System.out.println("Level " + level + " --5 best =  " + best);

                if (node.point.y() - target.y() < distance(best, target)) {
                    //System.out.print("Level " + level + " --6 Going up looking for ");
                    best = nearest(target, node.right, best, level + 1);
                    //System.out.println("Level " + level + " --6 best =  " + best);

                }
            } else {
                //System.out.print("Level " + level + " --7 Going down looking for ");
                best = nearest(target, node.right, best, level + 1);
                //System.out.println("Level " + level + " --7 best =  " + best);
                if (target.y() - node.point.y() < distance(best, target)) {
                    //System.out.print("Level " + level + " --8 Going up looking for ");
                    best = nearest(target, node.left, best, level + 1);
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

        public void add(Point p) {
            root = this.add(p, root, true);
        }

        private KDTreeNode add(Point p, KDTreeNode node, boolean evenLevel) {
            if (node == null) {
                return new KDTreeNode(p);
            }

            double compare = comparePoints(p, node, evenLevel);
            // equal coordinate will categorize to right node, which is top and right
            // first compare x then compare y
            if (evenLevel) {
                // compare x
                if (compare < 0) {
                    node.left = add(p, node.left, false);
                } else {
                    node.right = add(p, node.right, false);
                }
            } else {
                // compare y
                if (compare < 0) {
                    node.left = add(p, node.left, true);
                } else {
                    node.right = add(p, node.right, true);
                }
            }
            return node;

        }

        public KDTreeNode closest(double x, double y) {
            Point target = new Point(x, y);
            return closest(target, root, root, true);
        }


        private KDTreeNode closest(Point target, KDTreeNode node, KDTreeNode best, boolean evenLevel) {
            if (node == null) {
                return best;
            }

            if (distance(node, target) < distance(best, target)) {
                best = node;
                //System.out.printf("Best point = %s, distance = %.4f\n", best, distance(best, targetX, targetY));
            }

            double toSeparation = comparePoints(target, node, evenLevel);

            if (toSeparation < 0) {
                best = closest(target, node.left, best, !evenLevel);

                if (toSeparation <= distance(best, target)) {
                    best = closest(target, node.right, best, !evenLevel);
                }
            } else {
                best = closest(target, node.right, best, !evenLevel);

                if (toSeparation < distance(best, target)) {
                    best = closest(target, node.left, best, !evenLevel);
                }
            }

            return best;
        }

        private double distance(KDTreeNode node, Point target) {
            double x = node.point.x();
            double y = node.point.y();
            double targetX = target.x();
            double targetY = target.y();
            return Math.sqrt((x - targetX) * (x - targetX) + (y - targetY) * (y - targetY));
        }

        private double comparePoints(Point target, KDTreeNode node, boolean evenLevel) {
            if (evenLevel) {
                return target.x() - node.point.x();
            } else {
                return target.y() - node.point.y();
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
