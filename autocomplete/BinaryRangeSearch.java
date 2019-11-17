package autocomplete;

import java.util.Arrays;

public class BinaryRangeSearch implements Autocomplete {
    private Term[] terms;

    /**
     * Validates and stores the given array of terms.
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate it).
     * @throws IllegalArgumentException if terms is null or contains null
     */
    public BinaryRangeSearch(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException();
        }
        for (Term term: terms) {
            if (term == null) {
                throw new IllegalArgumentException();
            }
        }
        mergeSort(terms);
        this.terms = terms;
    }

    private void mergeSort(Term[] result) {
        if (result.length > 1) {
            Term[] left = Arrays.copyOfRange(result, 0, result.length / 2);
            Term[] right = Arrays.copyOfRange(result, result.length / 2, result.length);

            mergeSort(left);
            mergeSort(right);

            merge(result, left, right);
        }
    }

    private void merge(Term[] result, Term[] left, Term[] right) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < result.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1].compareTo(right[i2]) <= 0)) {
                result[i] = left[i1];
                i1++;
            } else {
                result[i] = right[i2];
                i2++;
            }
        }
    }
    /**
     * Returns all terms that start with the given prefix, in descending order of weight.
     * @throws IllegalArgumentException if prefix is null
     */
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }

        Term prefixTerm = new Term(prefix, 0);

        // reference from https://www.geeksforgeeks.org/find-first-and-last-positions-of-an-element-in-a-sorted-array/

        int leftMost = leftMostIndex(prefixTerm, 0, terms.length);
        int rightMost = rightMostIndex(prefixTerm, leftMost, terms.length);

        if (leftMost == -1 || rightMost == -1) {
            return new Term[0];
        } else {
            Term[] result = new Term[rightMost - leftMost + 1];
            for (int i = 0; i < result.length; i++) {
                result[i] = terms[leftMost + i];
            }
            Arrays.sort(result, TermComparators.byReverseWeightOrder());
            return result;
        }

    }

    private int leftMostIndex(Term prefixTerm, int left, int right) {
        while (left <= right) {
            int mid = (left + right) / 2;
            int length = prefixTerm.query().length();
            if ((mid == 0 || compare(terms[mid-1], prefixTerm, length) < 0 &&
                compare(terms[mid], prefixTerm, length) == 0)) {
                return mid;
            } else if (compare(terms[mid], prefixTerm, length) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    private int rightMostIndex(Term prefixTerm, int left, int right) {
        while (left <= right) {
            int mid = (left + right) / 2;
            int length = prefixTerm.query().length();
            if ((mid == terms.length - 1 || compare(terms[mid+1], prefixTerm, length) > 0) &&
                 compare(terms[mid], prefixTerm, length) == 0) {
                return mid;
            } else if (compare(terms[mid], prefixTerm, length) > 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    private int compare(Term current, Term target, int length) {
        return current.compareToByPrefixOrder(target, length);
    }
}
