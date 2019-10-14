package autocomplete;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TermTest {
    @Test
    public void testSimpleCompareTo() {
        Term a = new Term("autocomplete", 0);
        Term b = new Term("me", 0);
        assertTrue(a.compareTo(b) < 0); // "autocomplete" < "me"
    }

    // Write more unit tests below.
    @Test
    public void testCompareToByReverseWeightOrder() {
        Term[] a = {
                new Term("a", 0),
                new Term("b", 1),
                new Term("c", 2),
                new Term("d", 3),
                new Term("e", 4),
                new Term("f", 5),
                new Term("g", 5)
        };
        assertTrue(a[0].compareToByReverseWeightOrder(a[1]) > 0);
        assertTrue(a[5].compareToByReverseWeightOrder(a[6]) == 0);
        assertTrue(a[4].compareToByReverseWeightOrder(a[3]) < 0);
    }

    @Test
    public void testCompareToByPrefixOrder() {
        Term[] a = {
                new Term("abcde", 0),
                new Term("bcdefgh", 1),
                new Term("bcdeeee", 2),
                new Term("bcdfghh", 3),
                new Term("bcabcde", 4),
                new Term("fasdf", 5),
                new Term("gqwer", 5)
        };
        assertTrue(a[2].compareToByPrefixOrder(a[1], 4) == 0);
        assertTrue(a[2].compareToByPrefixOrder(a[4], 4) > 0);
        assertTrue(a[1].compareToByPrefixOrder(a[3], 4) < 0);
    }

    @Test
    public void testQueryPrefix() {
        Term[] a = {
                new Term("abcdefgh", 0),
                new Term("b", 1),
        };
        assertTrue(a[0].queryPrefix(3).equals("abc"));
        assertTrue(a[1].queryPrefix(4).equals("b"));
    }
}
