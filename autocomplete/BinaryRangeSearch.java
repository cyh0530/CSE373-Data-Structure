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
        mergeSort(terms);
        this.terms = terms;
    }

    private void mergeSort(Term[] terms) {
        if (terms.length > 1) {
            Term[] left = Arrays.copyOfRange(terms, 0, terms.length / 2);
            Term[] right = Arrays.copyOfRange(terms, terms.length / 2, terms.length);

            mergeSort(left);
            mergeSort(right);

            merge(terms, left, right);
        }
    }

    private void merge(Term[] terms, Term[] left, Term[] right) {
        int i1 = 0;
        int i2 = 0;
        for (int i = 0; i < terms.length; i++) {
            if (i2 >= right.length || (i1 < left.length && left[i1].compareTo(right[i2]) <= 0)) {
                terms[i] = left[i1];
                i1++;
            } else {
                terms[i] = right[i2];
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

    private  int rightMostIndex(Term prefixTerm, int left, int right) {
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
