package heap;

import org.junit.Test;

import java.util.Random;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    /* Be sure to write randomized tests that can handle millions of items. To
     * test for runtime, compare the runtime of NaiveMinPQ vs ArrayHeapMinPQ on
     * a large input of millions of items. */

    @Test (expected = IllegalArgumentException.class)
    public void testAdd1() {
        ArrayHeapMinPQ<Integer> actual = new ArrayHeapMinPQ<>();
        actual.add(1, 1);
        actual.add(1, 2);
    }
    @Test (expected = NoSuchElementException.class)
    public void testRemoveSmallest1() {
        ArrayHeapMinPQ<Integer> actual = new ArrayHeapMinPQ<>();
        actual.removeSmallest();
    }

    @Test (expected = NoSuchElementException.class)
    public void testGetSmallest1() {
        ArrayHeapMinPQ<Integer> actual = new ArrayHeapMinPQ<>();
        actual.getSmallest();
    }

    @Test
    public void testAddAndRemove() {
        int seed = 999;
        int num = 100000;
        int max = 1000000;
        Random r = new Random(seed);
        ArrayHeapMinPQ<Integer> testing = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> expected = new NaiveMinPQ<>();
        Set<Integer> prioritySet = new HashSet<>();
        assertEquals(expected.size(), testing.size());

        for (int i = 0; i < num; i++) {
            int item = r.nextInt(max);
            int priority = r.nextInt(max);
            while (testing.contains(item) || prioritySet.contains(priority)) {
                item = r.nextInt(max);
                priority = r.nextInt(max);
            }

            testing.add(item, priority);
            expected.add(item, priority);
            prioritySet.add(priority);
        }

        for (int i = 0; i < num; i++) {
            int actual = testing.removeSmallest();
            int expect = expected.removeSmallest();
            assertEquals("Fail on iteration " + i, expect, actual);
        }
    }

    @Test
    public void testRandomOperation() {
        int seed = 999;
        int iteration = 1000000;
        int max = 10000000;
        Random r = new Random(seed);
        ArrayHeapMinPQ<Integer> testing = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> expected = new NaiveMinPQ<>();
        List<Integer> itemList = new ArrayList<>();
        Set<Integer> prioritySet = new HashSet<>();
        assertEquals(expected.size(), testing.size());

        for (int i = 0; i < iteration; i++) {
            int item = r.nextInt(max);
            int priority = r.nextInt(max);
            while (testing.contains(item) || prioritySet.contains(priority)) {
                item = r.nextInt(max);
                priority = r.nextInt(max);
            }
            // add remove size contains changePriority
            int op = r.nextInt(5);
            // check if have items to remove
            if (op == 0) { // add
                testing.add(item, priority);
                expected.add(item, priority);
                itemList.add(item);
                prioritySet.add(priority);
            } else if (op == 1) { // remove
                if (testing.size() == 0) {
                    i -= 1;
                    continue;
                }
                int actual = testing.removeSmallest();
                int expect = expected.removeSmallest();
                itemList.remove((Integer) actual);
                assertEquals("removeSmallest fail on iteration " + i, expect, actual);
            } else if (op == 2) { // size
                int actual = testing.size();
                int expect = expected.size();
                assertEquals("size fail on iteration " + i, expect, actual);
            } else if (op == 3) { // contains
                int target = r.nextInt(max);
                boolean actual = testing.contains(target);
                boolean expect = expected.contains(target);
                assertEquals("contains fail on iteration " + i, actual, expect);
            } else if (op == 4) { // change priority
                int newPriority = r.nextInt(max);
                if (itemList.size() <= 1) {
                    i -= 1;
                    continue;
                }
                int index = r.nextInt(itemList.size()-1) + 1;
                int target = itemList.get(index);
                while (prioritySet.contains(newPriority)) {
                    newPriority = r.nextInt(max);
                }
                testing.changePriority(target, newPriority);
                expected.changePriority(target, newPriority);
            }


        }
    }

}
