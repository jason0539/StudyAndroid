package com.jason.workdemo.util;

import android.util.Log;

/**
 * liuzhenhui 16/6/4.上午10:07
 */
public class MLog {
    public static final String TAG = "lzh";
    private static boolean DEBUG = true;

    public static final String TAG_FILE = "TAG_FILE";
    public static final String TAG_JSON = "TAG_JSON";
    public static final String TAG_SPAN = "TAG_SPAN";
    public static final String TAG_SCROLL = "TAG_SCROLL";

    public static final void d(String subTag, String msg) {
        if (DEBUG) {
            Log.d(TAG, subTag + "-->" + msg);
        }
    }

    public static final void e(String subTag, String msg) {
        if (DEBUG) {
            Log.e(TAG, subTag + "-->" + msg);
        }
    }

}
