package org.zhubao.executor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;

public class TaskQueue extends LinkedBlockingDeque<Runnable> {
    
    private static final long serialVersionUID = -5730442599673601074L;
    
    private EnhancedThreadPoolExecutor executor;

    
    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(EnhancedThreadPoolExecutor executor) {
        this.executor = executor;
    }
    
    public boolean forceTaskIntoQueue(Runnable e) {
        if(executor.isShutdown()) {
            throw new RejectedExecutionException("The Executor is shutdown, can't add task in the queue.");
        }
        
        return super.offer(e);
    }

    @Override
    public boolean offer(Runnable e) {
        int currentThreadSize = executor.getPoolSize();
        
        if(currentThreadSize == executor.getMaximumPoolSize()) {
            return super.offer(e);
        }
        
        if(currentThreadSize > executor.getSubmittedThreadCount()) {
            return super.offer(e);
        }
        
        if(currentThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }
        
        return super.offer(e);
    }
    
    

}
