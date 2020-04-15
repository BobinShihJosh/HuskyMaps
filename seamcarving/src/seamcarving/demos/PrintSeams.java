package seamcarving.demos;

import edu.princeton.cs.algs4.Picture;
import seamcarving.AStarSeamFinder;
import seamcarving.DualGradientEnergyFunction;
import seamcarving.SeamCarver;
import seamcarving.Utils;
import seamcarving.Utils.Orientation;

import java.util.List;

import static seamcarving.Utils.Orientation.HORIZONTAL;
import static seamcarving.Utils.Orientation.VERTICAL;
import static seamcarving.demos.PictureUtils.loadPicture;

/**
 *  Prints energies of pixels, a vertical seam, and a horizontal seam.
 *
 *  The table gives the dual-gradient energies of each pixel.
 *  The asterisks denote a minimum-energy vertical or horizontal seam.
 */
public class PrintSeams {

    protected static void printSeam(SeamCarver carver, List<Integer> seam, Orientation direction) {
        double[][] energies = carver.computeEnergies();

        System.out.println(Utils.superimposeSeamOnEnergies(energies, seam, direction, ""));
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        Picture picture = loadPicture("3x4.png");
        SeamCarver carver = new SeamCarver(picture, new DualGradientEnergyFunction(), new AStarSeamFinder());
        main(carver, picture);

    }

    public static void main(SeamCarver carver, Picture picture) {
        System.out.printf("%d-by-%d image%n", picture.width(), picture.height());
        System.out.println();
        System.out.println("The table gives the dual-gradient energies of each pixel.");
        System.out.println("The asterisks denote a minimum energy vertical or horizontal seam.");
        System.out.println();

        List<Integer> verticalSeam = carver.findVerticalSeam();
        System.out.println("Vertical seam: " + verticalSeam);
        printSeam(carver, verticalSeam, VERTICAL);

        List<Integer> horizontalSeam = carver.findHorizontalSeam();
        System.out.println("Horizontal seam: " + horizontalSeam);
        printSeam(carver, horizontalSeam, HORIZONTAL);
    }

}
