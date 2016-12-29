package com.jason.workdemo.plugin.hook;

import android.app.Activity;
import android.app.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class HookHelper {
    public static final String TAG = HookHelper.class.getSimpleName();

    public static void hookActivityInstrumentation(Activity activity) throws Exception {
//        Class<?> activityClass = activity.getClass();
        Class<?> activityClass = Class.forName("android.app.Activity");
        Field mInstrumentationField = activityClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        mInstrumentationField.set(activity, evilInstrumentation);
    }

    public static void hookActivityThreadInstrumentation() throws Exception {
        //获取当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static方法，可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        //创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }
}
