package deques;

public class LinkedDeque<T> implements Deque<T> {
    private int size;
    private Node front;
    private Node end;

    public LinkedDeque() {
        front = new Node(null);
        end = new Node(null);
        front.next = end;
        end.prev = front;
        size = 0;
    }

    private class Node {
        private T value;
        private Node prev;
        private Node next;

        Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }
    }

    public void addFirst(T item) {
        Node newFirst = new Node(item);
        if (size == 0) {
            front.next = newFirst;
            end.prev = newFirst;
            newFirst.next = end;
            newFirst.prev = front;
        } else {
            Node originalFirst = this.front.next;
            this.front.next = newFirst;
            originalFirst.prev = newFirst;
            newFirst.next = originalFirst;
            newFirst.prev = front;
        }

        size += 1;
    }

    public void addLast(T item) {
        Node newLast = new Node(item);
        if (size == 0) {
            front.next = newLast;
            end.prev = newLast;
            newLast.next = end;
            newLast.prev = front;
        } else {
            Node originalLast = this.end.prev;
            this.end.prev = newLast;
            originalLast.next = newLast;
            newLast.next = end;
            newLast.prev = originalLast;
        }
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node first = front.next;
        front.next = first.next;
        first.next.prev = front;
        size -= 1;

        return first.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node last = end.prev;
        end.prev = last.prev;
        last.prev.next = end;
        size -= 1;

        return last.value;
    }

    public T get(int index) {
        if ((index > size) || (index < 0)) {
            return null;
        }
        if (front == null) {
            return null;
        }
        Node current = front;
        for (int i = 0; i <= index; i++) {
            current = current.next;
        }

        return current.value;
    }

    public int size() {
        return size;
    }
}
