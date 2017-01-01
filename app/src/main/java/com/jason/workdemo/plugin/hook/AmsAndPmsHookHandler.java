package com.jason.workdemo.plugin.hook;

import com.jason.common.utils.MLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by liuzhenhui on 2017/1/1.
 * 一个简单的用来演示的动态代理对象 (PMS和AMS都使用这一个类)
 * 只是打印日志和参数; 以后可以修改参数等达到更加高级的功能
 */

public class AmsAndPmsHookHandler implements InvocationHandler {

    private static final String TAG = "HookHandler";

    private Object mBase;

    public AmsAndPmsHookHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MLog.d(MLog.TAG_HOOK, "AmsAndPmsHookHandler->" + "you are hooked!! invoke method:" + method.getName() + " called with args:" + Arrays.toString(args));

        return method.invoke(mBase, args);
    }
}