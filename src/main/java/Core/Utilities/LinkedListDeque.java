package Core.Utilities;

import java.util.*;

public class LinkedListDeque<T> implements Iterable<T> {
    private final Node sentinel;
    private int size;

    private class Node {
        T value;
        Node previous, next;

        Node() {
            value = null;
        }

        Node(T v, Node p, Node n) {
            this.value = v;
            this.next = n;
            this.previous = p;
            n.previous = this;
            p.next = this;
        }
    }

    public LinkedListDeque() {
        this.sentinel = new Node();
        this.sentinel.previous = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    public LinkedListDeque(Collection<? extends T> c) {
        this();
        for (T element : c) {
            this.add(element);
        }
    }

    public void add(T value) {
        new Node(value, this.sentinel, this.sentinel.next);
        this.size++;
    }

    public int size() {
        return this.size;
    }

    public T removeFirst() {
        if (this.size == 0) {
            return null;
        } else {
            Node n = this.sentinel.next;
            this.sentinel.next = n.next;
            this.sentinel.next.previous = this.sentinel;
            this.size--;
            return n.value;
        }
    }

    public T removeLast() {
        if (this.size == 0) {
            return null;
        } else {
            Node n = this.sentinel.previous;
            this.sentinel.previous = n.previous;
            this.sentinel.previous.next = this.sentinel;
            this.size--;
            return n.value;
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node n = sentinel;

            @Override
            public boolean hasNext() {
                return n.next != sentinel;
            }

            @Override
            public T next() {
                n = n.next;
                return n.value;
            }
        };
    }
}


