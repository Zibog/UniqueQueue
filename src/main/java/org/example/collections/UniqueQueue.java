package org.example.collections;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Queue implementation based on LinkedHashSet. Contains only unique elements. Thead-unsafe
 */
public class UniqueQueue<T> extends AbstractQueue<T> {
    private final Set<T> set = new LinkedHashSet<>();

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean add(T t) {
        return set.add(t);
    }

    @Override
    public boolean offer(T t) {
        return this.add(t);
    }

    @Override
    public T remove() throws NoSuchElementException {
        Iterator<T> i = set.iterator();
        T next = i.next();
        i.remove();
        return next;
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        return this.remove();
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return set.iterator().next();
    }
}
