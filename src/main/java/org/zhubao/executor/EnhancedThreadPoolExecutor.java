package org.zhubao.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class EnhancedThreadPoolExecutor extends ThreadPoolExecutor {

    private AtomicInteger submittedThreadCount = new AtomicInteger(0);

    public EnhancedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            TaskQueue workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        workQueue.setExecutor(this);
    }

    public int getSubmittedThreadCount() {
        return submittedThreadCount.get();
    }

    @Override
    public void execute(Runnable command) {
        try {
            submittedThreadCount.incrementAndGet();
            super.execute(command);
        } catch (RejectedExecutionException e) {
            BlockingQueue<Runnable> taskQUeue = super.getQueue();

            if (taskQUeue instanceof TaskQueue) {
                TaskQueue queue = (TaskQueue) taskQUeue;

                if (!queue.forceTaskIntoQueue(command)) {
                    submittedThreadCount.decrementAndGet();
                    throw new RejectedExecutionException("The queue is full.");
                }
            } else {
                submittedThreadCount.decrementAndGet();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        submittedThreadCount.decrementAndGet();
    }

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new EnhancedThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new TaskQueue(10));

        IntStream.range(1, 16).forEach(i -> {
            executor.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Total Threads: " + executor.getPoolSize() + ", Queue Size: " + executor.getQueue().size());
        });
    }
}
