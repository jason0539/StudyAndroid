package com.jason.common.utils;

import android.app.Activity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by liuzhenhui on 2016/10/27.
 */
public class SoftInputUtils {
    public static final String TAG = SoftInputUtils.class.getSimpleName();

    public void hideSoftInput(Activity activity) {
        InputMethodManager manager = ((InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
