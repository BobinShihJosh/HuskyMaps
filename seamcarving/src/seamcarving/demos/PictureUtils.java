package seamcarving.demos;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;
import seamcarving.SeamCarver;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;

/**
 *  Some utility functions for testing SeamCarver.
 */
public class PictureUtils {
    public static final Path IMAGE_DIR = Path.of("seamcarving/data");

    public static Picture loadPicture(String filename) {
        return new Picture(IMAGE_DIR.resolve(filename).toFile());
    }

    /** Creates a random W-by-H picture. */
    public static Picture randomPicture(int W, int H) {
        Picture picture = new Picture(W, H);
        for (int col = 0; col < W; col++) {
            for (int row = 0; row < H; row++) {
                int r = StdRandom.uniform(256);
                int g = StdRandom.uniform(256);
                int b = StdRandom.uniform(256);
                Color color = new Color(r, g, b);
                picture.set(col, row, color);
            }
        }
        return picture;
    }

    /** Displays grayscale values as energy (converts to picture, calls show). */
    public static void showEnergy(SeamCarver sc) {
        doubleToPicture(sc.computeEnergies()).show();
    }

    /** Returns a picture of the energy matrix associated with the SeamCarver picture. */
    public static Picture toEnergyPicture(SeamCarver sc) {
        double[][] energyMatrix = sc.computeEnergies();
        return doubleToPicture(energyMatrix);
    }

    /**
     * Converts a double matrix of values into a normalized picture.
     * Values are normalized by the maximum grayscale value.
     */
    public static Picture doubleToPicture(double[][] grayValues) {
        /*
        each 1D array in the matrix represents a single column, so number
        of 1D arrays is the width, and length of each array is the height
        */
        int width = grayValues.length;
        int height = grayValues[0].length;

        Picture picture = new Picture(width, height);

        double maxVal = 0;
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (grayValues[col][row] > maxVal) {
                    maxVal = grayValues[col][row];
                }
            }
        }

        if (maxVal == 0) {
            return picture;  // return black picture
        }

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                float normalizedGrayValue = (float) grayValues[col][row] / (float) maxVal;
                Color gray = new Color(normalizedGrayValue, normalizedGrayValue, normalizedGrayValue);
                picture.set(col, row, gray);
            }
        }

        return picture;
    }


    /**
     * Overlays red pixels over the calculated seam.
     * This method is useful for debugging seams.
     */
    public static Picture seamOverlay(Picture picture, boolean isHorizontal, List<Integer> seam) {
        Picture overlaid = new Picture(picture);

        // if horizontal seam, set one pixel in every column
        if (isHorizontal) {
            for (int col = 0; col < picture.width(); col++) {
                overlaid.set(col, seam.get(col), Color.RED);
            }
        }

        // if vertical seam, set one pixel in each row
        else {
            for (int row = 0; row < picture.height(); row++) {
                overlaid.set(seam.get(row), row, Color.RED);
            }
        }

        return overlaid;
    }

}
