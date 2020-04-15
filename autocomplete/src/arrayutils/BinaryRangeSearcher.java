package arrayutils;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Make sure to check out the interface for more method details:
 *
 * @see ArraySearcher
 */
public class BinaryRangeSearcher<T, U> implements ArraySearcher<T, U> {
    private final T[] array;
    private final Matcher<T, U> matcher;

    /**
     * Creates a BinaryRangeSearcher for the given array of items that matches items using the
     * Matcher matchUsing.
     *
     * First sorts the array in place using the Comparator sortUsing. (Assumes that the given array
     * will not be used externally afterwards.)
     *
     * Requires that sortUsing sorts the array such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * For example:
     * sortUsing lexicographic string sort: [  aaa,  abc,   ba,  bzb, cdef ]
     * matchUsing T is prefixed by U
     * matchUsing.match for prefix "b":     [   -1,   -1,    0,    0,    1 ]
     *
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if sortUsing or matchUsing is null
     */
    public static <T, U> BinaryRangeSearcher<T, U> forUnsortedArray(T[] array,
                                                                    Comparator<T> sortUsing,
                                                                    Matcher<T, U> matchUsing) {
        if (array == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        if (sortUsing == null || matchUsing == null) {
            throw new IllegalArgumentException();
        }
        /*
        throw exceptions when necessary
        Tip: To reduce redundancy, you can let the BinaryRangeSearcher constructor throw some of
        the exceptions mentioned in this method's documentation. The caller doesn't care which
        method exactly causes the exception, as long as it's something that happens while
        executing this method.
        */
        Arrays.sort(array, sortUsing);
        return new BinaryRangeSearcher<>(array, matchUsing);
    }

    /**
     * Requires that array is sorted such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate the array).
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if matcher is null
     */

    protected BinaryRangeSearcher(T[] array, Matcher<T, U> matcher) {
        if (array == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        if (matcher == null) {
            throw new IllegalArgumentException();
        }
        this.array = array;
        this.matcher = matcher;
    }

    public MatchResult<T> findAllMatches(U target) {
        // replace this with your code
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (target == null) {
            throw new IllegalArgumentException();
        }

        // call private helper method to find the first match
        int first = findMatch(target, 1);

        // call private helper method to find the last match
        int last = findMatch(target, 0) + 1;

        if (first == array.length + 1 && last == array.length + 2) {
            last = 0;
            first = 0;
        }
        // for (int i = first; i < last; i++) {
        //     matches.add(array[i]);
        // }
        //int count = last - first;
        // String[] matchedArray = Arrays.copyOfRange(array, first, last-1);
        //
        // ArrayList<T> matches = new ArrayList<>(count);

        // first = 0;
        // last = count;


        //T[] matchesArray = matches.toArray(Arrays.copyOf(this.array, matches.size()));
        return new MatchResult<>(array, first, last);

    }

    private int findMatch(U target, int side) {
        // Iterative solution
        int startEnd = array.length + 1;

        int size = array.length - 1;
        int bound = 0;
        while (bound <= size) {
            int mid = (bound + size) / 2;

            if (this.matcher.match(array[mid], target) == 0) {
                startEnd = mid;
                // matches.add(array[mid]);
                if (side == 1) {
                    size = mid - 1;
                } else if (side == 0) {
                    bound = mid + 1;
                }
            }

            if (this.matcher.match(array[mid], target) < 0) {
                bound = mid + 1; // if matchee's value is smaller than target
            } else if (this.matcher.match(array[mid], target) > 0) {
                size = mid - 1; // if matchee's values is higher than target
            }
        }
        return startEnd;
    }

    public static class MatchResult<T> extends AbstractMatchResult<T> {
        final T[] array;
        final int start;
        final int end;

        /**
         * Use this constructor if there are no matching results.
         * (This lets us use Arrays.copyOfRange to make a new T[], which can be difficult to
         * acquire otherwise due to the way Java handles generics.)
         */
        protected MatchResult(T[] array) {
            this(array, 0, 0);
        }

        protected MatchResult(T[] array, int startInclusive, int endExclusive) {
            this.array = array;
            this.start = startInclusive;
            this.end = endExclusive;
        }

        @Override
        public int count() {
            return this.end - this.start;
        }

        @Override
        public T[] unsorted() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
