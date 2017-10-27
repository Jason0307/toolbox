package org.zhubao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CounterSynchronized {

    private int sum = 1;

    public synchronized void count() {
        sum = sum + 1;
    }

    public int getCount() {
        return sum;
    }

    public static void stop(ExecutorService executor) {
        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

    public static void main(String[] args) {
        final CounterSynchronized counter = new CounterSynchronized();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(1, 10000).forEach(i -> {
            executorService.submit(() -> {
                counter.count();
            });
        });

        stop(executorService);
        System.out.println("Total Count : " + counter.getCount());

    }
}
