package com.jason.workdemo.plugin.hook.ams;

import android.content.ComponentName;
import android.content.Intent;

import com.jason.common.utils.MLog;
import com.jason.workdemo.plugin.hook.StubActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class AmsHookInvocationHandler implements InvocationHandler {
    public static final String TAG = AmsHookInvocationHandler.class.getSimpleName();

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    private Object mBase;

    public AmsHookInvocationHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 拦截正常的startActivity，替换intent内容，达到启动未在manifest文件中注册过的activity的效果
        if ("startActivity".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, TAG+"->" + "invoke startActivity");
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
                MLog.d(MLog.TAG_HOOK, TAG+"->" + "invoke 启动实名Activity，替换其中的Component");
                Intent fakeIntent = new Intent();
                // 这里包名直接写死,如果再插件里,不同的插件有不同的包  传递插件的包名即可
                String targetPackage = "com.jason.workdemo";
                ComponentName componentName = new ComponentName(targetPackage, StubActivity.class.getCanonicalName());
                // 其实不需要使用预先注册的activity，直接使用宿主本身存在的activity就可以
//                ComponentName componentName = new ComponentName(targetPackage, MainActivity.class.getCanonicalName());
                fakeIntent.setComponent(componentName);
                fakeIntent.putExtra(EXTRA_TARGET_INTENT, raw);

                args[index] = fakeIntent;
            } else {
                MLog.d(MLog.TAG_HOOK, TAG+"->" + "invoke 非实名启动activity，不需要替换");
            }
        }
        return method.invoke(mBase, args);
    }
}