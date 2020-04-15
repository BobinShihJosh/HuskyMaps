package seamcarving;

import edu.princeton.cs.algs4.Picture;
// import java.lang.Math;

public class DualGradientEnergyFunction implements EnergyFunction {
    private Picture pic;


    @Override
    public double apply(Picture picture, int x, int y) {
        this.pic = picture;
        double rx;
        double gx;
        double bx;
        double ry;
        double gy;
        double by;
        // Test to see if pixel is at edge and corner
        if (x == 0 && y == 0) {
            // top left
            rx = fbDifference(true, 0, picture, 1, x, y);
            gx = fbDifference(true, 1, picture, 1, x, y);
            bx = fbDifference(true, 2, picture, 1, x, y);
            ry = fbDifference(false, 0, picture, 1, x, y);
            gy = fbDifference(false, 1, picture, 1, x, y);
            by = fbDifference(false, 2, picture, 1, x, y);
        } else if (x == 0 && y == picture.height() - 1) {
            // bottom left
            rx = fbDifference(true, 0, picture, 1, x, y);
            gx = fbDifference(true, 1, picture, 1, x, y);
            bx = fbDifference(true, 2, picture, 1, x, y);
            ry = fbDifference(false, 0, picture, -1, x, y);
            gy = fbDifference(false, 1, picture, -1, x, y);
            by = fbDifference(false, 2, picture, -1, x, y);
        } else if (x == picture.width() - 1 && y == picture.height() - 1) {
            // bottom right
            rx = fbDifference(true, 0, picture, -1, x, y);
            gx = fbDifference(true, 1, picture, -1, x, y);
            bx = fbDifference(true, 2, picture, -1, x, y);
            ry = fbDifference(false, 0, picture, -1, x, y);
            gy = fbDifference(false, 1, picture, -1, x, y);
            by = fbDifference(false, 2, picture, -1, x, y);
        } else if (x == picture.width() - 1 && y == 0) {
            // top right
            rx = fbDifference(true, 0, picture, -1, x, y);
            gx = fbDifference(true, 1, picture, -1, x, y);
            bx = fbDifference(true, 2, picture, -1, x, y);
            ry = fbDifference(false, 0, picture, 1, x, y);
            gy = fbDifference(false, 1, picture, 1, x, y);
            by = fbDifference(false, 2, picture, 1, x, y);
        } else if (x == 0) {
            // left
            rx = fbDifference(true, 0, picture, 1, x, y);
            gx = fbDifference(true, 1, picture, 1, x, y);
            bx = fbDifference(true, 2, picture, 1, x, y);
            ry = centDif(0, false, picture, x, y);
            gy = centDif(1, false, picture, x, y);
            by = centDif(2, false, picture, x, y);
        } else if (x == picture.width() - 1) {
            // right
            rx = fbDifference(true, 0, picture, -1, x, y);
            gx = fbDifference(true, 1, picture, -1, x, y);
            bx = fbDifference(true, 2, picture, -1, x, y);
            ry = centDif(0, false, picture, x, y);
            gy = centDif(1, false, picture, x, y);
            by = centDif(2, false, picture, x, y);
        } else if (y == 0) {
            // top
            rx = centDif(0, true, picture, x, y);
            gx = centDif(1, true, picture, x, y);
            bx = centDif(2, true, picture, x, y);
            ry = fbDifference(false, 0, picture, 1, x, y);
            gy = fbDifference(false, 1, picture, 1, x, y);
            by = fbDifference(false, 2, picture, 1, x, y);
        } else if (y == picture.height() - 1) {
            // bottom
            rx = centDif(0, true, picture, x, y);
            gx = centDif(1, true, picture, x, y);
            bx = centDif(2, true, picture, x, y);
            ry = fbDifference(false, 0, picture, -1, x, y);
            gy = fbDifference(false, 1, picture, -1, x, y);
            by = fbDifference(false, 2, picture, -1, x, y);
        } else {
            // not at edge or corner
            rx = centDif(0, true, picture, x, y);
            gx = centDif(1, true, picture, x, y);
            bx = centDif(2, true, picture, x, y);
            ry = centDif(0, false, picture, x, y);
            gy = centDif(1, false, picture, x, y);
            by = centDif(2, false, picture, x, y);
        }

        double dXsqr = (rx * rx) + (gx * gx) + (bx * bx);
        double dYsqr = (ry * ry) + (gy * gy) + (by * by);
        double energy = Math.sqrt(dXsqr + dYsqr);

        return energy;

    }

    private double centDif(int color, boolean getX, Picture img, int x, int y) {
        double centralDifference;
        if (getX) {
            if (color == 0) {
                centralDifference = img.get(x + 1, y).getRed() - img.get(x - 1, y).getRed();
            } else if (color == 1) {
                centralDifference = img.get(x + 1, y).getGreen() - img.get(x - 1, y).getGreen();
            } else {
                centralDifference = img.get(x + 1, y).getBlue() - img.get(x - 1, y).getBlue();
            }
        } else {
            if (color == 0) {
                centralDifference = img.get(x, y + 1).getRed() - img.get(x, y - 1).getRed();
            } else if (color == 1) {
                centralDifference = img.get(x, y + 1).getGreen() - img.get(x, y - 1).getGreen();
            } else {
                centralDifference = img.get(x, y + 1).getBlue() - img.get(x, y - 1).getBlue();
            }
        }
        return centralDifference;
    }

    // color 1,2,3 = r,g,b
    private double fbDifference(boolean getX, int color, Picture img, int d, int x, int y) {
        double gradient;
        if (getX) {
            if (color == 0) {
                gradient = -3 * img.get(x, y).getRed() + 4 * img.get(x + d, y).getRed()
                    - img.get(x + 2 * d, y).getRed();
            } else if (color == 1) {
                gradient = -3 * img.get(x, y).getGreen() + 4 * img.get(x + d, y).getGreen()
                    - img.get(x + 2 * d, y).getGreen();
            } else {
                gradient = -3 * img.get(x, y).getBlue() + 4 * img.get(x + d, y).getBlue()
                    - img.get(x + 2 * d, y).getBlue();
            }
        } else {
            if (color == 0) {
                gradient = -3 * img.get(x, y).getRed() + 4 * img.get(x, y + d).getRed()
                    - img.get(x, y + 2 * d).getRed();
            } else if (color == 1) {
                gradient = -3 * img.get(x, y).getGreen() + 4 * img.get(x, y + d).getGreen()
                    - img.get(x, y + 2 * d).getGreen();
            } else {
                gradient = -3 * img.get(x, y).getBlue() + 4 * img.get(x, y + d).getBlue()
                    - img.get(x, y + 2 * d).getBlue();
            }
        }
        return gradient;
    }

}
