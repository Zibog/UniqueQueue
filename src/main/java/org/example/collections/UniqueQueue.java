package org.example.collections;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Queue implementation based on LinkedHashSet. Contains only unique elements. Conditionally thead-safe
 */
public class UniqueQueue<T> extends AbstractQueue<T> {

    /*
    * Most of the time insertion and check for the element uniqueness takes O(1)
    * However, in case of many similar hashCodes it may take up to O(n)
    * Removing element from the head of this queue always takes O(1)
    */

    private final Set<T> set = new LinkedHashSet<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Condition notEmpty = writeLock.newCondition();

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return set.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean add(T t) {
        writeLock.lock();
        try {
            boolean added = set.add(t);
            if (added) {
                notEmpty.signal();
            }
            return added;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean offer(T t) {
        return this.add(t);
    }

    @Override
    public T remove() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.poll();
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T next;
        writeLock.lock();
        try {
            Iterator<T> i = set.iterator();
            next = i.next();
            i.remove();
        } finally {
            writeLock.unlock();
        }
        return next;
    }

    @Override
    public boolean remove(Object o) {
        writeLock.lock();
        try {
            return set.remove(o);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    public T take() throws InterruptedException {
        writeLock.lock();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }
        } finally {
            writeLock.unlock();
        }
        return this.poll();
    }

    @Override
    public boolean contains(Object o) {
        readLock.lock();
        try {
            return set.contains(o);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return set.iterator().next();
    }

    /**
     * Retrieves the head of this queue, waiting if necessary
     * until an element becomes available.
     *
     * @return the head of this queue
     * @throws InterruptedException if interrupted while waiting
     */
    public T peekWaiting() throws InterruptedException {
        writeLock.lock();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }
        } finally {
            writeLock.unlock();
        }
        return this.peek();
    }
}
