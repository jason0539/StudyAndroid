package com.lzh.demo.plugin.instrumentation;

import android.app.Activity;
import android.app.Instrumentation;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/7.
 * Instrumentation负责应用所有与系统的交互，ActivityThread中有一个，每个Activity中有一个
 * 方式1 hook的是Activity中的，所以要用activity的startActivity启动才有效
 * 方式2 hook的是ActivityThread，要通过ApplicationContext(ContextImpl)的startActivity有效
 */
public class InstrumentationHookHelper {
    public static final String TAG = InstrumentationHookHelper.class.getSimpleName();

    /**
     * 方式1
     * hook当前activity的mInstrumentation，仅对当前activity有效
     * 使用activity的startActivity即可测试效果
     */
    public static void hookActivityInstrumentation(Activity activity) {
        MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityInstrumentation ");
        try {
//        Class<?> activityClass = activity.getClass();
            Class<?> activityClass = Class.forName("android.app.Activity");
            Field mInstrumentationField = activityClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
            Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
            mInstrumentationField.set(activity, evilInstrumentation);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityInstrumentation " + e.toString());
        }
    }

    private static boolean activityThreadInstrumentationHooked = false;

    /**
     * 方式2
     * hook当前应用的activityThread的mInstrumentation，
     * 对applicationContext的startActivity有效，因为最终走到了ActivityThread的mInstrumentation
     * 对activity中的直接startActivity无效，因为Activity使用自己的mInstrumentation
     * 使用getApplicationContext.startActivity即可测试效果
     */
    public static void hookActivityThreadInstrumentation() {
        MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityThreadInstrumentation ");
        if (activityThreadInstrumentationHooked) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityThreadInstrumentation activityThread的Instrumentation已经hook，返回");
            return;
        }
        activityThreadInstrumentationHooked = true;
        try {
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
        } catch (Exception e) {
            activityThreadInstrumentationHooked = false;
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityThreadInstrumentation " + e.toString());
        }
    }
}
