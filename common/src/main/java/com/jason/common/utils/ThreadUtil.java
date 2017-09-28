package com.jason.common.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuzhenhui on 2016/10/29.
 */
public class ThreadUtil {
    private final static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT;
    /**
     * 最大线程数量
     */
    private static final int MAXMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 非核心线程闲置时间为1秒
     */
    private static final int KEEP_TIME = 1;

    /**
     * 线程池任务队列
     */
    private static final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue<>();

    /**
     * 线程工厂类
     */
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread Current ID = " + mCount.getAndIncrement());
        }
    };

    //非UI线程池
    /*
    if(currThreadNum < CoreThreadNum){
        new Thread();
        currThreadNum++;
    }else{
        if(workQueue.isFull()){
            if(currThreadNum < MaxThreadNum){
                new Thread();
                currThreadNum++;
            }else{
                throw Exception();
            }
        }else{
            workQueue.addTask();
        }
    }
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXMUM_POOL_SIZE, KEEP_TIME, TimeUnit.SECONDS, mPoolWorkQueue, mThreadFactory);

    //UI线程
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    /**
     * 执行一个非UI线程的任务
     */
    public static final void execute(final Runnable runnable) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MLog.d(MLog.TAG_THREAD, "UtilThread->run 线程：" + Thread.currentThread().getName());
                runnable.run();
            }
        });
    }

    public static final Future submit(final Callable runnable) {
        return executorService.submit(runnable);
    }

    /**
     * 执行一个UI线程任务
     */
    public static final void runOnUiThread(Runnable runnable) {
        mUiHandler.post(runnable);
    }
}