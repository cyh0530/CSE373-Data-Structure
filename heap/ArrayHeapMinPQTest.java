package heap;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ArrayHeapMinPQTest {
    /* Be sure to write randomized tests that can handle millions of items. To
     * test for runtime, compare the runtime of NaiveMinPQ vs ArrayHeapMinPQ on
     * a large input of millions of items. */

    @Test (expected = IllegalArgumentException.class)
    public void testAdd1() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        heap.add(1, 1);
        heap.add(1, 2);
    }
    @Test (expected = NoSuchElementException.class)
    public void testRemoveSmallest1() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        heap.removeSmallest();
    }

    @Test (expected = NoSuchElementException.class)
    public void testGetSmallest1() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        heap.getSmallest();
    }

    @Test
    public void testAdd() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        assertEquals(0, heap.size());

        heap.add(50, 50);

        // add smaller -> become 30, 50
        heap.add(30, 30);
        assertEquals(30, (long) heap.getSmallest());

        // add larger -> become 30, 50, 70
        heap.add(70, 70);

        // add middle -> 30, 40, 70, 50
        heap.add(40, 40);

        heap.add(35, 35);
        heap.add(60, 60);
        heap.add(55, 55);
        heap.add(15, 15);
        // become 15 30 55 35 40 70 60 50
        assertEquals(15, (long) heap.getSmallest());
        assertTrue(heap.contains(55));
        assertEquals(8, heap.size());
    }

    @Test (expected = NoSuchElementException.class)
    public void testRemoveSmallest2() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();

        heap.add(50, 50);
        heap.add(30, 30);
        heap.add(70, 70);
        heap.add(40, 40);
        heap.add(35, 35);
        heap.add(60, 60);
        heap.add(55, 55);
        heap.add(15, 15);
        heap.add(20, 20);
        // become 15 20 55 30 40 70 60 50 35
        assertTrue(heap.contains(15));
        assertEquals(15, (long) heap.removeSmallest());
        assertFalse(heap.contains(15));
        assertEquals(20, (long) heap.removeSmallest());
        assertEquals(30, (long) heap.removeSmallest());
        assertEquals(35, (long) heap.removeSmallest());
        assertEquals(40, (long) heap.removeSmallest());
        assertEquals(50, (long) heap.removeSmallest());
        assertEquals(55, (long) heap.removeSmallest());
        assertEquals(60, (long) heap.removeSmallest());
        assertEquals(70, (long) heap.removeSmallest());
        heap.removeSmallest(); // no such element exception
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();

        heap.add(100, 100);
        heap.add(30, 30);
        heap.add(50, 50);
        heap.add(80, 80);
        heap.add(70, 70);
        heap.add(20, 20);
        heap.add(40, 40);
        heap.add(15, 15);
        heap.add(10, 10);
        // 10 15 30 20 80 50 40 100 70

        // change priority to smaller than parent's
        heap.changePriority(20, 13);
        heap.changePriority(50, 8);
        assertEquals(50, (long) heap.getSmallest());
        heap.changePriority(10, 5);
        assertEquals(10, (long) heap.getSmallest());
        // 10 20 50 15 80 30 40 100 70

        // change priority but maintain same position
        heap.changePriority(15, 60);
        heap.changePriority(30, 35);
        // 10 20 50 15 80 30 40 100 70

        // change larger larger than children
        heap.changePriority(15, 150);
        heap.changePriority(10, 38);
        // 50 20 30 70 80 10 40 100 15

        assertEquals(50, (long) heap.removeSmallest());
        assertEquals(20, (long) heap.removeSmallest());
        assertEquals(30, (long) heap.removeSmallest());
        assertEquals(10, (long) heap.removeSmallest());
        assertEquals(40, (long) heap.removeSmallest());
        assertEquals(70, (long) heap.removeSmallest());
        assertEquals(80, (long) heap.removeSmallest());
        assertEquals(100, (long) heap.removeSmallest());
        assertEquals(15, (long) heap.removeSmallest());

    }


}
