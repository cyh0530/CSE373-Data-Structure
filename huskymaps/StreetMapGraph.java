package huskymaps;

import astar.AStarGraph;
import astar.WeightedEdge;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

import static huskymaps.utils.Spatial.greatCircleDistance;
import static huskymaps.utils.Spatial.projectToX;
import static huskymaps.utils.Spatial.projectToY;

public class StreetMapGraph implements AStarGraph<Long> {
    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Long, Set<WeightedEdge<Long>>> neighbors = new HashMap<>();
    private KDTree tree = new KDTree();
    private Node[] nodeList;
    public StreetMapGraph(String filename) {
        OSMGraphHandler.initializeFromXML(this, filename);
        for (Long id: nodes.keySet()) {
            tree.add(nodes.get(id));
        }
        nodeList = nodeSortedByName();
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lat The target latitude.
     * @param lon The target longitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lat, double lon) {
        return tree.closest(lat, lon).id();
    }


    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of full names of locations matching the <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> result = new LinkedList<>();

        int leftMostIndex = leftMostIndex(prefix, 0, nodeList.length, false);
        int rightMostIndex = rightMostIndex(prefix, 0, nodeList.length, false);

        if (leftMostIndex != -1) {
            for (int i = leftMostIndex; i <= rightMostIndex; i++) {
                result.add(nodeList[i].name());
            }
        }

        return result;
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose name matches the <code>locationName</code>.
     */
    public List<Node> getLocations(String locationName) {
        List<Node> result = new LinkedList<>();

        int leftMostIndex = leftMostIndex(locationName, 0, nodeList.length, true);
        int rightMostIndex = rightMostIndex(locationName, 0, nodeList.length, true);

        if (leftMostIndex != -1) {
            for (int i = leftMostIndex; i <= rightMostIndex; i++) {
                result.add(nodeList[i]);
            }
        }

        return result;

    }

