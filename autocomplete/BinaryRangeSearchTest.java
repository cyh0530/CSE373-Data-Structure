package autocomplete;

import edu.princeton.cs.algs4.In;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryRangeSearchTest {

    private static Autocomplete linearAuto;
    private static Autocomplete binaryAuto;

    private static int N = 0;
    private static Term[] terms = null;

    private static final String INPUT_FILENAME = "data/cities.txt";

    /**
     * Creates LinearRangeSearch and BinaryRangeSearch instances based on the data from cities.txt
     * so that they can easily be used in tests.
     */
    @Before
    public void setUp() {
        if (terms != null) {
            return;
        }

        In in = new In(INPUT_FILENAME);
        N = in.readInt();
        terms = new Term[N];
        for (int i = 0; i < N; i += 1) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query, weight);
        }

        linearAuto = new LinearRangeSearch(terms);
        binaryAuto = new BinaryRangeSearch(terms);
    }

    /**
     * Checks that the terms in the expected array are equivalent to the ones in the actual array.
     */
    private void assertTermsEqual(Term[] expected, Term[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Term e = expected[i];
            Term a = actual[i];
            assertEquals(e.query(), a.query());
            assertEquals(e.weight(), a.weight());
        }
    }

    @Test
    public void testSimpleExample() {
        Term[] moreTerms = new Term[] {
            new Term("hello", 0),
            new Term("world", 0),
            new Term("welcome", 0),
            new Term("to", 0),
            new Term("autocomplete", 0),
            new Term("meme", 10),
            new Term("meadsf", 4),
            new Term("meavacs", 8),
            new Term("me", 0)
        };


        BinaryRangeSearch brs = new BinaryRangeSearch(moreTerms);
        Term[] t = brs.allMatches("auto");
        Term[] expected = new Term[]{new Term("autocomplete", 0)};
        assertTermsEqual(expected, t);

    }

    // Write more unit tests below.
    @Test
    public void testCities() {
        Term[] termsMatch = binaryAuto.allMatches("fremont");
        for (Term term: termsMatch) {
            System.out.println(term.query());

        }
    }
}
