package com.jason.workdemo.plugin.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;

import com.jason.common.utils.MLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/6.
 * http://weishu.me/2016/03/21/understand-plugin-framework-activity-management/
 */
public class AmsHookHelper {
    public static final String TAG = AmsHookHelper.class.getSimpleName();

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static boolean invoke(Method method, Object[] args) {
        boolean handle = false;
        if ("startActivity".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK,"AmsHookHelper->"+"invoke startActivity");
            handle = true;
            // 只拦截这个方法
            // 替换参数, 任你所为;甚至替换原始Activity启动别的Activity偷梁换柱
            // API 23:
            // public final Activity startActivityNow(Activity parent, String id,
            // Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
            // Activity.NonConfigurationInstances lastNonConfigurationInstances) {

            // 找到参数里面的第一个Intent 对象
            Intent raw;
            int index = 0;

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            raw = (Intent) args[index];

            if (raw.getComponent() != null) {
                MLog.d(MLog.TAG_HOOK,"AmsHookHelper->"+"invoke 启动实名Activity，替换其中的Component");
                Intent fakeIntent = new Intent();
                // 这里包名直接写死,如果再插件里,不同的插件有不同的包  传递插件的包名即可
                String targetPackage = "com.jason.workdemo";
                ComponentName componentName = new ComponentName(targetPackage,StubActivity.class.getCanonicalName());
                fakeIntent.setComponent(componentName);
                fakeIntent.putExtra(EXTRA_TARGET_INTENT,raw);

                args[index] = fakeIntent;
            }else {
                MLog.d(MLog.TAG_HOOK,"AmsHookHelper->"+"invoke 非实名启动activity，不需要替换");
            }
        }
        return handle;
    }

    public static final void hookActivityThreadHandlerCallback(){
        try {
            // 先获取到当前的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            // 由于ActivityThread一个进程只有一个,我们获取这个对象的mH
            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH= (Handler) mHField.get(sCurrentActivityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH,new ActivityThreadHandlerCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
