package graphpathfinding;

import priorityqueues.DoubleMapMinPQ;
import timing.Timer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * @see ShortestPathFinder for more method documentation
 */
public class AStarPathFinder<VERTEX> extends ShortestPathFinder<VERTEX> {
    private final AStarGraph<VERTEX> graph;
    private DoubleMapMinPQ<VERTEX> fringe;
    private Map<VERTEX, Double> distances;
    private Map<VERTEX, VERTEX> previousNode;
    private List<VERTEX> solution;
    private Set<VERTEX> visited;
    private double solutionWeight;
    private boolean close;
    private VERTEX deuce;
    private int numStatesExplored;

    /**
     * Creates a new AStarPathFinder that works on the provided graph.
     */
    public AStarPathFinder(AStarGraph<VERTEX> graph) {
        // replace this with your code
        this.graph = graph;
        this.fringe = new DoubleMapMinPQ<>();
        this.distances = new HashMap<>();
        this.previousNode = new HashMap<>();
        this.solution = new ArrayList<>();
        this.visited = new HashSet<>();
        numStatesExplored = 0;
        this.close = false;
        this.deuce = null;
    }

    @Override
    public ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end,
                                                       Duration timeout) {
        Timer timer = new Timer(timeout);
        distances.put(start, 0.0);
        previousNode.put(start, null);
        fringe.add(start, distances.get(start) + graph.estimatedDistanceToGoal(start, end));

        while (!fringe.isEmpty()) {
            VERTEX n = fringe.removeMin();
            visited.add(n);
            numStatesExplored++;
            // time up case
            // if (timer.isTimeUp()) {
            //     return new ShortestPathResult.Timeout<>(numStatesExplored, timer.elapsedDuration());
            // }
            // end case
            if (n.equals(end)) {
                getSolution(start, end);
                solutionWeight = distances.get(end);
                return new ShortestPathResult.Solved<>(solution, solutionWeight, numStatesExplored,
                    timer.elapsedDuration());
            }
            // normal case
            for (WeightedEdge<VERTEX> e : graph.neighbors(n)) {
                VERTEX nb;
                nb = e.to();
                relax(nb, distances.get(n) + e.weight(), graph.estimatedDistanceToGoal(nb, end), end, n);
            }
        }

        return new ShortestPathResult.Unsolvable<>(numStatesExplored, timer.elapsedDuration());
    }

    private void getSolution(VERTEX start, VERTEX end) {
        solution.add(end);
        if (!end.equals(start)) {
            VERTEX currNode = previousNode.get(end);
            while (!currNode.equals(start)) {
                solution.add(0, currNode);
                currNode = previousNode.get(currNode);
            }
            solution.add(0, start);
        }
    }

    private void relax(VERTEX nb, double nbDistance, double heuristic, VERTEX end, VERTEX n) {
        if (!fringe.contains(nb) && !visited.contains(nb)) {
            fringe.add(nb, nbDistance + graph.estimatedDistanceToGoal(nb, end));
        }
        if (!distances.containsKey(nb)) {
            distances.put(nb, nbDistance);
            previousNode.put(nb, n);
        } else if (nbDistance < distances.get(nb)) {
            distances.put(nb, nbDistance);
            previousNode.put(nb, n);
            fringe.changePriority(nb, nbDistance + graph.estimatedDistanceToGoal(nb, end));
        }
    }

    @Override
    protected AStarGraph<VERTEX> graph() {
        return this.graph;
    }
}



