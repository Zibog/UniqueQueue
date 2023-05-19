package org.example.collections;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UniqueQueueMultithreadingTest {
    @Test
    public void testAdd() throws InterruptedException {
        Queue<String> queue = new UniqueQueue<>();

        int taskCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(taskCount);

        for (int i = 0; i < taskCount; i++) {
            int finalI = i;
            executorService.execute(() -> queue.add("hi" + finalI));
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        Assertions.assertEquals(1000, queue.size());

        executorService = Executors.newFixedThreadPool(taskCount);

        for (int i = 0; i < taskCount; i++) {
            int finalI = i;
            executorService.execute(() -> Assertions.assertFalse(queue.add("hi" + finalI)));
        }

        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        Assertions.assertEquals(1000, queue.size());
    }
}
