package com.jason.common.utils;

import android.content.Context;

/**
 * Created by liuzhenhui on 2016/10/31.
 */
public class ScreenUtils {
    private static final String TAG = ScreenUtils.class.getSimpleName();

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int) (dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 获取手机Dpi
    private static int mDensityDpi = 0;

    public static int getDensityDpi(Context context) {
        if (mDensityDpi == 0 && context != null) {
            mDensityDpi = context.getResources().getDisplayMetrics().densityDpi;
        }
        return mDensityDpi;
    }
}
