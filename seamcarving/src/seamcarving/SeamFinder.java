package seamcarving;

import graphpathfinding.AStarGraph;
import graphpathfinding.ShortestPathFinder;

import java.util.List;

public abstract class SeamFinder {
    protected abstract <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> graph);

    /**
     * Calculates and returns a minimum-energy horizontal seam in the current image.
     * The returned array will have the same length as the width of the image.
     * A value of v at index i of the output indicates that pixel (i, v) is in the seam.
     */
    public abstract List<Integer> findHorizontalSeam(double[][] energies);

    /**
     * Calculates and returns a minimum-energy vertical seam in the current image.
     * The returned array will have the same length as the height of the image.
     * A value of v at index i of the output indicates that pixel (v, i) is in the seam.
     */
    public abstract List<Integer> findVerticalSeam(double[][] energies);
}
