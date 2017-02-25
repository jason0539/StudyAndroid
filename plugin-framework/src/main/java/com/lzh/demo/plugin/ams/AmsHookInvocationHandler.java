package com.lzh.demo.plugin.ams;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Pair;

import com.jason.common.utils.MLog;
import com.lzh.demo.plugin.StubActivity;
import com.lzh.demo.plugin.service.ProxyService;
import com.lzh.demo.plugin.service.ServiceLoadHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/7.
 */
public class AmsHookInvocationHandler implements InvocationHandler {
    public static final String TAG = AmsHookInvocationHandler.class.getSimpleName();

    //// TODO: 2017/2/25 应该动态获取宿主包名
    public static final String HOST_PACKAGE_NAME = "com.lzh.demo.plugin";
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
            Pair<Integer,Intent> firstIntentPair = findFirstIntentOfArgs(args);
            int indexOfFirstIntent = firstIntentPair.first;
            Intent rawIntent = firstIntentPair.second;

            if (rawIntent.getComponent() != null) {
                MLog.d(MLog.TAG_HOOK, TAG+"->" + "invoke 启动实名Activity，替换其中的Component");
                Intent fakeIntent = new Intent();
                // 这里包名直接写死,如果再插件里,不同的插件有不同的包  传递插件的包名即可
                String targetPackage = HOST_PACKAGE_NAME;
                ComponentName componentName = new ComponentName(targetPackage, StubActivity.class.getCanonicalName());
                // 其实不需要使用预先注册的activity，直接使用宿主本身存在的activity就可以
//                ComponentName componentName = new ComponentName(targetPackage, MainActivity.class.getCanonicalName());
                fakeIntent.setComponent(componentName);
                fakeIntent.putExtra(EXTRA_TARGET_INTENT, rawIntent);

                args[indexOfFirstIntent] = fakeIntent;
            } else {
                MLog.d(MLog.TAG_HOOK, TAG+"->" + "invoke 非实名启动activity，不需要替换");
            }
        }
        if ("startService".equals(method.getName())) {
            //API 23:
            // public ComponentName startService(IApplicationThread caller, Intent service,
            //        String resolvedType, int userId) throws RemoteException

            //找到参数里面的第一个Intent对象
            Pair<Integer,Intent> firstIntentPair = findFirstIntentOfArgs(args);
            int indexOfFirstIntent = firstIntentPair.first;
            Intent rawIntent = firstIntentPair.second;
            Intent fakeIntent = new Intent();

            //代理Service的包名，也就是我们自己的包名
            String targetPackage = HOST_PACKAGE_NAME;

            //把要启动的Service替换为ProxyService，让ProxyService接收生命周期回掉
            ComponentName componentName = new ComponentName(targetPackage, ProxyService.class.getName());
            fakeIntent.setComponent(componentName);

            //把我们原始要启动的TargetService先存起来
            fakeIntent.putExtra(EXTRA_TARGET_INTENT,rawIntent);

            //替换掉Intent，欺骗AMS
            args[indexOfFirstIntent] = fakeIntent;
        }

        if ("stopService".equals(method.getName())) {
            // public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException
            Intent rawIntent = findFirstIntentOfArgs(args).second;
            if (!TextUtils.equals(HOST_PACKAGE_NAME,rawIntent.getComponent().getPackageName())) {
                // 插件的intent才做hook
                return ServiceLoadHelper.stopService(rawIntent);
            }
        }
        return method.invoke(mBase, args);
    }

    private Pair<Integer, Intent> findFirstIntentOfArgs(Object[] args) {
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                index = i;
                break;
            }
        }
        return Pair.create(index, (Intent)args[index]);
    }
}