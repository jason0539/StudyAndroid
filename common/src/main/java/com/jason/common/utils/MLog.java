package com.jason.common.utils;

import android.util.Log;

/**
 * Created by liuzhenhui on 2016/10/28.
 */
public class MLog {
    public static final String TAG = MLog.class.getSimpleName();
    private static boolean DEBUG = true;

    public static final String TAG_TEST_RXJAVA = "TAG_TEST_RXJAVA";
    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";
    public static final String TAG_REALM = "TAG_REALM";
    public static final String TAG_MVP = "TAG_MVP";
    public static final String TAG_JSON = "TAG_JSON";
    public static final String TAG_SPAN = "TAG_SPAN";
    public static final String TAG_SCROLL = "TAG_SCROLL";
    public static final String TAG_LOGIN = "TAG_LOGIN";
    public static final String TAG_HTTP = "TAG_HTTP";
    public static final String TAG_FILE = "TAG_FILE";
    public static final String TAG_WEBVIEW = "TAG_WEBVIEW";
    public static final String TAG_INDEXABLELV = "TAG_INDEXABLELV";

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
