package deques;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void testTricky() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(0);
        assertEquals(0, (int) deque.get(0));

        deque.addLast(1);
        assertEquals(1, (int) deque.get(1));

        deque.addFirst(-1);
        deque.addLast(2);
        assertEquals(2, (int) deque.get(3));

        deque.addLast(3);
        deque.addLast(4);

        // Test that removing and adding back is okay
        assertEquals(-1, (int) deque.removeFirst());
        deque.addFirst(-1);
        assertEquals(-1, (int) deque.get(0));

        deque.addLast(5);
        deque.addFirst(-2);
        deque.addFirst(-3);

        // Test a tricky sequence of removes
        assertEquals(-3, (int) deque.removeFirst());
        assertEquals(5, (int) deque.removeLast());
        assertEquals(4, (int) deque.removeLast());
        assertEquals(3, (int) deque.removeLast());
        assertEquals(2, (int) deque.removeLast());
        // Failing test
        assertEquals(1, (int) deque.removeLast());
    }

    @Test
    public void test() {
        ArrayDeque<Integer> d = new ArrayDeque<>();
        d.addFirst(0);
        d.addLast(1);
        d.addFirst(2);
        d.removeFirst();
        d.addFirst(4);
        d.addLast(5);
        d.addLast(6);
        d.addFirst(7);
        d.removeLast();
        d.addLast(9);
        d.addLast(10);
        d.addFirst(11);
        d.addFirst(12);
        d.removeFirst()     ;//==> 12
        d.removeLast()      ;//==> 10
        d.get(6)      ;//==> 9
        d.removeFirst()  ;//   ==> 11
        d.removeLast()      ;//==> 9
        d.removeLast()     ;// ==> 5
        d.get(2)     ;// ==> 0
        d.removeLast() ;//     ==> 1
        d.removeLast();
        System.out.println();
    }
}
