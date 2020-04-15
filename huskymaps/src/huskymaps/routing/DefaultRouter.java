package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import pointsets.KDTreePointSet;
import pointsets.Point;
import pointsets.PointSet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static huskymaps.utils.Spatial.projectToPoint;

/**
 * @see Router
 */
public class DefaultRouter extends Router {
    private StreetMapGraph graph;
    private KDTreePointSet<Point> kdTree;
    private AStarPathFinder<AStarGraph<Point>> aStar;
    private List<Point> initPoints = new ArrayList<>();
    private List<Point> p = new ArrayList<>();
    private HashMap<Point, Node> getNode = new HashMap<>();
    private static final Duration FORTY_SECONDS = Duration.ofSeconds(40);
    private PointSet pointS;

    public DefaultRouter(StreetMapGraph graph) {
        this.graph = graph;
        for (Node node : graph.allNodes()) {
            NodePoint nodeP = createNodePoint(node);
            if (!(graph.neighbors(node).isEmpty())) {
                initPoints.add(nodeP);
                getNode.put(new Point(nodeP.x(), nodeP.y()), node);
            }
        }
        pointS = new KDTreePointSet(initPoints); //createPointSet(initPoints);
        //kdTree = (KDTreePointSet<Point>) createPointSet(initPoints);

        // replace this with your code
    }

    @Override
    protected <T extends Point> PointSet<T> createPointSet(List<T> points) {
        // uncomment (and import) if you want to use WeirdPointSet instead of your own KDTreePointSet:
        // return new WeirdPointSet<>(points);
        return KDTreePointSet.createAfterShuffling(points);
    }

    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g) {
        return new AStarPathFinder<>(g);
    }

    @Override
    protected NodePoint createNodePoint(Node node) {
        return projectToPoint(Coordinate.fromNode(node), (x, y) -> new NodePoint(x, y, node));
    }

    @Override
    protected Node closest(Coordinate c) {
        // Project to x and y coordinates instead of using raw lat and lon for finding closest points:
        Point p1 = projectToPoint(c, Point::new);

        Point nearest = pointS.nearest(p1);
        Node result = getNode.get(nearest);
        return result;
    }

    @Override
    public List<Node> shortestPath(Coordinate start, Coordinate end) {
        Node src = closest(start);
        Node dest = closest(end);

        ShortestPathFinder sgf = new AStarPathFinder(graph); //createPathFinder(graph);
        List<Node> result = sgf.findShortestPath(src, dest, FORTY_SECONDS).solution();
        return result;
        // replace this with your code
        /*
        Feel free to use any arbitrary duration for your path finding timeout; we don't expect
        queries to take more than a few seconds, so e.g. 10-30 seconds is pretty reasonable.
         */
    }

    @Override
    public List<NavigationDirection> routeDirections(List<Node> route) {
        // Optional
        return null;
    }
}
