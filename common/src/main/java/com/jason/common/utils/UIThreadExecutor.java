package com.jason.common.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by liuzhenhui on 2016/10/29.
 */
public class UIThreadExecutor implements Executor {
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public static UIThreadExecutor SINGLETON = new UIThreadExecutor();

    @Override
    public void execute(Runnable command) {
        mHandler.post(command);
    }

    public void remove(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }
}