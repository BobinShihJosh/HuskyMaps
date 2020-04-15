package seamcarving;

import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The primary class for seam carving, which combines an energy function and a seam finding
 * algorithm to carve seams in a picture.
 */
public class SeamCarver {
    private final EnergyFunction energyFunction;
    private final SeamFinder seamFinder;
    private Picture picture;

    public SeamCarver(Picture picture, EnergyFunction energyFunction, SeamFinder seamFinder) {
        this.energyFunction = energyFunction;
        this.seamFinder = seamFinder;
        if (picture == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    /** Sets the current image. */
    private void setPicture(Picture picture) {
        this.picture = picture;
    }

    /** Returns the width of the current image, in pixels. */
    public int width() {
        return picture.width();
    }

    /** Returns the height of the current image, in pixels. */
    public int height() {
        return picture.height();
    }

    public double[][] computeEnergies() {
        return computeEnergies(this.picture, this.energyFunction);
    }

    public static double[][] computeEnergies(Picture picture, EnergyFunction f) {
        double[][] output = new double[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                output[i][j] = f.apply(picture, i, j);
            }
        }
        return output;
    }

    /** Calculates and removes a minimum-energy horizontal seam from the current image. */
    public void removeHorizontalSeam() {
        List<Integer> seam = findHorizontalSeam();
        removeHorizontalSeam(seam);
    }

    public List<Integer> findHorizontalSeam() {
        double[][] energies = computeEnergies();
        return this.seamFinder.findHorizontalSeam(energies);
    }

    /** Removes a horizontal seam from the current image. */
    public void removeHorizontalSeam(List<Integer> seam) {
        Picture carvedPicture = removeHorizontalSeam(seam, this.picture, picture.width(), picture.height(),
            Picture::new, Picture::get, Picture::set);

        setPicture(carvedPicture);
    }

    @FunctionalInterface
    interface Getter<T, U> {
        U get(T t, int x, int y);
    }

    @FunctionalInterface
    interface Setter<T, U> {
        void set(T t, int x, int y, U u);
    }

    public static <T> T removeHorizontalSeam(List<Integer> seam,
                                             T original,
                                             int width,
                                             int height,
                                             BiFunction<Integer, Integer, T> factory,
                                             Getter<T, Color> getter,
                                             Setter<T, Color> setter) {
        if (seam == null) {
            throw new NullPointerException("Input seam array cannot be null.");
        } else if (seam.size() == 1) {
            throw new IllegalArgumentException("Cannot remove seam of length 1.");
        } else if (seam.size() != width) {
            throw new IllegalArgumentException("Seam length does not match image.");
        }

        for (int i = 0; i < seam.size() - 2; i++) {
            if (Math.abs(seam.get(i) - seam.get(i + 1)) > 1) {
                throw new IllegalArgumentException(
                    "Invalid seam, consecutive vertical indices are greater than one apart.");
            }
        }

        T output = factory.apply(width, height - 1);
        // Copy over all indices other than those in the seam
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < seam.get(i); j++) {
                Color val = getter.get(original, i, j);
                setter.set(output, i, j, val);
            }

            for (int j = seam.get(i) + 1; j < height; j++) {
                Color val = getter.get(original, i, j);
                setter.set(output, i, j - 1, val);
            }
        }

        return output;
    }

    public static <T> T removeVerticalSeam(List<Integer> seam,
                                           T original,
                                           int width,
                                           int height,
                                           BiFunction<Integer, Integer, T> factory,
                                           Getter<T, Color> getter,
                                           Setter<T, Color> setter) {
        // Transpose x and y in every operation on the image
        return removeHorizontalSeam(seam, original, height, width,
            (y, x) -> factory.apply(x, y),
            (p, y, x) -> getter.get(p, x, y),
            (p, y, x, color) -> setter.set(p, x, y, color));
    }

    /** Calculates and removes a minimum-energy vertical seam from the current image. */
    public void removeVerticalSeam() {
        List<Integer> seam = findVerticalSeam();
        removeVerticalSeam(seam);
    }

    public List<Integer> findVerticalSeam() {
        double[][] energies = computeEnergies();
        return this.seamFinder.findVerticalSeam(energies);
    }

    /** Removes a vertical seam from the current image. */
    public void removeVerticalSeam(List<Integer> seam) {
        Picture carvedPicture = removeVerticalSeam(seam, this.picture, picture.width(), picture.height(),
            Picture::new, Picture::get, Picture::set);

        setPicture(carvedPicture);
    }
}
