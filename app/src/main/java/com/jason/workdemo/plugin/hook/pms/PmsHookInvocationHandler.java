package com.jason.workdemo.plugin.hook.pms;

import com.jason.common.utils.MLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2017/1/1.
 * 一个简单的用来演示的动态代理对象 (PMS和AMS都使用这一个类)
 * 只是打印日志和参数; 以后可以修改参数等达到更加高级的功能
 */

public class PmsHookInvocationHandler implements InvocationHandler {

    private static final String TAG = "HookHandler";

    private Object mBase;

    public PmsHookInvocationHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getInstalledApplications".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "invoke getInstalledApplications");
        }
        if ("getPackageInfo".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, TAG + "->" + "invoke getPackageInfo");
            // initializeJavaContextClassLoader 这个方法内部无意中检查了这个包是否在系统安装
            // 如果没有安装, 直接抛出异常, 应该临时Hook掉 PMS, 绕过这个检查.
            // 但是不做hook好像也可以？先不管，知道就好
//            return new PackageInfo();
        }
        return method.invoke(mBase, args);
    }
}