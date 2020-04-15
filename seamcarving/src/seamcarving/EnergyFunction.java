package seamcarving;

import edu.princeton.cs.algs4.Picture;

@FunctionalInterface
public interface EnergyFunction {
    /**
     * Returns the energy of pixel (x, y) in the given image.
     * @throws IndexOutOfBoundsException if (x, y) is not inside of the given image.
     */
    double apply(Picture picture, int x, int y);
}
