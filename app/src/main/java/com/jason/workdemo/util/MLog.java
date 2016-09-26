package com.jason.workdemo.util;

import com.jason.workdemo.BuildConfig;

/**
 * liuzhenhui 16/6/4.上午10:07
 */
public class MLog {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String TAG, String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, msg);
        }
    }
}
