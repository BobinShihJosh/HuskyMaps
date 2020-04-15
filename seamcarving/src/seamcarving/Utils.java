package seamcarving;

import java.util.List;

import static seamcarving.Utils.Orientation.VERTICAL;

public class Utils {

    /** A basic enum for horizontal/vertical directions. */
    public enum Orientation {
        // Tip: using these in your code could help reduce the number of different branches you need to maintain.
        HORIZONTAL, VERTICAL
    }

    /** Returns true iff pixel (x, y) is in the current image. */
    protected static boolean inBounds(int x, int y, int width, int height) {
        return (x >= 0) && (x < width) && (y >= 0) && (y < height);
    }

    public static String superimposeSeamOnEnergies(double[][] energies,
                                                   List<Integer> seam,
                                                   Orientation orientation,
                                                   String prefix) {
        double totalSeamEnergy = 0;
        int width = energies.length;
        int height = energies[0].length;
        StringBuilder message = new StringBuilder();
        for (int row = 0; row < height; row++) {
            message.append(prefix);
            for (int col = 0; col < width; col++) {
                double energy = energies[col][row];
                String marker;
                if (isPixelInSeam(seam, col, row, orientation)) {
                    marker = "*";
                    totalSeamEnergy += energy;
                } else {
                    marker = " ";
                }
                message.append(String.format("%7.2f%s ", energy, marker));
            }
            message.append(System.lineSeparator());
        }
        message.append(prefix).append("Total energy: ").append(totalSeamEnergy);
        return message.toString();
    }

    private static boolean isPixelInSeam(List<Integer> seam, int col, int row, Orientation orientation) {
        if (orientation == VERTICAL) {
            return col == seam.get(row);
        } else {
            return row == seam.get(col);
        }
    }
}
