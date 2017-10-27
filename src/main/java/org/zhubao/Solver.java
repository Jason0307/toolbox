package org.zhubao;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class Solver extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 7900649684500665951L;
    private int[] list;
    public int result;

    public Solver(int[] array) {
        this.list = array;
    }

    @Override
    protected Integer compute() {
        if (list.length == 1) {
            result = list[0];
        } else {
            int midpoint = list.length / 2;
            int[] l1 = Arrays.copyOfRange(list, 0, midpoint);
            int[] l2 = Arrays.copyOfRange(list, midpoint, list.length);
            Solver s1 = new Solver(l1);
            Solver s2 = new Solver(l2);
            s1.fork();
            s2.fork();
            int s1Result = s1.join();
            int s2Result = s2.join();
            result = s1Result + s2Result;
        }
        return result;
    }
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Solver solver = new Solver(new int[]{5, 4, 8, 9, 24, 43});
        Future<Integer> result = forkJoinPool.submit(solver);
        System.out.println(result.get());
        
    }

}
