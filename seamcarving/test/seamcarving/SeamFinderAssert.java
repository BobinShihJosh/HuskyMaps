package seamcarving;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.FactoryBasedNavigableListAssert;
import org.assertj.core.api.IntegerAssert;
import seamcarving.Utils.Orientation;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static seamcarving.Utils.Orientation.HORIZONTAL;
import static seamcarving.Utils.Orientation.VERTICAL;

/**
 * Asserts for ShortestPathFinders and ShortestPathResults.
 */
public class SeamFinderAssert extends AbstractObjectAssert<SeamFinderAssert, SeamFinder> {

    public SeamFinderAssert(SeamFinder actual) {
        super(actual, SeamFinderAssert.class);
    }

    public SeamAssert verticalSeam(double[][] energies) {
        return new SeamAssert(actual.findVerticalSeam(energies), energies, VERTICAL);
    }
    public SeamAssert horizontalSeam(double[][] energies) {
        return new SeamAssert(actual.findHorizontalSeam(energies), energies, HORIZONTAL);
    }

    public static class SeamAssert
        extends FactoryBasedNavigableListAssert<SeamAssert, List<Integer>, Integer, IntegerAssert> {

        private static final int MAX_WIDTH_PICTURE_TO_PRINT = 12;
        private static final int MAX_HEIGHT_PICTURE_TO_PRINT = 12;
        private static final double EPSILON = 1E-12;
        private final double[][] energies;
        private final Orientation orientation;
        private final int width;
        private final int height;
        private final int size;
        private final int maxVal;

        public SeamAssert(List<Integer> actual,
                          double[][] energies,
                          Orientation orientation) {
            super(actual, SeamAssert.class, IntegerAssert::new);
            this.energies = energies;
            this.orientation = orientation;
            this.width = energies.length;
            this.height = energies[0].length;
            if (this.orientation == VERTICAL) {
                this.size = this.height;
                this.maxVal = this.width;
            } else {
                this.size = this.width;
                this.maxVal = this.height;
            }
        }

        public SeamAssert isValid() {
            as("seam validity check").isNotNull();
            as("seam validity check: seam size").hasSize(this.size);
            as("seam validity check: seam values min/max")
                .allMatch(i -> i >= 0 && i < this.maxVal, "0 <= val < " + this.maxVal);
            List<Integer> withoutFirst = this.actual.subList(1, this.actual.size());
            List<Integer> withoutLast = this.actual.subList(0, this.actual.size() - 1);
            assertThat(withoutFirst)
                .as("seam validity check: seam values adjacency")
                .withFailMessage("%nSeam is disconnected:%n" + superimposeSeamOnEnergies(this.actual))
                .zipSatisfy(withoutLast, (a, b) -> assertThat(b)
                    .as("adjacent seam values")
                    .isBetween(a-1, a+1));

            return this;
        }

        // uses Integer array since it's harder to convert an int array to List<Integer>
        public SeamAssert hasSameEnergyAs(Integer... other) {
            return hasSameEnergyAs(Arrays.asList(other));
        }

        public SeamAssert hasSameEnergyAs(List<Integer> other) {
            isValid();

            double actualEnergy = calculateSeamEnergy(this.actual);
            double expectedEnergy = calculateSeamEnergy(other);
            assertThat(actualEnergy).as("energy of seam")
                .withFailMessage("%nExpecting:%n%s%nto have same energy as:%n%s",
                    superimposeSeamOnEnergies(this.actual), superimposeSeamOnEnergies(other))
                .isCloseTo(expectedEnergy, within(EPSILON));
            return this;
        }

        private String superimposeSeamOnEnergies(List<Integer> seam) {
            String prefix = "  ";
            if (this.width <= MAX_WIDTH_PICTURE_TO_PRINT && this.height <= MAX_HEIGHT_PICTURE_TO_PRINT) {
                return Utils.superimposeSeamOnEnergies(this.energies, seam, this.orientation, prefix);
            } else {
                StringBuilder message = new StringBuilder(prefix)
                    .append("[ ").append(this.width).append("-by-").append(this.height)
                    .append(" image; omitting full seam/energies from output ]")
                    .append(System.lineSeparator());
                double totalSeamEnergy = calculateSeamEnergy(seam);
                message.append(prefix).append("Total energy: ").append(totalSeamEnergy);
                return message.toString();
            }
        }

        private double calculateSeamEnergy(List<Integer> seam) {
            double seamEnergy = 0.0;
            if (orientation == HORIZONTAL) {
                for (int i = 0; i < energies.length; i++) {
                    seamEnergy += energies[i][seam.get(i)];
                }
            } else {
                for (int j = 0; j < energies[0].length; j++) {
                    seamEnergy += energies[seam.get(j)][j];
                }
            }
            return seamEnergy;
        }
    }
}
