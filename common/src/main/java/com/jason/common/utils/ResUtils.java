package com.jason.common.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by liuzhenhui on 2016/11/15.
 */
public class ResUtils {
    public static final String TAG = ResUtils.class.getSimpleName();
    public static Resources mResource;

    public static final void init(Context context) {
        mResource = context.getResources();
    }

    public static String getString(int id) {
        return mResource.getString(id);
    }
}
