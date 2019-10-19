package heap;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private List<PriorityNode> priorityHeap;
    private Map<T, Integer> indexMap;

    public ArrayHeapMinPQ() {
        priorityHeap = new ArrayList<>();
        indexMap = new HashMap<>();
        // empty at index 0
        priorityHeap.add(new PriorityNode(null, 0));

    }

    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (this.contains(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode newNode = new PriorityNode(item, priority);
        priorityHeap.add(newNode);

        indexMap.put(item, size());
        swim(size());
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    /**
     * Returns the item with the smallest priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return priorityHeap.get(1).getItem();
    }

    /**
     * Removes and returns the item with the smallest priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        swap(1, size());
        T smallest = priorityHeap.remove(size()).getItem();
        indexMap.remove(smallest);
        sink(1);
        return smallest;
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int index = indexMap.get(item);
        priorityHeap.get(index).priority = priority;
        if (priority < priorityHeap.get(index / 2).getPriority()) {
            swim(index);
        } else {
            sink(index);
        }
    }

    /**
     * Returns the number of priorityHeap in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return priorityHeap.size() - 1;
    }


    private void swim(int index) {
        while (index > 1 && smaller(index, index / 2)) {
            swap(index, index / 2);
            index /= 2;
        }
    }

    private void sink(int index) {
        while (index * 2 <= size()) {
            int child = index * 2;
            if (child < size() && smaller(child + 1, child)) { // have both child node
                child++;
            }
            if (smaller(child, index)) {
                swap(child, index);
                index = child;
            } else {
                break;
            }
        }
    }

    private boolean smaller(int i, int j) {
        return priorityHeap.get(i).getPriority() < priorityHeap.get(j).getPriority();
    }

    /**
     * A helper method for swapping the priorityHeap at two indices of the array heap.
     */
    private void swap(int a, int b) {
        indexMap.put(priorityHeap.get(a).getItem(), b);
        indexMap.put(priorityHeap.get(b).getItem(), a);

        PriorityNode temp = priorityHeap.get(a);
        priorityHeap.set(a, priorityHeap.get(b));
        priorityHeap.set(b, temp);

    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((PriorityNode) o).getItem().equals(getItem());
            }
        }

        @Override
        public int hashCode() {
            return item.hashCode();
        }
    }
}
