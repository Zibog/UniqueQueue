package org.example.collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class UniqueQueueTest {
    @Test
    public void testUniqueQueue() {
        Queue<String> stringUniqueQueue = new UniqueQueue<>();
        Assertions.assertEquals(0, stringUniqueQueue.size());

        Assertions.assertTrue(stringUniqueQueue.add("cool"));
        Assertions.assertTrue(stringUniqueQueue.add("fun"));
        Assertions.assertTrue(stringUniqueQueue.add("smile"));
        Assertions.assertFalse(stringUniqueQueue.add("cool"));
        Assertions.assertTrue(stringUniqueQueue.add("1"));

        Assertions.assertEquals(4, stringUniqueQueue.size());
        Assertions.assertEquals("cool", stringUniqueQueue.poll());

        stringUniqueQueue.addAll(Arrays.asList("cool", "fun", "sun", "2"));

        Assertions.assertEquals(6, stringUniqueQueue.size());
        Assertions.assertEquals("fun", stringUniqueQueue.peek());

        Assertions.assertEquals("fun", stringUniqueQueue.poll());
        Assertions.assertEquals("smile", stringUniqueQueue.poll());
        Assertions.assertEquals("1", stringUniqueQueue.poll());
        Assertions.assertEquals("cool", stringUniqueQueue.poll());
        Assertions.assertEquals("sun", stringUniqueQueue.poll());
        Assertions.assertEquals("2", stringUniqueQueue.poll());

        Assertions.assertNull(stringUniqueQueue.poll());
        Assertions.assertEquals(0, stringUniqueQueue.size());
    }

    @Test
    public void testOfferThenPoll() {
        Queue<String> queue = new UniqueQueue<>();
        List<String> dataSet = Arrays.asList("1", "3", "6", "10", "4");

        dataSet.forEach(queue::offer);

        for (String s : dataSet) {
            Assertions.assertEquals(s, queue.poll());
        }
        Assertions.assertTrue(queue.isEmpty());
    }

    @Test
    public void testOfferDuplicates() {
        Queue<Integer> queue = new UniqueQueue<>();

        Assertions.assertTrue(queue.offer(1));
        Assertions.assertFalse(queue.offer(1));
    }

    @Test
    public void testPeek() {
        final Queue<Integer> queue = new UniqueQueue<>();

        Assertions.assertNull(queue.peek());
        Assertions.assertTrue(queue.offer(66));
        Assertions.assertEquals(66, queue.peek().intValue());
        Assertions.assertEquals(1, queue.size());
    }

    @Test
    public void testRemove() {
        Queue<Integer> queue = new UniqueQueue<>();

        Assert.assertThrows(NoSuchElementException.class, queue::remove);
        Assertions.assertTrue(queue.offer(3));
        Assertions.assertTrue(queue.offer(1));
        Assertions.assertTrue(queue.offer(2));
        Assertions.assertEquals(3, queue.remove().intValue());
        Assertions.assertEquals(2, queue.size());
        Assertions.assertEquals(1, queue.remove().intValue());
        Assertions.assertEquals(2, queue.remove().intValue());
        Assertions.assertTrue(queue.isEmpty());
    }

    @Test
    public void testElement() {
        Queue<Integer> queue = new UniqueQueue<>();

        Assert.assertThrows(NoSuchElementException.class, queue::element);
        Assertions.assertTrue(queue.offer(4));
        Assertions.assertTrue(queue.offer(1));
        Assertions.assertEquals(4, queue.element().intValue());
        Assertions.assertEquals(2, queue.size());
    }
}