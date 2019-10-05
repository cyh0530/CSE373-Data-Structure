package deques;

import org.junit.Test;

import static org.junit.Assert.*;

/** Performs some basic linked deque tests. */
public class LinkedDequeTest {

    /** Adds a few strings to the deque, checking isEmpty() and size() are correct. */
    @Test
    public void addIsEmptySizeTest() {
        LinkedDeque<String> lld = new LinkedDeque<>();
        assertTrue(lld.isEmpty());

        lld.addFirst("front");
        assertEquals(1, lld.size());
        assertFalse(lld.isEmpty());

        lld.addLast("middle");
        assertEquals(2, lld.size());

        lld.addLast("back");
        assertEquals(3, lld.size());
    }

    /** Adds an item, then removes an item, and ensures that the deque is empty afterwards. */
    @Test
    public void addRemoveTest() {
        LinkedDeque<Integer> lld = new LinkedDeque<>();
        assertTrue(lld.isEmpty());

        lld.addFirst(10);
        assertFalse(lld.isEmpty());

        lld.removeFirst();
        assertTrue(lld.isEmpty());
    }

    @Test
    public void addFirstTest() {
        LinkedDeque<Integer> link = new LinkedDeque<>();
        // test initialized isEmpty
        assertTrue(link.isEmpty());
        link.addFirst(0);
        // test first item get
        assertEquals(0, (int) link.get(0));

        link.addFirst(-1);
        link.addFirst(-2);
        link.addFirst(-3);

        // testing get
        assertEquals(-2, (int) link.get(1));

        // test if deque is not empty
        assertFalse(link.isEmpty());

        // test multiple remove
        assertEquals(-3, (int) link.removeFirst());
        assertEquals(0, (int) link.removeLast());
        assertEquals(-2, (int) link.removeFirst());
        assertEquals(-1, (int) link.removeLast());

        // at now, the deque should be empty
        assertTrue(link.isEmpty());

        assertNull(link.removeLast());

        link.addLast(0);
        assertEquals(0, (int) link.get(0));
    }
}
