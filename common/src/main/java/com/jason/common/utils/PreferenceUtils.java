package com.jason.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by liuzhenhui on 2016/11/18.
 */
public class PreferenceUtils {
    public static final String TAG = PreferenceUtils.class.getSimpleName();

    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;

    public static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
    }

    public static void putString(String key, String value) {
        spEditor.putString(key, value);
        spEditor.apply();
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        spEditor.putInt(key, value);
        spEditor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        spEditor.putLong(key, value);
        spEditor.apply();
    }

    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

}
