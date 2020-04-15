package autocomplete;

// import java.nio.channels.FileLockInterruptionException;

import java.util.Objects;

/**
 * This is currently the only implementation of the {@link Term} interface, which is why it's named
 * "default." (Having an interface with a single implementation is a little redundant, but we need
 * it to keep you from accidentally renaming things.)
 * <p>
 * Make sure to check out the interface for method specifications.
 *
 * @see Term
 */
public class DefaultTerm implements Term {
    private String query;
    private long weight;

    @Override
    public String toString() {
        return "DefaultTerm{" +
            "query='" + query + '\'' +
            ", weight=" + weight +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultTerm)) {
            return false;
        }
        DefaultTerm that = (DefaultTerm) o;
        return weight == that.weight &&
            Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, weight);
    }

    /**
     * Initializes a term with the given query string and weight.
     *
     * @throws IllegalArgumentException if query is null or weight is negative
     */
    public DefaultTerm(String query, long weight) {
        //throw new UnsupportedOperationException("Not implemented yet.");
        if (query == null || weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    @Override
    public String query() {
        // throw new UnsupportedOperationException("Not implemented yet.");
        return this.query;
    }

    @Override
    public long weight() {
        //throw new UnsupportedOperationException("Not implemented yet.");
        return this.weight;
    }

    @Override
    public int queryOrder(Term that) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (that == null) {
            throw new NullPointerException();
        }
        return this.query.compareTo(that.query());
    }

    @Override
    public int reverseWeightOrder(Term that) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (that == null) {
            throw new NullPointerException();
        }
        return Long.compare(that.weight(), this.weight);
        // if (this.weight > that.weight()) {
        //     return -1;
        // } else if (this.weight < that.weight()) {
        //     return 1;
        // } else {
        //     return 0;
        // }
    }

    @Override
    public int matchesPrefix(String prefix) {
        // throw new UnsupportedOperationException("Not implemented yet.");
        if (prefix == null) {
            throw new NullPointerException();
        }
        if (prefix == "") {
            return 0;
        }
        if (this.query.length() > prefix.length()) {
            for (int i = 0; i < prefix.length(); i++) {
                if (this.query.charAt(i) - prefix.charAt(i) > 0) {
                    return 1;
                } else if (this.query.charAt(i) - prefix.charAt(i) < 0) {
                    return -1;
                }
            }
            return 0;
        }

        return this.query.compareTo(prefix);


    }
}
