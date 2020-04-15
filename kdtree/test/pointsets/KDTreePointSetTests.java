package pointsets;
import java.util.Random;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class KDTreePointSetTests extends BaseTest {
    protected PointSet<Point> createPointSet(List<Point> points) {
        return new KDTreePointSet<>(points);
    }

    private static Random r = new Random(1300);
    @Test
    void edgeCaseA() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(2.9, 1.4);
        assertThat(actual).isEqualTo(new Point(4, 2));
    }

    @Test
    void edgeCaseB() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(7, 2.9);
        assertThat(actual).isEqualTo(new Point(7, 5));
    }

    @Test
    void edgeCaseC() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(8, 8);
        assertThat(actual).isEqualTo(new Point(6, 8));
    }

    @Test
    void edgeCaseD() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(3.5, 8.5);
        assertThat(actual).isEqualTo(new Point(2, 7));
    }

    @Test
    void edgeCaseE() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(4, 6);
        assertThat(actual).isEqualTo(new Point(2, 7));
    }

    private PointSet<Point> createInterestingSet() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(3, 3));
        points.add(new Point(2, 7));
        points.add(new Point(1, 1));
        points.add(new Point(4, 2));
        points.add(new Point(7, 5));
        points.add(new Point(6, 8));
        points.add(new Point(8, .66666));
        return createPointSet(points);
    }

    private PointSet<Point> createLectureSet() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(2, 3));
        points.add(new Point(4, 2));
        points.add(new Point(4, 5));
        points.add(new Point(3, 3));
        points.add(new Point(1, 5));
        points.add(new Point(4, 4));
        return createPointSet(points);
    }

    // ******************* my tests ********************************
    @Test
    void buildingLectureTree() {
        PointSet<Point> set = createLectureSet();
        Point actual = set.nearest(0, 7);
        assertThat(actual).isEqualTo(new Point(1, 5));
        // PointSet<Point> set2 = createInterestingSet();
    }
    // ************************ random tests **************************

    @Test
    public void testWith1000RandomPoints() {
        List<Point> points1000 = randomPoints(1000);
        NaivePointSet nps = new NaivePointSet(points1000);
        KDTreePointSet kd = new KDTreePointSet(points1000);

        List<Point> queries200 = randomPoints(200);
        for (Point p : queries200) {
            Point expected = nps.nearest(p.x(), p.y());
            Point actual = kd.nearest(p.x(), p.y());
            assertThat(expected).isEqualTo(actual);
        }
    }

    // return n random points
    private List<Point> randomPoints(int N) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i += 1) {
            points.add(randomPoint());
        }
        return points;
    }

    // return one random point
    private Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }


    // *****************************************************************
    @Test
    @DisplayName("Construct and Call Nearest With 40 Points On The Line y=x Returns Correct Point")
    void testPointsOnLine() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 41; i++) {
            points.add(new Point(i, i));
        }
        PointSet<Point> set = createPointSet(points);
        Point target = new Point(41, 41);

        Point actual = set.nearest(target);
        assertThat(actual).isEqualTo(new Point(40, 40));
    }

    @Test
    @DisplayName("Check Does Not Prune Valid Branch (A)")
    void pruningA() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(.001, .4));

        PointSet<Point> set = createPointSet(points);
        Point actual = set.nearest(-.3, .4);
        assertThat(actual).isEqualTo(new Point(.001, .4));
    }

    @Test
    @DisplayName("Check Does Not Prune Valid Branch (B)")
    void pruningB() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(.4, .001));

        PointSet<Point> set = createPointSet(points);
        Point actual = set.nearest(.4, -.3);
        assertThat(actual).isEqualTo(new Point(.4, .001));
    }
}
