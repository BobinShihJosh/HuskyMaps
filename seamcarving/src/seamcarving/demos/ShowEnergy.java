package seamcarving.demos;

import edu.princeton.cs.algs4.Picture;
import seamcarving.AStarSeamFinder;
import seamcarving.DualGradientEnergyFunction;
import seamcarving.SeamCarver;

import static seamcarving.demos.PictureUtils.loadPicture;

/**
 * Shows 2 images: original image and the energies of each of its pixel (as a grayscale image).
 */
public class ShowEnergy {
    public static void main(String[] args) {
        Picture picture = loadPicture("HJoceanSmall.png");
        SeamCarver sc = new SeamCarver(picture,
            new DualGradientEnergyFunction(),
            new AStarSeamFinder());
        main(sc, picture);
    }

    public static void main(SeamCarver sc, Picture picture) {
        System.out.printf("%d-by-%d image%n", picture.width(), picture.height());
        picture.show();

        System.out.println("Displaying energy calculated for each pixel.");
        PictureUtils.showEnergy(sc);
    }
}
