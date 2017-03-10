package com.jcrspace.common.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangchaoren on 2017/3/10.
 */

public class QsThreadPool extends ThreadPoolExecutor {
    public static QsThreadPool newPauseThreadPool() {
        return new QsThreadPool(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
    public QsThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public QsThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public QsThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public QsThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    BlockingQueue<Runnable> pauseWorkQueue = new ArrayBlockingQueue<>(20);

    @Override
    public void execute(Runnable runnable) {
        if (paused) {
            pauseWorkQueue.add(runnable);
            return;
        }
        stackElementMap.put(runnable, new TimeOutException("execute RunInfo:" + runnable));
        super.execute(runnable);
    }

    Boolean paused = false;

    /**
     * 暂停线程池
     */
    public void pause() {
        paused = true;
        pauseWorkQueue.addAll(getQueue());
        getQueue().clear();
    }

    /**
     * 取消暂停（暂停时的任务重新开始）
     */
    public QsThreadPool cancelPause() {
        paused = false;
        Iterator<Runnable> iter = pauseWorkQueue.iterator();
        while (iter.hasNext()) {
            Runnable runnable = iter.next();
            execute(runnable);
            iter.remove();
        }
        return this;
    }

    /**
     * 清除队列中的任务
     */
    public QsThreadPool clear() {
        pauseWorkQueue.clear();
        getQueue().clear();
        return this;
    }

    Map<Runnable, TimeOutException> stackElementMap = new HashMap<>();

    @Override
    public int getActiveCount() {
        int i = 0;
        for (Map.Entry<Runnable, Thread> threadEntry : runingMap.entrySet()) {
            if (threadEntry.getValue().isAlive()) {
                i++;
                TimeOutException e = stackElementMap.get(threadEntry.getKey());
                if (e!=null&& System.currentTimeMillis() - e.startTime > 60 * 1000) {
                    try {
                        threadEntry.getValue().interrupt();
                        if (threadEntry.getValue().isAlive()) {
                            threadEntry.getValue().stop();
                            runingMap.remove(threadEntry.getKey());
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        runingMap.remove(r);
        stackElementMap.remove(r);
        super.afterExecute(r, t);
    }

    Map<Runnable, Thread> runingMap = new HashMap<>();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        runingMap.put(r, t);
        TimeOutException timeOutException = stackElementMap.get(r);
        if(timeOutException != null){
            timeOutException.setStartTime(System.currentTimeMillis());
        }
        super.beforeExecute(t, r);
    }

    static class TimeOutException extends Exception {
        long startTime;

        public TimeOutException(String detailMessage) {
            super(detailMessage);
            startTime = System.currentTimeMillis();
        }

        public TimeOutException setStartTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public long getStartTime() {
            return startTime;
        }
    }
}