    private int leftMostIndex(String prefix, int left, int right, boolean findExactMatch) {

        while (left <= right) {
            int mid = (left + right) / 2;
            String currentPrefix = nodeList[mid].name().toLowerCase();
            if (currentPrefix.length() > prefix.length()){
                currentPrefix = currentPrefix.substring(0, prefix.length());
            }
            if (isLeftMost(prefix, currentPrefix, mid, findExactMatch)) {
                return mid;
            } else if (currentPrefix.compareTo(prefix.toLowerCase()) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    private boolean isLeftMost(String prefix, String currentPrefix, int mid, boolean findExactMatch) {
        if (mid == 0) {
            return true;
        }
        String previousPrefix = nodeList[mid - 1].name().toLowerCase();
        if (previousPrefix.length() > prefix.length()){
            previousPrefix = previousPrefix.substring(0, prefix.length());
        }
        if (findExactMatch) {
            return prefix.toLowerCase().compareTo(previousPrefix) > 0 && prefix.equalsIgnoreCase(nodeList[mid].name());
        } else {
            return prefix.toLowerCase().compareTo(previousPrefix) > 0
                    && prefix.equalsIgnoreCase(currentPrefix);
        }
    }

    private int rightMostIndex(String prefix, int left, int right, boolean findExactMatch) {

        while (left <= right) {
            int mid = (left + right) / 2;
            String currentPrefix = nodeList[mid].name().toLowerCase();
            if (currentPrefix.length() > prefix.length()){
                currentPrefix = currentPrefix.substring(0, prefix.length());
            }
            if (isRightMost(prefix, currentPrefix, mid, findExactMatch)) {
                return mid;
            } else if (currentPrefix.compareTo(prefix.toLowerCase()) > 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return -1;
    }

    private boolean isRightMost(String prefix, String currentPrefix, int mid, boolean findExactMatch) {
        if (mid == nodeList.length - 1) {
            return true;
        }
        String nextPrefix = nodeList[mid + 1].name().toLowerCase();
        if (nextPrefix.length() > prefix.length()){
            nextPrefix = nextPrefix.substring(0, prefix.length());
        }
        if (findExactMatch) {
            return prefix.toLowerCase().compareTo(nextPrefix) < 0 && prefix.equalsIgnoreCase(nodeList[mid].name());
        } else {
            return prefix.toLowerCase().compareTo(nextPrefix) < 0
                    && prefix.equalsIgnoreCase(currentPrefix);
        }
    }

    private Node[] nodeSortedByName() {

        int haveName = 0;
        for (Long id: nodes.keySet()) {
            if (nodes.get(id).name() != null) {
                haveName++;
            }
        }
        int i = 0;
        Node[] result = new Node[haveName];
        for (Long id: nodes.keySet()) {
            if (nodes.get(id).name() != null) {
                result[i] = nodes.get(id);
                i++;
            }
        }
        mergeSort(result);

        return result;
    }

    private void mergeSort(Node[] list) {
        if (list.length > 1) {

            Node[] left = Arrays.copyOfRange(list, 0, list.length / 2);
            Node[] right = Arrays.copyOfRange(list, list.length / 2, list.length);

            mergeSort(left);
            mergeSort(right);
            merge(list, left, right);
        }
    }

    private void merge(Node[] list, Node[] left, Node[] right) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < list.length; i++) {

            if (i2 >= right.length || (i1 < left.length && left[i1].name().compareTo(right[i2].name()) < 0)) {
                list[i] = left[i1];
                i1++;
            } else {
                list[i] = right[i2];
                i2++;
            }
        }
    }

    /** Returns a list of outgoing edges for V. Assumes V exists in this graph. */
    @Override
    public List<WeightedEdge<Long>> neighbors(Long v) {
        return new ArrayList<>(neighbors.get(v));
    }

    /**
     * Returns the great-circle distance between S and GOAL. Assumes
     * S and GOAL exist in this graph.
     */
    @Override
    public double estimatedDistanceToGoal(Long s, Long goal) {
        Node sNode = nodes.get(s);
        Node goalNode = nodes.get(goal);
        return greatCircleDistance(sNode.lon(), goalNode.lon(), sNode.lat(), goalNode.lat());
    }

    /** Returns a set of my vertices. Altering this set does not alter this graph. */
    public Set<Long> vertices() {
        return new HashSet<>(nodes.keySet());
    }

    /** Adds an edge to this graph if it doesn't already exist, using distance as the weight. */
    public void addWeightedEdge(long from, long to, String name) {
        if (nodes.containsKey(from) && nodes.containsKey(to)) {
            Node fromNode = nodes.get(from);
            Node toNode = nodes.get(to);
            double weight = greatCircleDistance(fromNode.lon(), toNode.lon(), fromNode.lat(), toNode.lat());
            neighbors.get(from).add(new WeightedEdge<>(from, to, weight, name));
        }
    }

    /** Adds an edge to this graph if it doesn't already exist. */
    public void addWeightedEdge(long from, long to, double weight, String name) {
        if (nodes.containsKey(from) && nodes.containsKey(to)) {
            neighbors.get(from).add(new WeightedEdge<>(from, to, weight, name));
        }
    }

    /** Adds an edge to this graph if it doesn't already exist. */
    public void addWeightedEdge(WeightedEdge<Long> edge) {
        if (nodes.containsKey(edge.from()) && nodes.containsKey(edge.to())) {
            neighbors.get(edge.from()).add(edge);
        }
    }

    /** Checks if a vertex has 0 out-degree from graph. */
    private boolean isNavigable(Node node) {
        return !neighbors.get(node.id()).isEmpty();
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    public double lat(long v) {
        if (!nodes.containsKey(v)) {
            return 0.0;
        }
        return nodes.get(v).lat();
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    public double lon(long v) {
        if (!nodes.containsKey(v)) {
            return 0.0;
        }
        return nodes.get(v).lon();
    }

    /** Adds a node to this graph, if it doesn't yet exist. */
    void addNode(Node node) {
        if (!nodes.containsKey(node.id())) {
            nodes.put(node.id(), node);
            neighbors.put(node.id(), new HashSet<>());
        }
    }

    Node getNode(long id) {
        return nodes.get(id);
    }

    Node.Builder nodeBuilder() {
        return new Node.Builder();
    }

    private class KDTree {

        public KDTreeNode root;

        public KDTree() {
            this.root = null;
        }

        public void add(Node n) {
            root = this.add(n, root, true);
        }

        private KDTreeNode add(Node n, KDTreeNode node, boolean evenLevel) {
            if (node == null) {
                return new KDTreeNode(n);
            }

            double compare = comparePoints(n, node.node, evenLevel);
            // equal coordinate will categorize to right node, which is top and right
            // first compare x then compare y
            if (evenLevel) {
                // compare x
                if (compare < 0) {
                    node.left = add(n, node.left, false);
                } else {
                    node.right = add(n, node.right, false);
                }
            } else {
                // compare y
                if (compare < 0) {
                    node.left = add(n, node.left, true);
                } else {
                    node.right = add(n, node.right, true);
                }
            }
            return node;

        }

        public Node closest(double lon, double lat) {
            Node target = new Node(0, lon, lat, "", 0);
            return closest(target, root, root.node, true);
        }


        private Node closest(Node target, KDTree.KDTreeNode node, Node best, boolean evenLevel) {
            if (node == null) {
                return best;
            }

            if (distance(node.node, target) < distance(best, target)
                && neighbors.containsKey(node.node.id()) && neighbors.get(node.node.id()).size() != 0) {
                best = node.node;
            }

            double toSeparation = comparePoints(target, node.node, evenLevel);

            if (toSeparation < 0) {
                best = closest(target, node.left, best, !evenLevel);

                if (toSeparation <= distance(best, target)) {
                    best = closest(target, node.right, best, !evenLevel);
                }
            } else {
                best = closest(target, node.right, best, !evenLevel);

                if (toSeparation <= distance(best, target)) {
                    best = closest(target, node.left, best, !evenLevel);
                }
            }
            return best;
        }

        private double distance(Node node, Node target) {
            double x = projectToX(node.lon(), node.lat());
            double y = projectToY(node.lon(), node.lat());
            double targetX = projectToX(target.lon(), target.lat());
            double targetY = projectToY(target.lon(), target.lat());
            return Math.sqrt((x - targetX) * (x - targetX) + (y - targetY) * (y - targetY));
        }

        private double comparePoints(Node target, Node node, boolean evenLevel) {
            if (evenLevel) {
                return projectToX(target.lon(), target.lat()) - projectToX(node.lon(), node.lat());
            } else {
                return projectToY(target.lon(), target.lat()) - projectToY(node.lon(), node.lat());
            }
        }

        private class KDTreeNode {

            public Node node;
            public KDTreeNode left;
            public KDTreeNode right;

            public KDTreeNode(Node node) {
                this.node = node;
                left = null;
                right = null;
            }
        }
    }
}
