package astar;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @see ShortestPathsSolver for more method documentation
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private TreeMapMinPQ<Vertex> pq;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private double time;
    private int explored;

    /**
     * Immediately solves and stores the result of running memory optimized A*
     * search, computing everything necessary for all other methods to return
     * their results in constant time. The timeout is given in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        pq = new TreeMapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        solution = new LinkedList<>();
        outcome = SolverOutcome.UNSOLVABLE;

        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        edgeTo.put(start, start);

        Stopwatch sw = new Stopwatch();

        while (!pq.isEmpty()) {
            Vertex curr = pq.removeSmallest();
            if (curr.equals(end)) {
                outcome = SolverOutcome.SOLVED;
                Vertex prev = curr;
                do {
                    solution.add(0, prev);
                    prev = edgeTo.get(prev);
                } while (!prev.equals(start));

                solutionWeight = distTo.get(end);

                break;
            }
            explored++;

            List<WeightedEdge<Vertex>> neighbors = input.neighbors(curr);
            for (WeightedEdge<Vertex> e: neighbors) {
                Vertex next = e.to();
                if (!distTo.containsKey(next)) {
                    distTo.put(next, Double.POSITIVE_INFINITY);
                }
                if (distTo.get(next) > distTo.get(curr) + e.weight()) {
                    distTo.put(next, distTo.get(curr) + e.weight());
                    edgeTo.put(next, curr);
                    if (!pq.contains(next)) {
                        pq.add(next, Double.POSITIVE_INFINITY);
                    }
                    pq.changePriority(next, distTo.get(next) + input.estimatedDistanceToGoal(next, end));
                }
            }
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                break;
            }
        }
        time = sw.elapsedTime();
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    /** The total number of priority queue removeSmallest operations. */
    @Override
    public int numStatesExplored() {
        return explored;
    }

    @Override
    public double explorationTime() {
        return time;
    }
}
