package seamcarving;

import edu.princeton.cs.algs4.Picture;
import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class BasicTests extends BaseTest {
    protected SeamFinderAssert assertThat(SeamFinder seamFinder) {
        return new SeamFinderAssert(seamFinder);
    }

    @Test
    public void sanityEnergyTest() {
        // Creates the sample array specified in the spec and checks for matching energies
        Picture p = new Picture(3, 4);
        int[][][] exampleArray = {{{255, 101, 51}, {255, 101, 153}, {255, 101, 255}},
                                  {{255, 153, 51}, {255, 153, 153}, {255, 153, 255}},
                                  {{255, 203, 51}, {255, 204, 153}, {255, 205, 255}},
                                  {{255, 255, 51}, {255, 255, 153}, {255, 255, 255}}};
        double[][] exampleEnergySquared = {{52852.0, 52641.0, 52432.0},
                                           {52020.0, 52225.0, 52432.0},
                                           {52024.0, 52024.0, 52024.0},
                                           {52852.0, 52020.0, 51220.0}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                int[] colorVals = exampleArray[j][i];
                p.set(i, j, new Color(colorVals[0], colorVals[1], colorVals[2]));
            }
        }

        EnergyFunction energyFunction = new DualGradientEnergyFunction();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                double energy = energyFunction.apply(p, i, j);
                assertThat(energy)
                    .as("energy of pixel (%d, %d)", i, j)
                    .isCloseTo(Math.sqrt(exampleEnergySquared[j][i]), within(1e-3));
            }
        }
    }

    @Test
    public void sanityVerticalSeamTest() {
        Picture p = new Picture("data/6x5.png");
        EnergyFunction energyFunction = new DualGradientEnergyFunction();
        double[][] energies = SeamCarver.computeEnergies(p, energyFunction);

        SeamFinder seamFinder = new AStarSeamFinder();
        assertThat(seamFinder).verticalSeam(energies).hasSameEnergyAs(4, 4, 3, 2, 2);
    }

    @Test
    public void sanityHorizontalSeamTest() {
        Picture p = new Picture("data/6x5.png");
        EnergyFunction energyFunction = new DualGradientEnergyFunction();
        double[][] energies = SeamCarver.computeEnergies(p, energyFunction);

        SeamFinder seamFinder = new AStarSeamFinder();
        assertThat(seamFinder).horizontalSeam(energies).hasSameEnergyAs(2, 2, 1, 2, 1, 1);
    }
}
