package com.lzh.demo.plugin.ams;

import android.os.Handler;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by liuzhenhui on 2017/1/6.
 * http://weishu.me/2016/03/21/understand-plugin-framework-activity-management/
 */
public class AmsHookHelper {
    public static final String TAG = AmsHookHelper.class.getSimpleName();

    private static boolean amsHooked = false;

    /**
     * hook当前应用的AMS
     * http://weishu.me/2016/03/07/understand-plugin-framework-ams-pms-hook/
     */
    public static void hookActivityManagerNative() {
        if (amsHooked) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityManagerNative ams已经hook，返回");
            return;
        }
        amsHooked = true;
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            // 获取 gDefault 这个字段, 想办法替换它
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
            Object rawIActivityManager = mInstanceField.get(gDefault);

            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
//        Thread.currentThread().getContextClassLoader()
            Object proxy = Proxy.newProxyInstance(activityManagerNativeClass.getClassLoader(),
                    new Class<?>[]{iActivityManagerInterface}, new AmsHookInvocationHandler(rawIActivityManager));

            mInstanceField.set(gDefault, proxy);
        } catch (Exception e) {
            amsHooked = false;
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "hookActivityManagerNative " + e.toString());
        }
    }

    /**
     * hook启动activity后的mH的处理逻辑，替换intent达到启动未在manifest中注册的activity的效果
     */
    public static final void hookActivityThreadHandlerCallback() {
        try {
            // 先获取到当前的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            // 由于ActivityThread一个进程只有一个,我们获取这个对象的mH
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(sCurrentActivityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ActivityThreadHandlerCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
