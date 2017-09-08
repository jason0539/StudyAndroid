package com.jason.common.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhenhui on 2016/10/29.
 */
public class ThreadUtil {
    private final static int NUM_OF_CORES = Runtime.getRuntime().availableProcessors();

    //非UI线程池
    private static ExecutorService executorService =
            new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    //UI线程
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    /**
     * 执行一个非UI线程的任务
     */
    public static final void execute(final Runnable runnable) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MLog.d(MLog.TAG_JSON,"UtilThread->run 线程：" + Thread.currentThread().getName());
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