package seamcarving;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.WeightedEdge;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AStarSeamFinder extends SeamFinder {
    private double[][] energy;
    private static final Duration FORTY_SECONDS = Duration.ofSeconds(40);

    /*
    Use this method to create your ShortestPathFinder.
    This will be overridden during grading to use our solution path finder, so you don't get
    penalized again for any bugs in code from previous assignments
    */
    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> graph) {
        return new AStarPathFinder<>(graph);
    }

    public class SeamGraph implements AStarGraph<Point> {
        @Override
        public List<WeightedEdge<Point>> neighbors(Point p) {
            List<WeightedEdge<Point>> nbEdges = new ArrayList<>();
            boolean isHorizontal = p.isHorizontal();
            int start;
            int end;
            int width = width(energy);
            int height = height(energy);
            if (p.x() == width - 1 && isHorizontal || p.y() == height - 1 && !isHorizontal) {
                // End Point
                nbEdges.add(new WeightedEdge<>(p, new Point(width, height, isHorizontal), 0));
            } else if (isHorizontal) {
                if (p.x() == -1 && p.y() == -1) {
                    // Start Point
                    start = 0;
                    end = height - 1;
                } else {
                    // normal Points
                    start = p.y() - 1;
                    end = p.y() + 1;
                    // top
                    if (start < 0) {
                        start = 0;
                    }
                    // bottom
                    if (end > height - 1) {
                        end = height - 1;
                    }
                }
                for (int i = start; i <= end; i++) {
                    nbEdges.add(new WeightedEdge<>(p, new Point(p.x() + 1, i, true),
                        energy[p.x() + 1][i]));
                }
            } else {
                if (p.x() == -1 && p.y() == -1) {
                    // start point
                    start = 0;
                    end = width - 1;
                } else {
                    // normal points
                    start = p.x() - 1;
                    end = p.x() + 1;
                    // left
                    if (start < 0) {
                        start = 0;
                    }
                    // right
                    if (end > width - 1) {
                        end = width - 1;
                    }
                }
                for (int i = start; i <= end; i++) {
                    nbEdges.add(new WeightedEdge<>(p, new Point(i, p.y() + 1, false),
                        energy[i][p.y() + 1]));
                }
            }
            return nbEdges;
        }

        @Override
        public double estimatedDistanceToGoal(Point v, Point goal) {
            return 0;
        }
    }


    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        this.energy = energies;
        int height = height(energy);
        int width = width(energy);

        Point start = new Point(-1, -1, true);
        Point goal = new Point(width, height, true);
        SeamGraph sGraph = new SeamGraph();
        ShortestPathFinder<Point> finder = createPathFinder(sGraph);
        List<Point> result = finder.findShortestPath(start, goal, FORTY_SECONDS).solution();

        if (result.size() != 0) {
            List<Integer> seamHorizontal = new ArrayList<>(width);
            for (int i = 0; i < width; i++) {
                seamHorizontal.add(i, result.get(i + 1).y());
            }
            return seamHorizontal;
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        this.energy = energies;
        int height = height(energy);
        int width = width(energy);

        Point start = new Point(-1, -1, false);
        Point goal = new Point(width, height, false);
        SeamGraph sGraph = new SeamGraph();
        ShortestPathFinder<Point> finder = createPathFinder(sGraph);
        List<Point> result = finder.findShortestPath(start, goal, FORTY_SECONDS).solution();

        if (result.size() != 0) {
            List<Integer> seamVertical = new ArrayList<>(height);
            for (int i = 0; i < height; i++) {
                seamVertical.add(i, result.get(i + 1).x());
            }
            return seamVertical;
        } else {
            return null;
        }
    }

    public class Point {
        private int x;
        private int y;
        private boolean isHorizontal;

        public Point(int x, int y, boolean isHorizontal) {
            this.x = x;
            this.y = y;
            this.isHorizontal = isHorizontal;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public boolean isHorizontal() {
            return isHorizontal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Point)) {
                return false;
            }
            Point point = (Point) o;
            return x == point.x &&
                y == point.y &&
                isHorizontal == point.isHorizontal;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, isHorizontal);
        }
    }

    private int width(double[][] a) {
        return a.length;
    }

    private int height(double[][] a) {
        return a[0].length;
    }


}
