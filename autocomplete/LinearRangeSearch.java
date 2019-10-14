package autocomplete;

import java.util.*;

public class LinearRangeSearch implements Autocomplete {

    private Term[] terms;
    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public LinearRangeSearch(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException();
        }
        for (Term term: terms) {
            if (term.query() == null) {
                throw new IllegalArgumentException();
            }
        }
        this.terms = terms;

    }

    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        List<Term> match = new ArrayList<>();
        for (Term term: terms) {
            if (term.query().startsWith(prefix)) {
                match.add(term);
            }
        }

        Term[] result = new Term[match.size()];
        result = match.toArray(result);
        Arrays.sort(result, TermComparators.byReverseWeightOrder());

        return result;
    }
}

