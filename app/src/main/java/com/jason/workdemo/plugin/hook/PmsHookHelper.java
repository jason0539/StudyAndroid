package com.jason.workdemo.plugin.hook;

import com.jason.common.utils.MLog;

import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class PmsHookHelper {
    public static final String TAG = PmsHookHelper.class.getSimpleName();

    public static final boolean invoke(Method method, Object[] args) {
        boolean handled = false;

        if ("getInstalledApplications".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, "PmsHookHelper->" + "invoke getInstalledApplications");
        }

        return handled;
    }
}
