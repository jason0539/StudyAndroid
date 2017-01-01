package com.jason.common.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by liuzhenhui on 2017/1/1.
 */
public class LayoutUtils {
    public static final String TAG = LayoutUtils.class.getSimpleName();

    public static final LinearLayout getVerticalLinearLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    public static final ViewGroup.LayoutParams getLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        return layoutParams;
    }
}
