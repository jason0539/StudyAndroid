package com.jason.workdemo.plugin.hook;

import android.content.ClipData;
import android.os.IBinder;

import com.jason.common.utils.MLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by liuzhenhui on 2016/12/29.
 */
public class BinderHookHandler implements InvocationHandler {
    public static final String TAG = BinderHookHandler.class.getSimpleName();

    //原始的Service对象 (IInterface)
    Object base;

    public BinderHookHandler(IBinder base, Class<?> stubClass) {
        try {
            Method asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            this.base = asInterfaceMethod.invoke(null, base);
        } catch (Exception e) {
            MLog.d(MLog.TAG_HOOK, "BinderHookHandler->" + "BinderHookHandler hook failed, e = " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("getPrimaryClip".equals(method.getName())) {
            MLog.d(MLog.TAG_HOOK, "BinderHookHandler->" + "invoke getPrimaryClip");
            return ClipData.newPlainText(null, "You are hooked");
        }

        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }

        return method.invoke(base, args);
    }
}
