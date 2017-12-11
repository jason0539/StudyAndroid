package com.lzh.demo.plugin.binder;

import android.content.ClipData;
import android.os.IBinder;

import com.jason.common.utils.MLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class BinderHookInvocationHandler implements InvocationHandler {
    public static final String TAG = BinderHookInvocationHandler.class.getSimpleName();

    Object base;

    public BinderHookInvocationHandler(IBinder base, Class<?> stubClass) {
        try {
            //asInterface拿到原始的Service对象 (IInterface)，用于与服务进程通信
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "BinderHookHandler->" + "BinderHookHandler hook failed, e = " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //这里并没有真正去系统服务的CLIPBOARD_SERVICE中查询剪贴板内容，而是在应用进程自己返回了伪造数据
        if ("getPrimaryClip".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, "BinderHookHandler->" + "invoke getPrimaryClip");
            return ClipData.newPlainText(null, "You are hooked");
        }

        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }

        //真正调用系统的CLIPBOARD_SERVICE服务进程
        return method.invoke(base, args);
    }
}
